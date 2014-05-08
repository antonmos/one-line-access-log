import filters.ParameterFilter
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class ParameterFilterTest extends Specification {
    private static final String PASSWORD = 'password'
    @Shared ParameterFilter filter
    def setupSpec() {
        filter = new ParameterFilter([PASSWORD])
    }


    @Unroll
    def 'test filtering one level map #params'() {

        when:
            Map filtered = filter.filterParams(params)
        then:
            filtered.size() == params.size()
            filtered[filteredKey] == ParameterFilter.REPLACEMENT
            filtered.each { it.key != filteredKey && it.value == params[it.key]}


        where:
          params                                | filteredKey
          ['password' : 'secret']                 | 'password'
          ['PASSword' : 'secret', foo : "bar"]    | 'PASSword'
          ['foo.pasSworD' : 'secret']             | 'foo.pasSworD'

    }

    def 'test filted nested map'() {

        def params = [foo: 'bar', more: [bar: 'foo', password: 'secret']]
        when:
            def filtered = filter.filterParams(params)
        then:
           filtered['more']['password'] == ParameterFilter.REPLACEMENT
    }


}
