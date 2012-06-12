package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(InterestProcessorService)
@Mock([LoanType, Contract, Period, Member, ReceiveTransaction])
class InterestProcessorServiceTests {

    @Before
    void setUp() {
        new LoanType(name: 'A', processor: 'Effective').save()
        new LoanType(name: 'B', processor: 'Hybrid').save()
        new LoanType(name: 'C', processor: 'Flat').save()
    }

    void setUpPeriod(type='Effective') {
        def loanType = new LoanType(name: 'Common', processor: type)
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
            processor: type,
            loanAmount: 2000.00,
            interestRate: 24.00,
            cooperativeShare: 0.75, // For Commission
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

    void setUpPeriodFlat() {
        def loanType = new LoanType(name: 'Common', processor: 'Flat')
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
            processor: "Flat",
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
            processor: loanType.processor,
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

    /******************************************
     * Effective
     ******************************************/

    void testCalculateEffectiveMethodLeapYear() {
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

        setUpPeriod('Effective')

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
        assert result.actualInterest == 40.655738
        assert result.effectedInterest == 30.491803
        assert result.fee == 10.163935

        contract.loanBalance = 1334.655738
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

        service.metaClass.getLastPayment = { c ->
            [ paymentDate: p1.dueDate ]
        }

        /**
         * Test second period.
         */
        result = service.process(p2, p2.dueDate)
        assert result.actualInterest == 26.255523
        assert result.effectedInterest == 19.691642
        assert result.fee == 6.563881

    }

    void testCalculateEffectiveMethodLeapYearWithInterestLessThanMaxInterest() {
        /*
         *********************************************************************
         * Loan Amount : 2000.00, Interest Rate : 12% /year
         * ================================================
         * First month :
         * -- balance : 2000.00
         * -- interest : 20.327869
         * -- effected interest : 20.327869
         * -- fee : 0.00
         * ================================================
         * Next month :
         * -- balance : 1334.327869
         * -- interest : 13.124536
         * -- effected interest : 13.124536
         * -- fee : 0.00
         *********************************************************************
        */

        setUpPeriod('Effective')

        def contract = Contract.get(1)
        contract.interestRate = 12.00
        contract.maxInterestRate = 18.00
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
        assert result.actualInterest == 20.327869
        assert result.effectedInterest == 20.327869
        assert result.fee == 0.00

        contract.loanBalance = 1334.327869
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

        service.metaClass.getLastPayment = { c ->
            [ paymentDate: p1.dueDate ]
        }

        /**
         * Test second period.
         */
        result = service.process(p2, p2.dueDate)
        assert result.actualInterest == 13.124536
        assert result.effectedInterest == 13.124536
        assert result.fee == 0.00

    }

    def saveRecieveTx(period, amount, interest, fee, date) {
        def receiveTx = new ReceiveTransaction(
            period: period,
            amount: amount,
            balanceForward: 2000.00,
            balancePaid: amount,
            interestRate: 12.00,
            interestPaid: interest,
            fee: 0.00,
            fine: 0.00,
            differential: 0.00,
            isShareCapital: false,
            paymentDate: date,
            sign: +1,
            status: true
        )
        receiveTx.save()

        receiveTx
    }

    void testCalculateEffectiveMethodLeapYearWithPartialPayment() {
        setUpPeriod('Effective')

        def contract = Contract.get(1)
        contract.interestRate = 12.00
        contract.maxInterestRate = 18.00
        contract.approvalStatus = true
        contract.approvalDate = new Date().parse("yyyy-MM-dd", "2012-03-01")
        contract.loanReceiveStatus = true
        contract.payloanDate = contract.approvalDate
        contract.save()

        def p1 = Period.get(1)
        p1.dueDate = new Date().parse("yyyy-MM-dd", "2012-04-01")
        p1.save()

        /* First paid */
        def paymentDate = new Date().parse("yyyy-MM-dd", "2012-03-11")
        def result = service.effective(p1, paymentDate)
        assert result.actualInterest == 6.557377
        assert result.effectedInterest == 6.557377
        assert result.fee == 0.00

        contract.loanBalance = 1300.000000
        contract.save()

        p1.payoffStatus = true
        p1.payoffDate = p1.dueDate
        p1.save()

        PeriodService.metaClass.getCurrentPeriod = { it -> p1 }

        service.metaClass.getLastPayment = { c ->
            [ paymentDate: Date.parse("yyyy-MM-dd", "2012-03-11") ]
        }

        /* Second */
        paymentDate = new Date().parse("yyyy-MM-dd", "2012-03-16")
        result = service.process(p1, paymentDate)
        assert result.actualInterest == 2.131148
        assert result.effectedInterest == 2.131148
        assert result.fee == 0.00
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

        setUpPeriod('Effective')

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
        assert result.actualInterest == 40.767123
        assert result.effectedInterest == 30.575342
        assert result.fee == 10.191781

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

        service.metaClass.getLastPayment = { c ->
            [ paymentDate: p1.dueDate ]
        }

        /**
         * Test second period.
         */
        result = service.effective(p2, p2.dueDate)
        assert result.actualInterest == 26.329653
        assert result.effectedInterest == 19.74724
        assert result.fee == 6.582413
    }

    void testCalculateEffectiveMethodZeroBalance() {
        setUpPeriod()

        def contract = Contract.get(1)
        contract.approvalDate = Date.parse("yyyy-MM-dd", "2012-01-01")
        contract.loanBalance = 0
        contract.save()

        def lastPeriod = Period.get(3)
        lastPeriod.payoffStatus = true
        lastPeriod.payoffDate = Date.parse("yyyy-MM-dd", "2012-01-01")
        lastPeriod.save()

        def period = new Period(
            amount: 1000.00,
            dueDate: Date.parse("yyyy-MM-dd", "2013-01-01"),
            no: 4,
            status: true,
            payoffStatus: false,
            payoffDate: Date.parse("yyyy-MM-dd", "2013-01-01"),
            contract: contract
        )
        period.save()

        def result = service.effective(period, period.dueDate)
        assert result.actualInterest == 0.00
        assert result.effectedInterest == 0.00
        assert result.fee == 0.00
    }

    /******************************************
     *  Flat
     ******************************************/
    void testCalculateFlatMethodLeapYear() {
        def getDate = { str -> Date.parse("yyyy-MM-dd", str) }

        setUpPeriodFlat()

        def contract = Contract.get(1)
        contract.approvalStatus = true
        contract.approvalDate = getDate("2012-03-01")
        contract.loanReceiveStatus = true
        contract.payloanDate = contract.approvalDate
        contract.loanAmount = 60000.00
        contract.loanBalance = 60000.00
        contract.advancedInterestKeep = 14400.00
        contract.advancedInterestBalance = 14400.00
        contract.interestRate = 12.00
        contract.maxInterestRate = 18.00
        contract.numberOfPeriod = 24
        contract.save()

        contract = Contract.get(1)
        assert contract.numberOfPeriod == 24

        def sample_period = [
            // no, amount, dueDate
            [1,    2500, "2012-04-1"], [2,    2500, "2012-05-1"],
            [3,    2500, "2012-06-1"], [4,    2500, "2012-07-1"],
            [5,    2500, "2012-08-1"], [6,    2500, "2012-09-1"],
            [7,    2500, "2012-10-1"], [8,    2500, "2012-11-1"],
            [9,    2500, "2012-12-1"], [10,   2500, "2013-01-1"],
            [11,   2500, "2013-02-1"], [12,   2500, "2013-03-1"],
            [13,   2500, "2013-04-1"], [14,   2500, "2013-05-1"],
            [15,   2500, "2013-06-1"], [16,   2500, "2013-07-1"],
            [17,   2500, "2013-08-1"], [18,   2500, "2013-09-1"],
            [19,   2500, "2013-10-1"], [20,   2500, "2013-11-1"],
            [21,   2500, "2013-12-1"], [22,   2500, "2014-01-1"],
            [23,   2500, "2014-02-1"], [24,   2500, "2014-03-1"]
        ]

        def expect = [
            // actual, effective, fee
            [609.836066,  609.836066,  0],
            [565.57377,   565.57377,   0],
            [559.016393,  559.016393,  0],
            [516.393443,  516.393443,  0],
            [508.196721,  508.196721,  0],
            [482.786885,  482.786885,  0],
            [442.622951,  442.622951,  0],
            [431.967213,  431.967213,  0],
            [393.442623,  393.442623,  0],
            [382.191781,  382.191781,  0],
            [356.712329,  356.712329,  0],
            [299.178082,  299.178082,  0],
            [305.753425,  305.753425,  0],
            [271.232877,  271.232877,  0],
            [254.794521,  254.794521,  0],
            [221.917808,  221.917808,  0],
            [203.835616,  203.835616,  0],
            [178.356164,  178.356164,  0],
            [147.945205,  147.945205,  0],
            [127.39726,   127.39726,   0],
            [98.630137,   98.630137,   0],
            [76.438356,   76.438356,   0],
            [50.958904,   50.958904,   0],
            [23.013699,   23.013699,   0]
        ]

        // prepare periods
        def periodList = sample_period.collect {
            def p = new Period(
                contract: contract, no: it[0], amount: it[1],
                dueDate: getDate(it[2]), status: true, payoffStatus: false
            )
            p.save()
            return p
        }

        def paymentDate = contract.approvalDate
        service.metaClass.getLastPayment = { c ->
            [ paymentDate: paymentDate ]
        }

        def amountPerMonth = contract.loanAmount / contract.numberOfPeriod
        periodList.each { period ->
            print "period : ${period.no}"
            print " ,date: ${period.dueDate}, balance : ${contract.loanBalance}"

            def result = service.flat(period, period.dueDate),
                wanted = expect[period.no - 1]

            assert result.actualInterest == wanted[0]
            assert result.effectedInterest == wanted[1]
            assert result.fee == wanted[2]

            period.payoffStatus = true
            period.payoffDate = period.dueDate
            period.save()

            contract.loanBalance -= amountPerMonth
            contract.save()

            println " ===> pass"

            paymentDate = period.dueDate
        }
    }

    void testCalculateFlatMethodLeapYearWithPartialPayment() {
        def getDate = { str -> Date.parse("yyyy-MM-dd", str) }

        setUpPeriodFlat()

        def contract = Contract.get(1)
        contract.approvalStatus = true
        contract.approvalDate = getDate("2012-03-01")
        contract.loanReceiveStatus = true
        contract.payloanDate = contract.approvalDate
        contract.loanAmount = 60000.00
        contract.loanBalance = 60000.00
        contract.advancedInterestKeep = 14400.00
        contract.advancedInterestBalance = 14400.00
        contract.interestRate = 12.00
        contract.maxInterestRate = 18.00
        contract.numberOfPeriod = 24
        contract.save()

        def p1 = new Period()
        p1.amount = 2500.00
        p1.contract = contract
        p1.no = 1
        p1.status = true
        p1.dueDate = new Date().parse("yyyy-MM-dd", "2012-04-01")
        p1.save()
        println p1.errors

        /* First paid */
        def paymentDate = new Date().parse("yyyy-MM-dd", "2012-03-11")
        def result = service.effective(p1, paymentDate)
        assert result.actualInterest == 196.721311
        assert result.effectedInterest == 196.721311
        assert result.fee == 0.00

        contract.loanBalance = 59000.000000
        contract.save()

        p1.payoffStatus = true
        p1.payoffDate = p1.dueDate
        p1.save()

        PeriodService.metaClass.getCurrentPeriod = { it -> p1 }

        service.metaClass.getLastPayment = { c ->
            [ paymentDate: Date.parse("yyyy-MM-dd", "2012-03-11") ]
        }

        /* Second */
        paymentDate = new Date().parse("yyyy-MM-dd", "2012-03-16")
        result = service.process(p1, paymentDate)
        assert result.actualInterest == 96.721311
        assert result.effectedInterest == 96.721311
        assert result.fee == 0.00
    }

    /******************************************
     * Commission
     ******************************************/

    void testCalculateCommissionMethodLeapYear() {
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

        setUpPeriod('Commission')

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
        assert result.actualInterest == 40.655738
        assert result.effectedInterest == 30.491803
        assert result.cooperativeInterest == 22.868852
        assert result.fee == 10.163935

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

        service.metaClass.getLastPayment = { c ->
            [ paymentDate: p1.dueDate ]
        }

        /**
         * Test second period.
         */
        result = service.process(p2, p2.dueDate)
        assert result.actualInterest == 26.255523
        assert result.effectedInterest == 19.691642
        assert result.cooperativeInterest == 14.768732
        assert result.fee == 6.563881
    }

    void testCalculateCommissionMethodLeapYearWithPartialPayment() {
        setUpPeriod('Commission')

        def contract = Contract.get(1)
        contract.interestRate = 12.00
        contract.maxInterestRate = 18.00
        contract.approvalStatus = true
        contract.approvalDate = new Date().parse("yyyy-MM-dd", "2012-03-01")
        contract.loanReceiveStatus = true
        contract.payloanDate = contract.approvalDate
        contract.save()

        def p1 = Period.get(1)
        p1.dueDate = new Date().parse("yyyy-MM-dd", "2012-04-01")
        p1.save()

        /* First paid */
        def paymentDate = new Date().parse("yyyy-MM-dd", "2012-03-11")
        def result = service.commission(p1, paymentDate)
        assert result.actualInterest == 6.557377
        assert result.effectedInterest == 6.557377
        assert result.cooperativeInterest == 4.918033
        assert result.fee == 0.00

        contract.loanBalance = 1300.000000
        contract.save()

        p1.payoffStatus = true
        p1.payoffDate = p1.dueDate
        p1.save()

        PeriodService.metaClass.getCurrentPeriod = { it -> p1 }

        service.metaClass.getLastPayment = { c ->
            [ paymentDate: Date.parse("yyyy-MM-dd", "2012-03-11") ]
        }

        /* Second */
        paymentDate = new Date().parse("yyyy-MM-dd", "2012-03-16")
        result = service.process(p1, paymentDate)
        assert result.actualInterest == 2.131148
        assert result.effectedInterest == 2.131148
        assert result.cooperativeInterest == 1.598361
        assert result.fee == 0.00
    }

    void testCalculateCommissionMethodNotLeapYear() {
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

        setUpPeriod('Commission')

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
        assert result.actualInterest == 40.767123
        assert result.effectedInterest == 30.575342
        assert result.cooperativeInterest == 22.931507
        assert result.fee == 10.191781

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

        service.metaClass.getLastPayment = { c ->
            [ paymentDate: p1.dueDate ]
        }

        /**
         * Test second period.
         */
        result = service.process(p2, p2.dueDate)
        assert result.actualInterest == 26.329653
        assert result.effectedInterest == 19.74724
        assert result.cooperativeInterest == 14.81043
        assert result.fee == 6.582413
    }

    void testCalculateCommissionMethodZeroBalance() {
        setUpPeriod('Commission')

        def contract = Contract.get(1)
        contract.approvalDate = Date.parse("yyyy-MM-dd", "2012-01-01")
        contract.loanBalance = 0
        contract.save()

        def lastPeriod = Period.get(3)
        lastPeriod.payoffStatus = true
        lastPeriod.payoffDate = Date.parse("yyyy-MM-dd", "2012-01-01")
        lastPeriod.save()

        def period = new Period(
            amount: 1000.00,
            dueDate: Date.parse("yyyy-MM-dd", "2013-01-01"),
            no: 4,
            status: true,
            payoffStatus: false,
            payoffDate: Date.parse("yyyy-MM-dd", "2013-01-01"),
            contract: contract
        )
        period.save()

        def result = service.process(period, period.dueDate)
        assert result.actualInterest == 0.00
        assert result.effectedInterest == 0.00
        assert result.cooperativeInterest == 0.00
        assert result.fee == 0.00
    }

    void testCalculateInterestInMonthUnit() {
        assert 54 == service.calculateInterestInMonthUnit(1200.00, 18.00, 3)
    }

    void testGetEffectiveInterestRate() {
        def contract = [
            interestRate: 24.00,
            maxInterestRate: 18.00
        ]

        assert service.getEffectiveInterestRate(contract) == 18.00

        contract['interestRate'] = 12.00

        assert service.getEffectiveInterestRate(contract) == 12.00
    }

    void testGetLastPayment() {
        setUpPeriod()

        def contract = Contract.get(1)
        def period = Period.get(1)
        def receiveTx = saveRecieveTx(period, 100.00, 9.25, 0.00, new Date())

        assert service.getLastPayment(contract) == receiveTx
    }
}
