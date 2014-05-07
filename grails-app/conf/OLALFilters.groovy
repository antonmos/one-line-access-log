import grails.util.GrailsNameUtils

import javax.annotation.PostConstruct
import java.util.regex.Pattern

class OLALFilters {

    private static final String START_TIME_ATTRIBUTE = 'OLALController__START_TIME__'
    private static final String VIEW_START_TIME_ATTRIBUTE = 'OLALController__VIEW_START_TIME__'

    def grailsApplication
    def dependsOn = []

    private Pattern filterParamRegExp

    @PostConstruct
    def init() {
        def conf = grailsApplication.config.grails.plugins.olal.dependsOnFilters
        dependsOn =   conf ? conf : []

        compileParamFilterPattern()
    }

    private void compileParamFilterPattern() {
        def paramFilterConf = grails.exceptionresolver.params.exclude

        if (paramFilterConf) {
            def patterns = paramFilterConf.clone()
            paramFilterConf.each {
                patterns << ".*\\.$it"
            }
            filterParamRegExp = Pattern.compile(patterns.join('|'), Pattern.CASE_INSENSITIVE)
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

                int actionPct = (double)(view * 100) / total
                log.info("($request.remoteAddr) Status $response.status in ${total/1000}s (view $actionPct%) for [$request.method] to $formattedCtrlName#$actionName ${actionParamsOnly} ")

            }
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

        if(filterParamRegExp) {
            return filterParams(queryParams)
        } else {
            return queryParams;
        }
    }
    def filterParams(Map params) {

        def filtered = [:]

        params.each { String key, value ->
            if (filterParamRegExp.matcher(key).matches()) {
                value = '[FILTERED]'
            } else if (value instanceof Map) {
                value = filterParams(value)
            } else if (value.class.isArray()) {
                value.each { it instanceof Map ? filterParams(it) : it }
            }

            filtered[key] = value
        }
        return filtered
    }
}
