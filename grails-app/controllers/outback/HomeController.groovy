package outback

import grails.converters.JSON

class HomeController {

    def index = {
        render([CurrentVersion: 100,
                MinVersion: 100,  // The mininum API versions clients must use to understand our responses.
                ServerVersion: grailsApplication.metadata.'app.version'] as JSON);
    }
}
