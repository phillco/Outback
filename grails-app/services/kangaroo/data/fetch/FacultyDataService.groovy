package kangaroo.data.fetch

import groovy.util.slurpersupport.GPathResult

import kangaroo.Professor
import kangaroo.AppUtils

/**
 * Downloads the faculty page from austincollege.edu, which contains useful data like photos, titles, phone numbers, etc.
 * This data is used to augment existing teacher profiles.
 */
class FacultyDataService {

    static transactional = true

    // These are faculty that have different names on the faculty page (left) than WebHopper (right).
    static def synonyms = [
            "Robert Cape": "Bob Cape",
            "Daniel Dominick": "Dan Dominick",
            "Steven Goldsmith": "Steve Goldsmith",
            "Henry Gorman": "Hank Gorman",
            "Gregory Kinzer": "Greg Kinzer",
            "Jacqueline Moore": "Jackie Moore",
            "Stephen Ramsey": "Steve Ramsey",
            "Donald Rodgers": "Don Rodgers",
            "Donald Salisbury": "Don Salisbury",
            "Timothy Tracz": "Tim Tracz",
            "Ivette Vargas-O'Bryan": "Ivette Vargas",
            "Michael Wallo": "Michael C. Wallo",
            "Brian Watkins": "Brian A. Watkins",
    ]

    /**
     * Returns the Austin College faculty page as raw text.
     */
    static String getFacultyPageAsText() { new URL('http://www.austincollege.edu/academics/faculty/').text }

    /**
     * Returns the Austin College faculty page as a cleaned Groovy XML tree.
     */
    static GPathResult getFacultyPageAsXml() {AppUtils.cleanAndConvertToXml(facultyPageAsText)}

    /**
     * Returns just the list of faculty as a cleaned Groovy XML tree.
     */
    static GPathResult getFacultyList() { facultyPageAsXml.depthFirst().collect { it }.find { it.name() == "ul" && it.@class == 'staffList' } }

    def fetchAndMatch() {

        // Download and extract the faculty list.
        AppUtils.runAndTime("Faculty fetched and matched") {
            println 'Fetching faculty page...'
            matchScrapedFaculty(extractRawData())
        }
    }

    /**
     * Converts the raw XML into a useful model (a list of string maps).
     * Example: [["name": "Aaron Block", "title": "Assistant Professor"], ["name":"Micheal Higgs", title: "ACJavatron"], ...]
     */
    static List<Map> extractRawData() {
        facultyList.depthFirst().collect { it }.findAll { it.name() == "li" }.collect {
            def map = [:];
            map.name = it.h3; // yum!
            map.photoUrl = it.h3.a.img.@src
            map.title = it.div[0]
            map.department = it.div[1]
            map.office = it.div[2]
            map.phone = it.div[3]
            map.email = it.div[4]

            // Convert the map values from Nodes to strings.
            def stringMap = [:]
            map.each { key, value -> stringMap[key] = value.toString() }
            return stringMap;
        }
    }

    /**
     * Takes the processed data from the faculty list and (tries to) match it to the professors in the database.
     */
    void matchScrapedFaculty(scraped) {

        print 'Matching downloaded faculty...'

        scraped.each { createProfessor(it) }

        // Professor.findAllByMatched(false).each { println "Not Matched: ${it}"}
        def matched = Professor.countByMatched(true);
        def percent = (Professor.count() > 0) ? ((double) ((matched / Professor.count()) * 100)).round() : 0;
        println "...done! ${matched} matched of ${Professor.count()} (${percent}%)."
    }

    /**
     * Updates the professor to match the raw data given.
     */
    def createProfessor(Map rawData) {
        def professor = new Professor()

        def name = AppUtils.cleanFacultyName(rawData.name);
        professor.id = AppUtils.extractProfessorUsername(rawData.email);
        professor.firstName = name.firstName
        professor.lastName = name.lastName
        professor.matched = true;
        professor.photoUrl = rawData.photoUrl
        professor.title = rawData.title
        professor.department = rawData.department
        professor.office = rawData.office
        professor.email = rawData.email
        professor.phone = rawData.phone
        professor.save();
        if (professor.hasErrors()) println professor.errors
    }
}
