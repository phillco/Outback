package outback

import grails.converters.JSON

class BaseController {

    def unsupportedRequestError() {
        response.status = 405;
        render([error: "NotSupported", errorMessage: "${request.method} is not supported here."] as JSON)
    }

    def missingParametersError(missingParameters) {
        response.status = 400;
        render([error: "MissingParameters", errorMessage: "The request is missing the indicated parameters.", missingParameters: missingParameters] as JSON)
    }
}
