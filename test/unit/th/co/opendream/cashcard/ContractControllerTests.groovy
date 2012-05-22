package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*
import groovy.mock.interceptor.MockFor

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ContractController)
@Mock([Member, LoanType, Contract, PeriodService, Period, Transaction])
class ContractControllerTests {

	def utilService

    @Before
    void setUp() {
    	def commonLoan = new LoanType(name: "Common").save()

        def m1 = new Member(identificationNumber: "1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
        def m2 = new Member(identificationNumber: "1119900100015", firstname: "Noomz", lastname: "Siriwat", telNo: "111111111", gender: "MALE", address: "Opendream2")

        utilService = [ check_id_card: { id -> true }, isPayable: { Contract c -> true } ] as UtilService

        controller.utilService = utilService

        m1.utilService = utilService
        m2.utilService = utilService

        m1.save()
        m2.save()
    }

    void testCreate() {
        controller.periodService = [
            generatePeriod: { a, period ->
                [
                    [amount: 333, no: 1, contract: null] as Period,
                    [amount: 333, no: 2, contract: null] as Period
                ]
            }
        ] as PeriodService


        Period.metaClass.save = {
            delegate.id = 1;
            delegate
        }

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

    void testShow() {
        params.id = '1'

        def mock = new MockFor(Contract.class)
        mock.demand.get() { [id: 1] }
        mock.use {
            controller.show()
            assert view == '/contract/show'
        }

        mock.demand.get() { null }
        mock.use {
            params.id = '42'

            controller.show()
            assert response.redirectedUrl == '/error'
        }
    }

    void testApprove() {
        params.id = 1
        def member = Member.get(params.id)
        Contract.metaClass.static.get = { Serializable gid ->
            [ id: gid, member: member ] as Contract
        }

        controller.approve()

        assert model.contractInstance.id == Contract.get(1).id
        assert view == '/contract/approve'
    }

    void testApproveOk() {
        def generateContractMethods= { id ->
            { ->
                GroovySystem.getMetaClassRegistry().removeMetaClass(Contract)
                // FROM
                //   http://blogs.bytecode.com.au/glen/2008/03/12/mockfor-march-unit-testing-grails-controllers.html
                //
                //def remove = GroovySystem.metaClassRegistry.&removeMetaClass
                //remove Contract
                def member = Member.get(id)
                Contract.metaClass.save = { ->
                    delegate.member = member
                    delegate.id = 1
                    delegate.approvalDate = new Date()
                    delegate
                }

                Contract.metaClass.static.get = { Serializable gid ->
                    [ id: gid, member: member ] as Contract
                }
            }
        }

        params.id = 1
        generateContractMethods(params.id).call()
        controller.doApprove()
        assert response.redirectedUrl == "/member/show/1"

        response.reset()

        params.id = 2
        generateContractMethods(params.id).call()
        controller.doApprove()
        assert response.redirectedUrl == "/member/show/2"
    }

    void testDoApproveIsNotOk() {
        Contract.metaClass.save = { -> null }
        Contract.metaClass.static.get = { Serializable gid ->
            [ id: gid, member: Member.get(gid) ] as Contract
        }
        params.id = 1
        controller.doApprove()
        assert model != null
        assert view == '/contract/approve'
    }

    void testPreparePeriod() {
        params.amount = '1000.00'
        params.nop = '3'

        def mock = mockFor(PeriodService)
        mock.demand.generatePeriod {
            [
                [amount: 333, no: 1]
            ]
        }

        controller.periodService = mock.createMock()
        controller.preparePeriod()
        assert view == '/contract/preparePeriod'

        params.amount = '0'
        params.nop = '0'

        controller.preparePeriod()
        assert response.redirectedUrl == '/error'
    }

    void testdoApproveWithoutId() {
        controller.doApprove()
        assert response.redirectedUrl == '/error'
    }

    void testApproveWithoutId() {
        controller.approve()
        assert response.redirectedUrl == '/error'
    }

    void testPayLoan() {
        params.id = 1
        def member = Member.get(params.id)
        Contract.metaClass.static.get = { Serializable gid ->
            [ id: gid, member: member,  loanAmount: 300] as Contract
        }

        controller.payloan()

        assert model.contractInstance.loanAmount == 300
        assert model.contractInstance.id == Contract.get(1).id
        assert view == '/contract/payloan'
    }


    void testPayLoanWithoutId() {
        controller.payloan()
        assert response.redirectedUrl == '/error'
    }

    void testDoPayloanWithoutDateOrId() {
        params.id = 1
        params.payloanDate = ''
        controller.doPayloan()
        assert response.redirectedUrl == '/error'

        response.reset()

        params.id = null
        controller.doPayloan()
        assert response.redirectedUrl == '/error'
    }


    void testDoPayloanOK() {
        params.id = 1
        params.payloanDate = new Date()


        def member = Member.get(params.id)
        Contract.metaClass.static.get = { Serializable gid ->
            [ id: gid, member: member,  loanAmount: 300, payloanDate: null] as Contract
        }

        Contract.metaClass.save = { ->
            delegate.member = member
            delegate.id = 1
            delegate.payloanDate = new Date()
            delegate
        }

        controller.doPayloan()
        assert response.redirectedUrl == "/member/show/${member.id}"
    }

    void testPayoff() {
        def contract = [id: 1, approvalStatus: true, loanReceiveStatus: true]
        Period.metaClass.static.get = { Serializable id ->
            id == '1' ? [id: id, contract: contract] : null
        }

        params.id = '1'
        controller.payoff()
        assert view == '/contract/payoff'

        contract.approvalStatus = false
        controller.payoff()
        assert response.redirectUrl == '/error'

        response.reset()

        contract.approvalStatus = true
        contract.loanReceiveStatus = false
        controller.payoff()
        assert response.redirectUrl == '/error'

        response.reset()

        params.id = '42'
        controller.payoff()
        assert response.redirectUrl == '/error'
    }

    void testDoPayoff() {
        //params.id = '1'
        //params.amount = '300.00'
        //params.fine = ''
        //params.isShareCapital = 'true'

        //controller.doPayoff()
        //assert response.redirectUrl == '/member/show/1'
    }

}
