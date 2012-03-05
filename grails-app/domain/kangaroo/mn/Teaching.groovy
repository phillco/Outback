package kangaroo.mn

import kangaroo.Course
import kangaroo.Professor

/**
 * Many-many relationship between professors and courses (professor teaches these courses; course is taught at these times).
 */
class Teaching implements Serializable {

    Professor professor

    Course course

    static mapping = {
        id(composite: ['professor', 'course'])
        version(false)
    }
}
