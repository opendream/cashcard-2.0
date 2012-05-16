package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ContractController)
@Mock([Member, LoanType])
class ContractControllerTests {

	def utilService

    @Before
    void setUp() {
    	def commonLoan = new LoanType(name: "Common").save()

        def m1 = new Member(identificationNumber: "1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
        def m2 = new Member(identificationNumber: "1234567891234", firstname: "Noomz", lastname: "Siriwat", telNo: "111111111", gender: "MALE", address: "Opendream2")

        utilService = [ check_id_card: { id -> true } ] as UtilService

        m1.utilService = utilService
        m2.utilService = utilService

        m1.save()
        m2.save()
    }

    void testCreate() {
		params.memberId = '1'
		params.loanType = '1'

		Contract.metaClass.save = { delegate.id = 1; delegate }
		controller.create()
		assert response.redirectedUrl == "/member/show/1"
    }

    void testCreateFail() {
    	params.memberId = '1'
    	params.loanType = '1'

    	Contract.metaClass.save = { null }
    	controller.create()
    	assert view == '/contract/sign'
    }

    void testCreateFailNoMember() {
    	params.memberId = '42'
    	params.loanType = '1'

    	controller.create()
    	assert response.redirectedUrl == '/error'
    }

    void testCreateFailNoLoanType() {
    	params.memberId = '1'
    	params.loanType = '42'

    	controller.create()
    	assert response.redirectedUrl == '/error'
    }

    void testSelectLoanType() {
        params.id = '1'
        controller.selectLoanType()

        assert view == '/contract/selectLoanType'

        // Non-existed member
        params.id = '42'
        controller.selectLoanType()

        assert response.redirectedUrl == '/error'
    }

    void testSign() {
        params.id = '1'
        params.type = '1'

        controller.sign()
        assert view == '/contract/sign'

        response.reset()

        // Non-existsed member
        params.id = '42'
        controller.sign()
        assert response.redirectedUrl == '/error'

        response.reset()

        // Non-existed loan type
        params.id = '1'
        params.type = '42'
        controller.sign()
        assert response.redirectedUrl == '/error'
    }
}
