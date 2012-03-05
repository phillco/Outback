package kangaroo.data

import kangaroo.Term

class BackendDataService {

    // The current term.
    static final String CURRENT_TERM_CODE = "12SP"

    static Term getCurrentTerm() { return Term.findOrCreate(CURRENT_TERM_CODE)}
}
