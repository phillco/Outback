package kangaroo

import grails.converters.JSON
import redis.clients.jedis.Jedis

/**
 * Returns data formatted for the client-side DataTables.
 */
class DataTablesService {

    static transactional = false

    def redisService

    /**
     * Formats all of the courses into a table.
     */
    def getTable(term) {
        def rows = Course.findAllByTerm(term).collect { course -> toRow(course) }
        return (["sEcho": 0, "iTotalRecords": rows.size(), "iTotalDisplayRecords": rows.size(), "aaData": rows] as JSON)
    }

    /**
     * Returns a redis-cached version of the table.
     */
    String getTableCached(term) { redisService.memoize("courses/$term") { Jedis redis -> getTable(term) } }

    /**
     * Formats a given course to fit in a table row.
     */
    def toRow(Course course) {

        def row = []

        row << "<a href='${AppUtils.createLink('course', course.id)}'>${course}</a> <span class='section'>${course.sectionString()}</span>"
        row << course.department.name
        if (course.instructors)
            row << AppUtils.getProfessorLinksForClass(course, false, "<br/>");
        else
            row << "<i>Unknown<i/>"

        if (course.meetingTimes)
            row << AppUtils.getScheduleLinksForClass(course)
        else
            row << "<i>Unknown<i/>"

        return row;
    }
}
