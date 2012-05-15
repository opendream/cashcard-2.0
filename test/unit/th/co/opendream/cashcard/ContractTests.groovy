package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Contract)
class ContractTests {

    def commonLoan

    @Before
    void setUp() {
        commonLoan = new LoanType(name: "Common")
    }

    def generateValidContract() {
        new Contract(
            code: "ก.55-1000-20",
            loanType: commonLoan,
            loanAmount: 2000.00,
            interestRate: 2.00,
            loanBalance: 0.00,
            approvalStatus: false,
            loanReceiveStatus: false
        )
    }

    void verifyNotNull(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail null validation.",
            "nullable", instance.errors[field]
    }

    void verifyNotBlank(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail blank validation.",
            "blank", instance.errors[field]
    }

    void verifyUnique(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail unique validation.",
            "unique", instance.errors[field]
    }

    void testProperties() {
        def requiredProperties = [
            'code'          , 'loanType'        , 'loanAmount' ,
            'interestRate'  , 'loanBalance'     , 'approvalStatus' ,
            'loanReceiveStatus'
        ]

        def instanceProperties = Contract.metaClass.properties*.name

        def missing_properties = requiredProperties - instanceProperties
        assert 0 == missing_properties.size(),
            "Domain class is missing some required properties => ${missing_properties}"
    }

    void testValidateCode() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'code'

        verifyNotNull(contract, field)

        contract[field] = ""
        verifyNotBlank(contract, field)

        def existingContract = generateValidContract().save()
        contract[field] = "ก.55-1000-20"
        verifyUnique(contract, field)

        contract[field] = "Common Loan"
        assertTrue "${field} `${contract.code}` must pass all validations.",
            contract.validate([field])
    }

    void testValidateLoanType() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'loanType'

        verifyNotNull(contract, field)

        contract[field] = commonLoan
        assertTrue "${field} `${contract.code}` must pass all validations.",
            contract.validate([field])
    }

    void testValidateLoanAmount() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'loanAmount'

        // default value
        assertEquals "${field} must have default value as 0.000000",
            contract[field], 0.000000

        contract[field] = null
        verifyNotNull(contract, field)

        contract[field] = 0.000000
        assertTrue "${field} must pass all validations.",
            contract.validate([field])
    }

    void testValidateInterestRate() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'interestRate'

        // default value
        assertEquals "${field} must have default value as 0.00",
            contract[field], 0.00

        contract[field] = null
        verifyNotNull(contract, field)

        contract[field] = 0.00
        assertTrue "${field} must pass all validations.",
            contract.validate([field])
    }

    void testValidateLoanBalance() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'loanBalance'

        // default value
        assertEquals "${field} must have default value as 0.000000",
            contract[field], 0.000000

        contract[field] = null
        verifyNotNull(contract, field)

        contract[field] = 0.000000
        assertTrue "${field} must pass all validations.",
            contract.validate([field])
    }

    void testValidateApprovalStatus() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'approvalStatus'

        // default value
        assertEquals "${field} must have default value as false",
            false, contract[field]

        contract[field] = false
        assertTrue "${field} must pass all validations.",
            contract.validate([field])
    }

    void testValidateLoanReceiveStatus() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'loanReceiveStatus'

        // default value
        assertEquals "${field} must have default value as false",
            false, contract[field]

        contract[field] = false
        assertTrue "${field} must pass all validations.",
            contract.validate([field])
    }
}