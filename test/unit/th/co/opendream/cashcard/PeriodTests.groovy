package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Period)
@Mock([LoanType, Contract, Member])
class PeriodTests extends DomainTestTemplate  {

    def requiredProperties() {
        ['contract', 'amount', 'no', 'dueDate', 'status', 'payoffStatus',
         'payoffDate', 'cooperativeInterest', 'receiveTransaction',
         'interestAmount', 'interestOutstanding', 'interestPaid']
    }

    def domainClass() {
        Period.class
    }

    void testValidateContract() {
        mockForConstraintsTests(Period)

        def period = new Period(),
            field = 'contract'

        verifyNotNull(period, field)

        period[field] = [id: 1]
        assertTrue "${field} must pass all validations.",
            period.validate([field])
    }

    void testValidateAmount() {
        mockForConstraintsTests(Period)

        def period = new Period(),
            field = 'amount'

        assertTrue "${field} default value must pass all validations.",
            period.validate([field])

        period[field] = 200.00
        assertTrue "${field} value = 200.00 must pass all validations.",
            period.validate([field])
    }

    void testValidateNo() {
        mockForConstraintsTests(Period)

        def period = new Period(),
            field = 'no'

        verifyNotNull(period, field)

        period[field] = 1
        assertTrue "${field} value = ${period[field]} must pass all validations.",
            period.validate([field])
    }

    void testValidateDueDate() {
        mockForConstraintsTests(Period)

        def period = new Period(),
            field = 'dueDate'

        assertTrue "${field} value = ${period[field]} must pass all validations.",
            period.validate([field])

        period[field] = new Date()
        assertTrue "${field} value = ${period[field]} must pass all validations.",
            period.validate([field])
    }

    void testValidatePayoffDate() {
        mockForConstraintsTests(Period)

        def period = new Period(),
            field = 'payoffDate'

        assertTrue "${field} value = ${period[field]} must pass all validations.",
            period.validate([field])

        period[field] = new Date()
        assertTrue "${field} value = ${period[field]} must pass all validations.",
            period.validate([field])
    }

    void testValidateStatus() {
        mockForConstraintsTests(Period)

        def period = new Period(),
            field = 'status'

        assert period[field] == false
        assertTrue "${field} value = ${period[field]} must pass all validations.",
            period.validate([field])

        period[field] = true
        assertTrue "${field} value = ${period[field]} must pass all validations.",
            period.validate([field])
    }

    void testValidatePayoffStatus() {
        mockForConstraintsTests(Period)

        def period = new Period(),
            field = 'payoffStatus'

        assert period[field] == false
        assertTrue "${field} value = ${period[field]} must pass all validations.",
            period.validate([field])

        period[field] = true
        assertTrue "${field} value = ${period[field]} must pass all validations.",
            period.validate([field])
    }

    def generateValidPeriod() {
        def commonLoan = new LoanType(
            name: "Common", processor: "Commission", interestRate: 18.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: false,
            numberOfPeriod: 3
        ).save()

        def member = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")

        def contract = new Contract(
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

        new Period(
            contract: contract,
            amount: 200.00,
            no: 1,
            dueDate: new Date().plus(10),
            status: true,
            payoffStatus: false
        )
    }

    void testBeforeSaveNoPartialPayoff() {
        def period = generateValidPeriod()

        assert period.outstanding == 0.00
        assert period.payoffStatus == false
        assert period.partialPayoff == null

        period.beforeInsert.call()


        period.payAmount = 200
        period.beforeUpdate.call()

        assert period.partialPayoff == false
        assert period.payoffStatus == true
    }

    void testBeforeSavePartialPayoff() {
        def period = generateValidPeriod()

        assert period.outstanding == 0.00
        assert period.payoffStatus == false
        assert period.partialPayoff == null

        // 1st Partial
        period.payAmount = 100.00
        period.beforeInsert.call()
        period.beforeUpdate.call()

        assert period.partialPayoff == true
        assert period.payoffStatus == false
        assert period.outstanding == 100.00


        // 2nd Partial
        period.payAmount = 50.00
        period.beforeUpdate.call()

        assert period.partialPayoff == true
        assert period.payoffStatus == false
        assert period.outstanding == 50.00

        // Last Partial
        period.payAmount = 50.00
        period.beforeUpdate.call()

        assert period.partialPayoff == false
        assert period.payoffStatus == true
        assert period.outstanding == 0.00
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

    void testReceiveTransaction() {
        mockForConstraintsTests(Period)

        def period = new Period(),
            field = 'receiveTransaction'

        period[field] = []
        assertTrue "${field} must pass all validations.",
            period.validate([field])
    }
}
