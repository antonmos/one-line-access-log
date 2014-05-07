one-line-access-log plugin
==========================

Grails does not come with an Grails-centric access log which shows useful info for every request.

This plugin sets up a filter that logs the following info:
 * source IP address
 * http response status code
 * total time and percentage spend in view processing
 * http method
 * controller and action names
 * params hash with configurable list of sensitive parameters that should be filtered, e.g. passwords

Example log entry:
> (127.0.0.1) Status 200 in 3.525s (view 73%) for [GET] to PromotionController#dashboard [omhPropertyId:1, password:[FILTERED], promoStatus:[APP, FUT], promoType:[DO, FN, PO, VA]] 

### Installation

* add as runtime dependency to BuildConfig.groovy
* add grails.plugins.olal.paramsToFilter to Config.groovy
* Configure log4j loggers in Config.groovy, e.g. *root { info 'stdout' }*

### Configuration

#### Configuration of application-specific Grails Filters
To ensure that your application's LogginFilter is executed before the plugin log is written, add the following to Config.groovy
> grails.plugins.olal.dependsOnFilters = [filters.LoggingFilters.class]

This can be used to set app-specific date into the Log4j's MDC.

#### Configuration of sensitive parameters that should be filtered from the logs.
Configure list of parameter names for the values should be replaced with '[FILTERED]' in the log.
> grails.plugins.olal.paramsToFilter = ['password’]
    

