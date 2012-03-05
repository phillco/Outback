package outback

import grails.converters.JSON
import kangaroo.Professor

class ProfessorController {

    def individual = {

        switch (request.method) {
            case "GET":
                showIndividual();
                break;
            case "DELETE":
                deleteIndividual();
                break;
        }
    }

    def showIndividual() {
        def professor = getSelected()
        if (professor)
            render(professor as JSON)
        else
            notFoundError()
    }

    def deleteIndividual() {
        def professor = getSelected()
        if (professor) {
            professor.delete(flush: true)
            if (professor.hasErrors()) {
                response.status = 500;
                render([error: "ValidationFailed", errorMessage: "The indicated professor could not be deleted.", errorDetails: professor.errors.allErrors.join("/")] as JSON)
            }
            else
                render([status: "Deleted"] as JSON)
        }
        else
            notFoundError()
    }

    Professor getSelected() { Professor.get(params.id) }

    def notFoundError() {
        response.status = 404;
        render([error: "NotFound", errorMessage: "The indicated professor could not be found."] as JSON)
    }
}
