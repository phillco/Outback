package kangaroo

/**
 * Departments (Math, CS, Astrology) contain courses, majors, and professors.
 */
class Department {

    // The official code for this department (eg, "CS")
    String id

    // The full name of this department (eg, "Computer Science")
    String name = id

    static constraints = {
        id(maxSize: 4)
        name(maxSize: 64)
    }

    static mapping = {
        id(column: 'code', generator: 'assigned')
    }

    String toString() { name }
}
