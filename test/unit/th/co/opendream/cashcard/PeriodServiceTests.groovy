package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

import groovy.time.TimeCategory

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PeriodService)
@Mock([Member, Contract, Period, LoanType, ReceiveTransaction])
class PeriodServiceTests {

    void setUpPeriod() {
        def loanType = new LoanType(name: 'Common')
        loanType.save()

        def member = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
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
    }

    def setUpMockForPeriodPayoff() {
        def loanType = new LoanType(name: 'Common')
        loanType.save()

        def member = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
        member.utilService = [
            check_id_card: { id -> true }
        ]
        member.save()

        def contract = new Contract(
            code: "ก.55-1000-10",
            member: member,
            loanType: loanType,
            loanAmount: 2000.00,
            interestRate: 24.00,
            loanBalance: 2000.00,
            approvalStatus: true,
            approvalDate: new Date().parse("yyyy-MM-dd", "2012-03-01"),
            loanReceiveStatus: true,
            payloanDate: new Date().parse("yyyy-MM-dd", "2012-03-01"),
            guarantor1: "Keng",
            guarantor2: "Neung",
            numberOfPeriod: 3
        )
        contract.save()

        mockDomain(Period, [
            [contract: contract, amount: 706.00, no: 1,
             dueDate: Date.parse("yyyy-MM-dd", "2012-04-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 706.00, no: 2,
             dueDate: Date.parse("yyyy-MM-dd", "2012-05-01"), 
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 708.00, no: 3,
             dueDate: Date.parse("yyyy-MM-dd", "2012-06-01"),
             status: true, payoffStatus: false
            ]
        ])

        contract
    }

    void testGeneratePeriod() {
        
        def periodList = service.generatePeriod(1000.00, 3)

        assert periodList[0].amount == 333
        assert periodList[1].amount == 333
        assert periodList[2].amount == 334
        assert periodList.size() == 3

        periodList = service.generatePeriod(1000.00, 6)

        assert periodList[0].amount == 166
        assert periodList[1].amount == 166
        assert periodList[2].amount == 166
        assert periodList[3].amount == 166
        assert periodList[4].amount == 166
        assert periodList[5].amount == 170
        assert periodList.size() == 6
    }

    void testGetCurrentPeriod() {
        setUpPeriod()

        def contract = Contract.get(1)

        assert service.getCurrentPeriod(contract) == Period.get(2)

        def p1 = Period.get(1)
        p1.dueDate = null
        p1.save()

        def p2 = Period.get(2)
        p2.dueDate = null
        p2.save()

        def p3 = Period.get(3)
        p3.dueDate = null
        p3.save()

        assert service.getCurrentPeriod(contract) == null
    }

    void testPeriodPayoff() {
        def contract = setUpMockForPeriodPayoff()
        println Period.list()
        def p1 = Period.get(1)
        def p2 = Period.get(2)
        def p3 = Period.get(3)

        service.processorService = [ process: { p, d ->
            if (p.id == 1) {
                [actualInterest: 40.655737, effectedInterest: 30.491805, fee: 10.163932]
            }
            else if (p.id == 2) {
                [actualInterest: 26.255522, effectedInterest: 19.691643, fee: 6.563879]
            }
            else if (p.id == 3) {
                [actualInterest: 13.312950, effectedInterest: 9.984713, fee: 3.328237]
            }
        } ] as ProcessorService

        /************************** Verify money ******************************/
        service.periodPayoff(p1, 706.00, 0.00, false, p1.dueDate)
        assert ReceiveTransaction.list().size() == 1

        p1 = Period.get(1) // Reload data
        assert p1.payoffStatus == true
        assert p1.payoffDate == p1.dueDate

        def receiveTx = ReceiveTransaction.get(1)
        assert receiveTx.amount == 706.00
        assert receiveTx.balanceForward == 2000.00
        assert receiveTx.balancePaid == 665.344263
        assert receiveTx.interestRate == 24.00
        assert receiveTx.interestPaid == 30.491805
        assert receiveTx.fee == 10.163932
        assert receiveTx.fine == 0.00
        assert receiveTx.differential == 0.00
        assert receiveTx.isShareCapital == false

        contract = Contract.get(1)
        assert contract.loanBalance == 1334.655737
        /*********************** /END Verify money ****************************/

        /************************** Verify money ******************************/
        service.periodPayoff(p2, 706.00, 0.00, false, p2.dueDate)
        assert ReceiveTransaction.list().size() == 2

        p2 = Period.get(2) // Reload data
        assert p2.payoffStatus == true
        assert p2.payoffDate == p2.dueDate

        receiveTx = ReceiveTransaction.get(2)
        assert receiveTx.amount == 706.00
        assert receiveTx.balanceForward == 1334.655737
        assert receiveTx.balancePaid == 679.744478
        assert receiveTx.interestRate == 24.00
        assert receiveTx.interestPaid == 19.691643
        assert receiveTx.fee == 6.563879
        assert receiveTx.fine == 0.00
        assert receiveTx.differential == 0.00
        assert receiveTx.isShareCapital == false

        contract = Contract.get(1)
        assert contract.loanBalance == 654.911259
        /*********************** /END Verify money ****************************/

        /************************** Verify money ******************************/
        service.periodPayoff(p3, 708.00, 0.00, false, p3.dueDate)
        assert ReceiveTransaction.list().size() == 3

        p3 = Period.get(3) // Reload data
        assert p3.payoffStatus == true
        assert p3.payoffDate == p3.dueDate

        receiveTx = ReceiveTransaction.get(3)
        assert receiveTx.amount == 708.00
        assert receiveTx.balanceForward == 654.911259
        assert receiveTx.balancePaid == 694.687050
        assert receiveTx.interestRate == 24.00
        assert receiveTx.interestPaid == 9.984713
        assert receiveTx.fee == 3.328237
        assert receiveTx.fine == 0.00
        assert receiveTx.differential == 39.775791
        assert receiveTx.isShareCapital == false

        contract = Contract.get(1)
        assert contract.loanBalance == 0.00
        /*********************** /END Verify money ****************************/
    }
}
