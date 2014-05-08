package filters
import java.util.regex.Pattern

class ParameterFilter {
    Pattern filterParamRegExp
    public static final REPLACEMENT = '[FILTERED]'

    public ParameterFilter(def paramNames) {
        def patterns = paramNames.collect { ".*\\.?$it" }
        filterParamRegExp = Pattern.compile(patterns.join('|'), Pattern.CASE_INSENSITIVE)
    }

    def filterParams(Map params) {

        def filtered = [:]

        params.each { String key, value ->
            if (filterParamRegExp.matcher(key).matches()) {
                value = REPLACEMENT
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
