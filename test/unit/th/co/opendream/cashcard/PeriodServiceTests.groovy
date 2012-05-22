package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

import groovy.time.TimeCategory

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PeriodService)
@Mock([Member, Contract, Period, LoanType])
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

    void testCalculateFormulaOne() {
        setUpPeriod()

        def p1 = Period.get(1)
        p1.dueDate = new Date().parse("yyyy-MM-dd", "2012-04-01")
        p1.payoffStatus = true
        p1.payoffDate = p1.dueDate
        p1.save()

        def p2 = Period.get(2)
        p2.dueDate = new Date().parse("yyyy-MM-dd", "2012-05-01")
        p2.save()

        def p3 = Period.get(3)
        p3.dueDate = new Date().parse("yyyy-MM-dd", "2012-06-01")
        p3.save()

        PeriodService.metaClass.getCurrentPeriod = { contract -> p2 }

        /*
         *********************************************************************
         * Loan Amount : 2000.00, Interest Rate : 24% /year
         * ================================================
         * First month :
         * -- balance : 2000.00
         * -- interest : 39.344262
         * -- effected interest : 29.508197
         * -- fee : 9.836065
         * ================================================
         * Next month :
         * -- balance : 1333.344262
         * -- interest : 26.229723
         * -- effected interest : 19.672292, fix grails bug to 19.672293
         * -- fee : 6.557431, fix grails bug to 6.557430
         *********************************************************************
        */

        def contract = Contract.get(1)
        contract.loanBalance = 1333.344262
        contract.save()

        def result = service.calculateInterestFormulaOne(p2, p2.dueDate)
        assert result.actualInterest == 26.229723
        assert result.effectedInterest == 19.672293
        assert result.fee == 6.557430
    }
}
