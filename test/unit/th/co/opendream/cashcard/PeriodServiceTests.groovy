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

    void testBeforeInsert() {
        def period = [id: 1, outstanding: 0.00, amount: 234.56]

        period = service.beforeInsert(period)
        assert period.outstanding == 234.56
    }

    void testBeforeUpdateNoPartialPayoff() {
        def period = [id: 1, outstanding: 200.00, amount: 200.00, payoffStatus: false, partialPayoff: null]

        period.payAmount = 200.00
        period = service.beforeUpdate(period)

        assert period.partialPayoff == false
        assert period.payoffStatus == true
    }

    void testBeforeUpdatePartialPayoff() {
        def period = [id: 1, outstanding: 200.00, amount: 200.00, payoffStatus: false, partialPayoff: null]

        // 1st Partial
        period.payAmount = 100.00
        period = service.beforeUpdate(period)

        assert period.partialPayoff == true
        assert period.payoffStatus == false
        assert period.outstanding == 100.00


        // 2nd Partial
        period.payAmount = 50.00
        period = service.beforeUpdate(period)

        assert period.partialPayoff == true
        assert period.payoffStatus == false
        assert period.outstanding == 50.00

        // Last Partial
        period.payAmount = 50.00
        period = service.beforeUpdate(period)

        assert period.partialPayoff == false
        assert period.payoffStatus == true
        assert period.outstanding == 0.00
    }

}
