package kangaroo

/**
 * Represents a major or minor. The actual requirements for the major are currently not normalized (ie, a table for subrequirements), and are simply stored as HTML.
 * This is because they're so complicated -- approaching that problem will be quite an undertaking.
 */
class Major {

    // The name of this major (often just the name of the department, i.e. "Mathematics").
    String name

    // Stores a description of the requirements for this major or minor (HTML).
    String description

    // Is this a major or a minor?
    boolean isMajor

    Department department

    static constraints = {
        name(maxSize: 128)
        description(maxSize: 8192)
    }
}
