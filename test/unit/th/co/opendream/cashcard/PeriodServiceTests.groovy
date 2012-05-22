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

    void testCalculateFormulaOneLeapYear() {
        /*
         *********************************************************************
         * Loan Amount : 2000.00, Interest Rate : 24% /year
         * ================================================
         * First month :
         * -- balance : 2000.00
         * -- interest : 40.655738 (groovy: 40.655737)
         * -- effected interest : 30.491803 (groovy: 30.491805)
         * -- fee : 10.163935 (groovy: 10.163932)
         * ================================================
         * Next month :
         * -- balance : 1334.655737
         * -- interest : 26.255523 (groovy: 26.255522)
         * -- effected interest : 19.691642 (groovy: 19.691643)
         * -- fee : 6.563881, (groovy: 6.563879)
         *********************************************************************
        */

        setUpPeriod()

        def contract = Contract.get(1)
        contract.approvalStatus = true
        contract.approvalDate = new Date().parse("yyyy-MM-dd", "2012-03-01")
        contract.loanReceiveStatus = true
        contract.payloanDate = contract.approvalDate
        contract.save()

        def p1 = Period.get(1)
        p1.dueDate = new Date().parse("yyyy-MM-dd", "2012-04-01")
        p1.save()

        /**
         * Test first period.
         */
        def result = service.calculateInterestFormulaOne(p1, p1.dueDate)
        assert result.actualInterest == 40.655737
        assert result.effectedInterest == 30.491805
        assert result.fee == 10.163932

        contract.loanBalance = 1334.655737
        contract.save()

        p1.payoffStatus = true
        p1.payoffDate = p1.dueDate
        p1.save()

        def p2 = Period.get(2)
        p2.dueDate = new Date().parse("yyyy-MM-dd", "2012-05-01")
        p2.save()

        def p3 = Period.get(3)
        p3.dueDate = new Date().parse("yyyy-MM-dd", "2012-06-01")
        p3.save()

        PeriodService.metaClass.getCurrentPeriod = { it -> p2 }

        /**
         * Test second period.
         */
        result = service.calculateInterestFormulaOne(p2, p2.dueDate)
        assert result.actualInterest == 26.255522
        assert result.effectedInterest == 19.691643
        assert result.fee == 6.563879
    }

    void testCalculateFormulaOneNotLeapYear() {
        /*
         *********************************************************************
         * Loan Amount : 2000.00, Interest Rate : 24% /year
         * ================================================
         * First month :
         * -- balance : 2000.00
         * -- interest : 40.767123 (groovy: 40.767120)
         * -- effected interest : 30.575342 (groovy: 30.575343)
         * -- fee : 10.191781 (groovy: 10.191777)
         * ================================================
         * Next month :
         * -- balance : 1334.767123
         * -- interest : 26.329653 (groovy: 26.329651)
         * -- effected interest : 19.747240 (groovy: 19.747240)
         * -- fee : 6.582413, (groovy: 6.582411)
         *********************************************************************
        */

        setUpPeriod()

        def contract = Contract.get(1)
        contract.approvalStatus = true
        contract.approvalDate = new Date().parse("yyyy-MM-dd", "2011-03-01")
        contract.loanReceiveStatus = true
        contract.payloanDate = contract.approvalDate
        contract.save()

        def p1 = Period.get(1)
        p1.dueDate = new Date().parse("yyyy-MM-dd", "2011-04-01")
        p1.save()

        /**
         * Test first period.
         */
        def result = service.calculateInterestFormulaOne(p1, p1.dueDate)
        assert result.actualInterest == 40.767120
        assert result.effectedInterest == 30.575343
        assert result.fee == 10.191777

        contract.loanBalance = 1334.767123
        contract.save()

        p1.payoffStatus = true
        p1.payoffDate = p1.dueDate
        p1.save()

        def p2 = Period.get(2)
        p2.dueDate = new Date().parse("yyyy-MM-dd", "2011-05-01")
        p2.save()

        def p3 = Period.get(3)
        p3.dueDate = new Date().parse("yyyy-MM-dd", "2011-06-01")
        p3.save()

        PeriodService.metaClass.getCurrentPeriod = { it -> p2 }

        /**
         * Test second period.
         */
        result = service.calculateInterestFormulaOne(p2, p2.dueDate)
        assert result.actualInterest == 26.329651
        assert result.effectedInterest == 19.747240
        assert result.fee == 6.582411
    }

    void testPeriodPayoff() {
        setUpPeriod()

        def contract = Contract.get(1)
        contract.approvalStatus = true
        contract.approvalDate = new Date().parse("yyyy-MM-dd", "2012-03-01")
        contract.loanReceiveStatus = true
        contract.payloanDate = contract.approvalDate
        contract.save()

        def p1 = Period.get(1)
        p1.dueDate = new Date().parse("yyyy-MM-dd", "2012-04-01")
        p1.save()

        service.metaClass.calculateInterestFormulaOne = { p, d ->
            [actualInterest: 40.655737, effectedInterest: 30.491805, fee: 10.163932]
        }

        service.periodPayoff(p1, 706.00, 0.00, false, p1.dueDate)
        assert ReceiveTransaction.list().size() == 1

        p1 = Period.get(1)
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
        assert receiveTx.isShareCapital == false

        contract = Contract.get(1)
        assert contract.loanBalance == 1334.655737
    }
}
