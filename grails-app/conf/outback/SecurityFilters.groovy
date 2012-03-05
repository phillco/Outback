package outback

import grails.converters.JSON
import kangaroo.api.EditKey

/**
 * [PC] Requires all non-GET API calls to include a valid EditKey using HTTP Basic Authentication.
 */
class SecurityFilters {

    def filters = {

        all(controller: '*', action: '*') {

            before = {

                // Check for, and verify, the edit key if needed.
                if (request.method != "GET" && !isAuthenticated(request)) {
                    notAuthenticatedError(owner, response);
                    return false;
                }

                return true;
            }
        }
    }

    boolean isAuthenticated(request) {

        def auth = extractAuthentication(request);
        if (auth?.username == "outback" && auth?.password && EditKey.exists(auth.password))
            return true;

        return false;
    }

    def notAuthenticatedError(filter, response) {
        response.status = 401;
        filter.render([error: "NotAuthorized", errorMessage: "You must supply a valid authorization token to use this action."] as JSON)
    }

    def extractAuthentication(request) {

        String[] header = request.getHeader("Authorization")?.split(" ");
        if (header.size() == 2 && header[0] == "Basic") {

            String decoded = new String(header[1].decodeBase64());

            //
            String[] parts = decoded.split(":", 2);
            return [username: parts[0], password: parts[1]]
        }
    }
}
