package filters
import grails.util.GrailsNameUtils

import javax.annotation.PostConstruct

class OLALFilters {

    private static final String START_TIME_ATTRIBUTE = 'OLALController__START_TIME__'
    private static final String VIEW_START_TIME_ATTRIBUTE = 'OLALController__VIEW_START_TIME__'

    def grailsApplication
    def dependsOn = []

    private ParameterFilter paramFilter

    @PostConstruct
    def init() {
        def conf = grailsApplication.config.grails.plugins.one.line.access.log.dependsOnFilters
        dependsOn =   conf ? conf : []
        configure()
    }

    public void configure() {
        def paramFilterConf = grailsApplication.config.grails.exceptionresolver.params.exclude
        if (paramFilterConf) {
            paramFilter = new ParameterFilter(paramFilterConf)
        }
    }

    def filters = {
        all(controller: '*', action: '*') {
            before = {
                request[START_TIME_ATTRIBUTE] = System.currentTimeMillis()
            }

            after = { Map model ->
                request[VIEW_START_TIME_ATTRIBUTE] = System.currentTimeMillis()
            }
            afterView = { Exception e ->
                long now = System.currentTimeMillis()
                long total = now - request[START_TIME_ATTRIBUTE]
                long view = now - request[VIEW_START_TIME_ATTRIBUTE]
                def formattedCtrlName = formatControllerName(controllerName)
                def actionParamsOnly = getActionParams(params)

                int status = getStatus(response)

                int viewPct = (double) (view * 100) / total
                log.info("($request.remoteAddr) Status $status in ${total / 1000}s (view $viewPct%) for [$request.method] to $formattedCtrlName#$actionName ${actionParamsOnly} ")

            }
        }
    }

    int getStatus(def response ) {
        def prop = response.hasProperty('status')
        if(prop?.field || prop?.getter) {
            return response.status
        }
        if (response.hasProperty('response')) {
            return getStatus(response.response)
        } else {
            log.warn("Cannot determine http status in this container")
            return 999
        }
    }

    def formatControllerName(name) {
        GrailsNameUtils.getClassName(name, 'Controller')
    }

    def getActionParams(params) {
        Map queryParams = [:] << params
        queryParams.remove('action')
        queryParams.remove('controller')
        queryParams.remove('format')

        if(paramFilter) {
            return paramFilter.filterParams(queryParams)
        } else {
            return queryParams;
        }
    }

}
