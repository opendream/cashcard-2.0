package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(LoanType)
class LoanTypeTests {

    def generateValidLoanType(name) {
        new LoanType(
            name: name
        )
    }

    void testProperties() {
        def requiredProperties = ['name'],
            instanceProperties = LoanType.metaClass.properties*.name

        def missing_properties = requiredProperties - instanceProperties
        assert 0 == missing_properties.size(),
            "Domain class is missing some required properties => ${missing_properties}"
    }

    void testValidateName() {
        mockForConstraintsTests(LoanType)

        def loanType = new LoanType(),
            field = 'name'

        loanType.validate([field])
        assertEquals "Name must fail null validation.",
            "nullable", loanType.errors[field]

        loanType.name = ""
        loanType.validate([field])
        assertEquals "Name must fail blank validation.",
            "blank", loanType.errors[field]

        def existingLoanType = generateValidLoanType("Existing Loan").save()
        loanType.name = "Existing Loan"
        loanType.validate([field])
        assertEquals "Name must fail unique validation.",
            "unique", loanType.errors[field]

        loanType.name = "Common Loan"
        assertTrue "Name ${loanType.name} must pass all validations.",
            loanType.validate([field])
    }
}
