package kangaroo.data

import kangaroo.Professor
import kangaroo.Term
import org.springframework.transaction.annotation.Transactional

class BackendDataService {

    // The current term.
    static final String CURRENT_TERM_CODE = "12SP"

    def departmentDataService
    def majorDataService
    def requirementsDataService
    def facultyDataService

    @Transactional
    def upgradeAllIfNeeded() {

        println "\nUpgrading backend data..."
        departmentDataService.upgradeIfNeeded()
        majorDataService.upgradeIfNeeded()
        requirementsDataService.upgradeIfNeeded()

        if (Professor.count() == 0)
            facultyDataService.fetchAndMatch()
    }

    def reset() {
        println "\nResetting backend data version..."
        DepartmentDataService.lastVersionUsed = 0
        RequirementsDataService.lastVersionUsed = 0
        MajorDataService.lastVersionUsed = 0
    }

    static Term getCurrentTerm() { return Term.findOrCreate(CURRENT_TERM_CODE)}
}
