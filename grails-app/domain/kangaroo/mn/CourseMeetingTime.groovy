package kangaroo.mn

import kangaroo.Course
import kangaroo.MeetingTime

/**
 * Many-many relationship between courses and meeting times (course meets at these times).
 */
class CourseMeetingTime implements Serializable {

    Course course

    MeetingTime meetingTime

    static mapping = {
        id(composite: ['course', 'meetingTime'])
        version(false)
    }
}
