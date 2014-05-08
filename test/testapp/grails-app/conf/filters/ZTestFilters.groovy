package filters

class ZTestFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                 log.info "before"
            }
            after = { Map model ->
                log.info "after"
            }
            afterView = { Exception e ->
                log.info "afterView"
            }
        }
    }
}
