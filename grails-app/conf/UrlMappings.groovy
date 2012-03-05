class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: "home")

        "/professor/$id"(controller: "professor", action: "individual")

        "500"(view: '/error')
    }
}
