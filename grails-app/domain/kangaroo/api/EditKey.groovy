package kangaroo.api

class EditKey {

    String id = kangaroo.AppUtils.generateRandomToken()

    static constraints = {
        id(maxSize: 32, blank: false)
    }

    static mapping = {
        id(column: 'edit_key', generator: 'assigned')
        version(false)
    }
}
