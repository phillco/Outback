package outback

import grails.converters.JSON
import kangaroo.api.EditKey

class BaseController {

    boolean isAuthenticated() {

        def auth = extractAuthentication();
        if (auth?.username == "outback" && auth?.password && EditKey.exists(auth.password))
            return true;

        return false;
    }

    def notAuthenticatedError() {
        response.status = 401;
        render([error: "NotAuthorized", errorMessage: "You must supply a valid authorization token to use this action."] as JSON)
    }

    def extractAuthentication() {

        String[] header = request.getHeader("Authorization")?.split(" ");
        if (header.size() == 2 && header[0] == "Basic") {

            String decoded = new String(header[1].decodeBase64());

            //
            String[] parts = decoded.split(":", 2);
            return [username: parts[0], password: parts[1]]
        }
    }
}
