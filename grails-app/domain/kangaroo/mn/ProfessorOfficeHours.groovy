package kangaroo.mn

import kangaroo.MeetingTime
import kangaroo.Professor

/**
 * Many-many relationship between professors and office hours, which are stored as MeetingTimes (professor has these office hours).
 */
class ProfessorOfficeHours implements Serializable {

    Professor professor

    MeetingTime meetingTime

    static mapping = {
        id(composite: ['professor', 'meetingTime'])
        version(false)
    }

}
