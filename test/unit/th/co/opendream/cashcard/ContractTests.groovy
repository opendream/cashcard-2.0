package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Contract)
@Mock([Member])
class ContractTests {

    def commonLoan

    @Before
    void setUp() {
        commonLoan = new LoanType(name: "Common")
    }

    def generateValidContract() {
        def member = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")

        new Contract(
            code: "ก.55-1000-20",
            member: member,
            loanType: commonLoan,
            loanAmount: 2000.00,
            interestRate: 2.00,
            loanBalance: 0.00,
            approvalStatus: false,
            loanReceiveStatus: false,
            guarantor1: "Keng",
            guarantor2: "Neung",
            numberOfPeriod: 3
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
            'loanReceiveStatus', 'guarantor1'   , 'guarantor2' ,
            'numberOfPeriod', 'member'          , 'dateCreated' ,
            'lastUpdated'
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

    void testValidateMember() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'member'

        verifyNotNull(contract, field)

        contract[field] = [id: 1]
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

    void testValidateGuarantor1() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'guarantor1'

        contract[field] = ''
        assertTrue "${field} must pass all validations.",
            contract.validate([field])
    }

    void testValidateGuarantor2() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'guarantor2'

        contract[field] = ''
        assertTrue "${field} must pass all validations.",
            contract.validate([field])
    }

    void testValidateNumberOfPeriod() {
        mockForConstraintsTests(Contract)

        def contract = new Contract(),
            field = 'numberOfPeriod'

        verifyNotNull(contract, field)

        contract[field] = 3
        assertTrue "${field} must pass all validations.",
            contract.validate([field])
    }
}
