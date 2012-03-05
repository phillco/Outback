package kangaroo

/**
 * Those things that costs tons of money.
 */
class Textbook {

    static belongsTo = [course: Course]

    static transients = ['amazonLink', 'amazonSearchUrl', 'isbn10Digit']

    // 13-digit textbook ISBN. Nearly all Austin College textbooks have ISBNs -- but not all of them! Some only have titles.
    String isbn

    // Details about the textbook.
    String title
    String author
    String edition
    String publisher
    String format

    int copyrightYear
    boolean required // Is this textbook required for a course, or only recommended?
    boolean isDigital // Is this textbook digital-only?

    // Prices.
    double bookstoreNewPrice
    double bookstoreUsedPrice
    double bookstoreRentalPrice
    double amazonPrice

    // Did we successfully match this to a textbook on Amazon? If so we should have imageUrl.
    boolean matchedOnAmazon
    String imageUrl

    static constraints = {
        isbn(nullable: true)
        edition(nullable: true)
        publisher(nullable: true)
        format(nullable: true)
        imageUrl(nullable: true)
    }

    String toString() { title }

    /**
     * Converts this textbook's ISBN to the 10-digit variety.
     */
    String getIsbn10Digit() {
        kangaroo.IsbnConverter.convertTo10Digit(isbn)
    }

    /**
     * Returns a link to this page's Amazon listing.
     */
    String getAmazonLink() {

        // Amazon uses 10-digit ISBNS for URLs so we have to convert.
        if (isbn)
            "http://www.amazon.com/dp/${isbn10Digit}/?tag=austincollege-20"
    }

    /**
     * Returns a link to Amazon's search result for this textbook. Useful if they don't have a direct result.
     */
    String getAmazonSearchUrl() {
        return "http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Dstripbooks&field-keywords=${title.encodeAsURL()}&x=0&y=0"
    }

    /**
     * Returns a link to this textbook on Amazon, either directly to the results page, or to a search page listing it.
     */
    String toLink() {
        if (isbn && matchedOnAmazon)
            return amazonLink;
        else
            return amazonSearchUrl;
    }
}
