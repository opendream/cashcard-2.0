package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PrintoutService)
@Mock([Member, Contract, Period, LoanType, ReceiveTransaction])
class PrintoutServiceTests {

	@Before
	void setup() {
		def loanType = new LoanType(name: 'Common')
        loanType.save()

        def member = new Member(identificationNumber:"1159900100015", firstname:"Nat",
            lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream",
            creditUnionMemberNo: "1001-1-00001", creditUnionMemberId: 1
            )
        member.utilService = [
            check_id_card: { id -> true }
        ]
        member.save()

        def contract = new Contract(
            id: 1,
            code: "ก.55-1000-20",
            member: member,
            loanType: loanType,
            loanAmount: 2000.00,
            interestRate: 24.00,
            loanBalance: 2000.00,
            approvalStatus: false,
            loanReceiveStatus: false,
            guarantor1: "Keng",
            guarantor2: "Neung",
            numberOfPeriod: 3
        )
        contract.save()

        mockDomain(Period, [
            [id: 1, contract: contract, amount: 706.00, no: 1,
             dueDate: new Date().plus(10), status: true, payoffStatus: true],
            [id: 2, contract: contract, amount: 706.00, no: 2,
             dueDate: new Date().plus(20), status: true, payoffStatus: false],
            [id: 3, contract: contract, amount: 708.00, no: 3,
             dueDate: new Date().plus(30), status: true, payoffStatus: false]
        ])

        def p1 = Period.get(1) // Reload data
        p1.payoffStatus == true
        p1.payoffDate == p1.dueDate
        p1.save()

        def receiveTx = new ReceiveTransaction()
        receiveTx.id = 1
        receiveTx.amount = 706.00
        receiveTx.balanceForward = 2000.00
        receiveTx.balancePaid = 665.344263
        receiveTx.interestRate = 24.00
        receiveTx.interestPaid = 30.491805
        receiveTx.fee = 10.163932
        receiveTx.fine = 0.00
        receiveTx.differential = 0.00
        receiveTx.isShareCapital = false
        receiveTx.paymentDate = p1.dueDate
        receiveTx.period = p1
        receiveTx.sign = +1        
        receiveTx.save(flush:true)
	}

    void testPayoffPrintout() {
        def p1 = Period.get(1)
        assert p1.amount == 706.00

        def receiveTx = ReceiveTransaction.get(1)
        assert receiveTx.amount == 706.00

        def member = Member.findByIdentificationNumber('1159900100015')

        def printout = service.getPayoffPrintout(receiveTx.id)

        assert printout.identificationNumber == '1159900100015'
        assert printout.creditUnionMemberNo == '1001-1-00001'
        assert printout.member == "Nat Weerawan"
        assert printout.paymentDate == p1.dueDate
        assert printout.loanType == 'Common'
        assert printout.code == receiveTx.id // hove to change to code
        assert printout.periodAmount == p1.amount
        assert printout.amount == 706.00
        assert printout.periodNo ==  p1.id
        assert printout.periodBalance == 0.00
    }
    }
}
