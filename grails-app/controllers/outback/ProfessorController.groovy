package outback

import grails.converters.JSON
import kangaroo.Professor

class ProfessorController extends BaseController {

    def show = {
        def professor = getSelected()
        if (professor)
            render(professor as JSON)
        else
            notFoundError()
    }

    def update = {

        if (params.professor) {

            // Create or update the professor.
            def professor = Professor.findOrCreateWhere(id: params.id)
            bindData(professor, params.professor);
            professor.save();

            if (professor.hasErrors()) {
                response.status = 500;
                render([error: "ValidationFailed", errorMessage: "The indicated professor could not be saved.", errorDetails: professor.errors.allErrors.join("/")] as JSON)
            }
            else {
                response.status = 201;
                render([status: "Saved"] as JSON)
            }
        }
        else
            missingParametersError(['<body>'])
    }

    def delete = {

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
