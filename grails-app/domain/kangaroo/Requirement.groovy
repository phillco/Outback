package kangaroo

import kangaroo.mn.CourseFulfillsRequirement

/**
 * Represents a graduation requirement code (i.e. "HU" - "Humanities").
 *
 * Students need x number of courses in each distribution to graduate (@todo put these details into Kangaroo).
 * Individual courses are listed as fulfilling some requirements.
 */
class Requirement {

    String id // The code ("HU", "SS")
    String name
    boolean isInterdisciplinaryMajor

    static constraints = {
        id(maxSize: 8)
        name(maxSize: 96)
    }

    static mapping = {
        id(column: 'code', generator: 'assigned')
    }

    List<Course> getCoursesThatFulfill() { CourseFulfillsRequirement.findAllByRequirement(this)*.course; }

    String toString() { name }
}
