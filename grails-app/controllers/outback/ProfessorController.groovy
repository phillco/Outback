package outback

import grails.converters.JSON
import kangaroo.Professor

class ProfessorController {

    def individual = {

        switch (request.method) {
            case "GET":
                showIndividual();
                break;
        }
    }

    def showIndividual() {
        def professor = Professor.get(params.id)
        if (professor)
            render(professor as JSON)
        else
            render([error: "NotFound", errorMessage: "The indicated professor could not be found."] as JSON)
    }
}
