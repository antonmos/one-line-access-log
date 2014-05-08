import filters.OLALFilters
import filters.ParameterFilter
import grails.test.mixin.TestFor
import org.apache.commons.logging.Log
import spock.lang.Specification

@TestFor(OLALFilters)
class OLALFiltersTest extends Specification {

    def setupSpec() {
        grailsApplication.config.grails.exceptionresolver.params.exclude = ['password']
    }
    def testFilter() {

        setup:
        def mockLogger = Mock(Log)
        OLALFilters filter = grailsApplication.mainContext.getBean('filters.OLALFilters')
        filter.log = mockLogger
        when:
        //Grails adds these to the params has that the filters get, but withFilters doesnt do it.
        params['action'] = 'index'
        params['controller'] = 'test'
        params['format'] = 'test'
        params['password'] = 'secret'

        withFilters ( controller: 'test', action: 'index' ) {
        }

        then:
           response.status == 200
           1 * mockLogger.info({
               def statusOk = it.contains("Status 200")
               def params = it[it.lastIndexOf('#index')+7..it.lastIndexOf(']')]
               def paramsOk = !params.contains('index') && !params.contains('controller') && !params.contains('format')
               println params
               def filteringOk = params.contains(ParameterFilter.REPLACEMENT)
               def controllerAndActionOk = it.contains("TestController#index")
               statusOk && paramsOk && controllerAndActionOk && filteringOk
           })
    }
}
