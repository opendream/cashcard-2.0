package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ShareCapitalAccountController)
@Mock([Member, Users])
class ShareCapitalAccountControllerTests {

    @Before
    void setUp() {

    }

    def generateMember() {

        def m1 = new Member(identificationNumber:"1159900100015", firstname:"สมหญิง", lastname: "รักเรียน", telNo: "0818526122", gender: "MALE", address: "Opendream")

        m1.save(validate: false)

        m1
    }

    void testCreate() {
        generateMember()

        params.id = '1'
        controller.create()
        assert view == "/shareCapitalAccount/create"

        params.id = '42'
        controller.create()
        assert response.redirectUrl == "/error"
    }

    void testDoCreate() {
        generateMember()

        params.id = '1'
        params.accountNumber = '110-55-0001'
        params.registeredDate = 'date.struct'
        params.registeredDate_day = '10'
        params.registeredDate_month = '1'
        params.registeredDate_year = '2012'
        params.balance = '300.00'

        def count = 0
        def pass = true
        ShareCapitalAccount.metaClass.save = { ->
            delegate.id = 1
            count++
            return pass
        }

        controller.springSecurityService = [principal: [id: 1]]
        controller.doCreate()
        assert response.redirectUrl == '/member/show/1'
        assert count == 1

        response.reset()
        pass = false
        controller.doCreate()
        assert view == "/shareCapitalAccount/create"

        response.reset()
        params.balance = '-100.00'
        controller.doCreate()
        assert view == "/shareCapitalAccount/create"

        params.id = '42'
        controller.doCreate()
        assert response.redirectUrl == '/error'
    }

    void testDeposit() {
        generateMember()

        params.id = '1'

        // Mocking
        controller.shareCapitalAccountService = [
            getMemberAccount: { m -> true }
        ]

        controller.deposit()
        assert view == "/shareCapitalAccount/deposit"

        params.id = '42'
        controller.deposit()
        assert response.redirectUrl == '/error'
    }

    void testDoDeposit() {
        generateMember()

        params.id = '1'
        params.paymentDate = 'date.struct'
        params.paymentDate_day = '10'
        params.paymentDate_month = '1'
        params.paymentDate_year = '2012'
        params.amount = '300.00'

        // Mocking
        def inDepositCount = 0
        controller.shareCapitalAccountService = [
            getMemberAccount: { m -> true },
            deposit: { a, b, d -> inDepositCount++ }
        ]

        controller.doDeposit()
        assert response.redirectUrl == '/member/show/1'

        response.reset()

        controller.shareCapitalAccountService = [
            getMemberAccount: { m -> true },
            deposit: { a, b, d -> false }
        ]

        controller.doDeposit()
        assert view == '/shareCapitalAccount/deposit'

        response.reset()

        params.id = '42'
        controller.doDeposit()
        assert response.redirectUrl == '/error'
    }
}
