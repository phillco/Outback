import kangaroo.api.EditKey

class BootStrap {

    def init = { servletContext ->

        if (EditKey.count() == 0)
            new EditKey().save();

    }


    def destroy = {
    }
}
