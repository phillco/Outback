package kangaroo.data.prefill

import kangaroo.Requirement
import kangaroo.mn.CourseFulfillsRequirement

class RequirementsDataService extends UpdateableDataService {

    static String name = "Requirements list"
    static String url = "${dataRoot}/requirements.json"
    static int lastVersionUsed = 0;

    @Override
    protected void upgradeAll(dataFromServer) {

        // Add the new requirements.
        def itemsToKeep = []
        dataFromServer.list.each { data ->

            def existing = Requirement.get(data.code)
            if (existing) {
                existing.id = data.code;
                existing.name = data.name;
                existing.isInterdisciplinaryMajor = data.isDiscipline;
                itemsToKeep << existing.save();
            }
            else
                itemsToKeep << new Requirement(id: data.code, name: data.name, isInterdisciplinaryMajor: data.isDiscipline).save();
        }

        // Remove requirements that no longer appear in the list.
        (Requirement.list() - itemsToKeep).each { toDelete ->
            CourseFulfillsRequirement.findAllByRequirement(toDelete).each { it.delete(flush: true) }
            toDelete.delete(flush: true)
        }
    }
}