package kangaroo.data.prefill

import kangaroo.Course
import kangaroo.Department

/**
 * Fills the department with the list of departments.
 */
class DepartmentDataService extends UpdateableDataService {

    static String name = "Departments list"
    static String url = "${dataRoot}/departments.json"
    static int lastVersionUsed = 0;

    def cacheService

    @Override
    protected void upgradeAll(dataFromServer) {

        // Add the new departments.
        def itemsToKeep = []
        dataFromServer.list.each { data ->

            def existing = Department.get(data.code)
            if (existing) {
                existing.id = data.code;
                existing.name = data.name;
                itemsToKeep << existing.save();
            }
            else
                itemsToKeep << new Department(id: data.code, name: data.name).save();
        }

        // Remove departments that no longer appear in the list.
        // NOTE / @todo: Departments cannot be removed if they have courses pointing to them.
        (Department.list() - itemsToKeep).each { toDelete ->
            if (Course.countByDepartment(toDelete) == 0)
                toDelete.delete(flush: true)
        }

        // This modifies the course table.
        cacheService.clearCache()

        // Also, reimport the majors list -- some may match now.
        MajorDataService.lastVersionUsed = 0;
    }
}
