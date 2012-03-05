package kangaroo

/**
 * [PC] Reports bugs to FogBugz. Invoked automatically by the error page.
 *
 * @see coursearch.ErrorController
 */
class BugReportService {

    static transactional = true

    final String FOGBUGZ_URL = "https://austincollege.fogbugz.com";
    final String DEFAULT_SCOUT_MESSAGE = "Default return message";

    def reportBug(bugTitle, bugDetails, customerEmail) {

        // If no title was specified, choose a default one.
        def forceNewBug = 0;
        if (!bugTitle) {
            forceNewBug = 1; // Don't lump bugs with the same title together.
            bugTitle = "Kangaroo Exception";
        }

        // Create the parameters.
        String data = '&ScoutProject=Kangaroo' +
                "&ScoutArea=Error Reports" +
                "&Description=${bugTitle}" +
                "&Extra=${bugDetails}" +
                "&Email=${customerEmail ?: ""}" +
                "&ForceNewBug=${forceNewBug}" +
                "&ScoutDefaultMessage=${DEFAULT_SCOUT_MESSAGE}" +
                "&FriendlyResponse=1" +
                "&ScoutUserName=" + "Exception Reporter";

        // Create the connection...
        log.info "Opening connection to FogBugz...";
        def connection = new URL("${FOGBUGZ_URL}/ScoutSubmit.asp").openConnection();
        connection.setRequestMethod("POST");
        connection.doOutput = true

        // ...add our parameters...
        Writer wr = new OutputStreamWriter(connection.outputStream)
        wr.write(data)
        wr.close()

        // ...and connect!
        connection.connect()

        if (connection.responseCode == 200) {

            // Check if there was a scout message sent back, or just our default string.
            def response = connection.content.text;
            log.info "...success (${response})";
            def specializedMessage = !response.equals(DEFAULT_SCOUT_MESSAGE);

            return [message: specializedMessage ? response : "", specializedMessage: specializedMessage];
        }
        else {
            log.info "...BugReportService.reportBug FAILED"
            return null;
        }
    }
}