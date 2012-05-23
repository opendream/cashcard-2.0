package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ProcessorService)
@Mock([LoanType, Contract, Period, Member])
class ProcessorServiceTests {

    @Before
    void setUp() {
        new LoanType(name: 'A', processor: 'Effective').save()
        new LoanType(name: 'B', processor: 'Hybrid').save()
        new LoanType(name: 'C', processor: 'Flat').save()
    }

    void setUpPeriod() {
        def loanType = new LoanType(name: 'Common', processor: 'Effective')
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
        def loanType = new LoanType(name: 'Common', processor: "Effective")
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
        def result = service.process(p1, p1.dueDate)
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
        result = service.process(p2, p2.dueDate)
        assert result.actualInterest == 26.255522
        assert result.effectedInterest == 19.691643
        assert result.fee == 6.563879
    }

    void testCalculateEffectiveMethodNotLeapYear() {
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
        def result = service.process(p1, p1.dueDate)
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
        result = service.process(p2, p2.dueDate)
        assert result.actualInterest == 26.329651
        assert result.effectedInterest == 19.747240
        assert result.fee == 6.582411
    }
}
