class OneLineAccessLogGrailsPlugin {
    // the plugin version
    def version = "0.6  "
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        "lib/grails-rt.jar"
    ]

    def title = "One Line Access Log Plugin" // Headline display name of the plugin
    def author = "Anton Mostovoy"
    def authorEmail = "anton@mostovoy.net"
    def description = '''\
Grails does not come with a Grails-centric access log which shows useful info for every request.
This plugin provides a single line access log for every request in the format:
{code}(10.1.3.5) Status 200 in 0.003s (view 33%) for [GET] to FooBooController#index [asd:324]{code}
'''

    // URL to the plugin's documentation
    def documentation = "https://github.com/antonmos/one-line-access-log/blob/master/README.md"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "Orbitz Worldwide Inc", url: "http://www.orbitz.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "JIRA", url: "https://github.com/antonmos/one-line-access-log/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/antonmos/one-line-access-log" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
        event.ctx.getBean(filters.OLALFilters).configure()
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
