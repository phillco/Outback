import grails.converters.JSON
import kangaroo.Professor
import kangaroo.api.EditKey

class BootStrap {

    def init = { servletContext ->

        if (EditKey.count() == 0)
            new EditKey().save();

        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Professor) {
            return [id: it.id, firstName: it.firstName, middleName: it.lastName, lastName: it.lastName, title: it.title,
                    departmentString: it.department, email: it.email, office: it.office, phone: it.phone, photoURL: it.photoUrl];
        }

    }


    def destroy = {
    }
}
