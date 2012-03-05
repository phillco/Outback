package kangaroo.api

import kangaroo.AppUtils

class EditKey {

    String id = AppUtils.generateRandomToken()

    static constraints = {
        id(maxSize: 32, blank: false)
    }

    static mapping = {
        id(column: 'edit_key', generator: 'assigned')
        version(false)
    }
}
