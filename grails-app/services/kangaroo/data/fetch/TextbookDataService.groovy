package kangaroo.data.fetch

import kangaroo.AppUtils
import kangaroo.Course
import kangaroo.Textbook

class TextbookDataService {

    static transactional = false

    def amazonDataService

    def sessionFactory
    def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP

    def lookupTextbooksForAllCourses() {

        println "Fetching detailed textbook data from bkstr.com..."

        Textbook.list().each { it.delete() };
        cleanUpGorm()
        AppUtils.runAndTime("Textbooks fetched") {
            Course.list().each { course ->
                lookupTextbookInfo(course)
            }
        }
        cleanUpGorm()
    }

    def lookupTextbookInfo(Course course) {

        // Download and extract the list of textbooks.
        Course.withTransaction {

            course = course.merge()

            def page = AppUtils.cleanAndConvertToXml(new URL(course.textbookPageUrl()).text);
            def list = page.depthFirst().collect { it }.find { it.name() == "div" && it.@class.toString().contains("results") }

            int failures = 0;
            list.depthFirst().collect { it }.findAll { it.name() == "ul" }.collect {

                def textbook = new Textbook(course: course);

                it.li.each { detail ->

                    def line = detail.toString().trim();

                    // Information about the textbooks is presented as "TYPE:<data>".
                    def parts = line.split(":");
                    if (parts.size() >= 2) {
                        def key = parts[0].trim().toUpperCase();
                        def value = parts[1..-1].join(":").trim(); // Join any colons in the value field together.
                        parseTextbookDetail(textbook, key, value);
                    }
                }

                textbook.save();
                if (textbook.hasErrors()) {
                    println textbook.errors.toString()
                    failures++
                }
            }

            course.textbooksParsed = !failures;
            course.save(flush: true)
            cleanUpGorm()
        }
    }

    def cleanUpGorm() {
        def session = sessionFactory.currentSession
        session.flush()
        session.clear()
        propertyInstanceMap.get().clear()
    }

    def parseTextbookDetail(Textbook textbook, key, value) {

        switch (key) {
            case 'TITLE':
                textbook.title = value;
                break;

            case 'AUTHOR':
                textbook.author = value;
                break;

            case 'FORMAT':
                textbook.format = value;
                break;

            case 'EDITION':
                textbook.edition = value;
                break;

            case 'COPYRIGHT YEAR':
                def year = safeParseInt(value);
                if (year)
                    textbook.copyrightYear;
                break;

            case 'PUBLISHER':
                textbook.publisher = value;
                break;

            case 'ISBN':
                textbook.isbn = value;
                break;

        // Pricing details...
            case 'NEW':
                textbook.bookstoreNewPrice = AppUtils.parseCurrency(value);
                break;
            case 'USED':
                textbook.bookstoreRentalPrice = AppUtils.parseCurrency(value);
                break;
            case 'RENTAL':
                textbook.bookstoreRentalPrice = AppUtils.parseCurrency(value);
                break;
            case 'DIGITAL':
                textbook.isDigital = true;
                textbook.bookstoreNewPrice = AppUtils.parseCurrency(value);
                break;

            default:
                println "Unused: ${key} (${value})"
        }

    }

    /**
     * If the input is a number, returns it; otherwise returns null.
     */
    def safeParseInt(value) {
        try {
            Integer.parseInt(value)
        } catch (NumberFormatException) {}
    }


}
