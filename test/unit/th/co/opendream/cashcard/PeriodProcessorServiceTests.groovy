package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PeriodProcessorService)
@Mock([Member, Contract, Period, LoanType, ReceiveTransaction])
class PeriodProcessorServiceTests {

    /***********************************
     * Effective
     **********************************/
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
            loanAmount: 2000.00,
            cooperativeShare: 0.75,
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

    def setUpMockForPeriodPayoff(type='Effective') {
        def loanType = new LoanType(name: 'Common', processor: type)
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
            cooperativeShare: 0.75,
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

    /***********************************
     * Flat
     **********************************/

    def setUpMockForFlatPeriodPayoff(principle, interestRate, numberOfPeriod, keep, date) {
        def loanType = new LoanType(name: 'Common', processor: 'Flat')
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
            cooperativeShare: 0.75,
            loanAmount: principle,
            interestRate: interestRate,
            loanBalance: principle,
            advancedInterestKeep: keep,
            advancedInterestBalance: keep,
            approvalStatus: true,
            approvalDate: date,
            loanReceiveStatus: true,
            payloanDate: date,
            guarantor1: "Keng",
            guarantor2: "Neung",
            numberOfPeriod: numberOfPeriod
        )
        contract.save()

        contract
    }

    /***********************************
     * Effective
     **********************************/
    def setUpMockForPeriodPayoff_12Months(type='Effective') {
        def loanType = new LoanType(name: 'Common', processor: type)
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
            cooperativeShare: 0.75,
            loanAmount: 2000.00,
            interestRate: 24.00,
            loanBalance: 2000.00,
            approvalStatus: true,
            approvalDate: new Date().parse("yyyy-MM-dd", "2012-03-01"),
            loanReceiveStatus: true,
            payloanDate: new Date().parse("yyyy-MM-dd", "2012-03-01"),
            guarantor1: "Keng",
            guarantor2: "Neung",
            numberOfPeriod: 12
        )
        contract.save()

        mockDomain(Period, [
            [contract: contract, amount: 206.00, no: 1,
             dueDate: Date.parse("yyyy-MM-dd", "2012-04-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 206.00, no: 2,
             dueDate: Date.parse("yyyy-MM-dd", "2012-05-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 206.00, no: 3,
             dueDate: Date.parse("yyyy-MM-dd", "2012-06-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 206.00, no: 4,
             dueDate: Date.parse("yyyy-MM-dd", "2012-07-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 206.00, no: 5,
             dueDate: Date.parse("yyyy-MM-dd", "2012-08-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 206.00, no: 6,
             dueDate: Date.parse("yyyy-MM-dd", "2012-09-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 206.00, no: 7,
             dueDate: Date.parse("yyyy-MM-dd", "2012-10-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 206.00, no: 8,
             dueDate: Date.parse("yyyy-MM-dd", "2012-11-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 206.00, no: 9,
             dueDate: Date.parse("yyyy-MM-dd", "2012-12-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 206.00, no: 10,
             dueDate: Date.parse("yyyy-MM-dd", "2012-01-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 206.00, no: 11,
             dueDate: Date.parse("yyyy-MM-dd", "2012-02-01"),
             status: true, payoffStatus: false
            ],
            [contract: contract, amount: 214.00, no: 12,
             dueDate: Date.parse("yyyy-MM-dd", "2012-03-01"),
             status: true, payoffStatus: false
            ]
        ])

        contract
    }

    void testProcessPeriodPayoff() {
        def contract = setUpMockForPeriodPayoff('Effective')
        def p1 = Period.get(1)
        def p2 = Period.get(2)
        def p3 = Period.get(3)

        service.interestProcessorService = [ process: { p, d ->
            if (p.id == 1) {
                [actualInterest: 40.655737, effectedInterest: 30.491805, fee: 10.163932]
            }
            else if (p.id == 2) {
                [actualInterest: 26.255522, effectedInterest: 19.691643, fee: 6.563879]
            }
            else if (p.id == 3) {
                [actualInterest: 13.312950, effectedInterest: 9.984713, fee: 3.328237]
            }
        } ] as InterestProcessorService

        /************************** Verify money ******************************/
        service.process(p1, 706.00, 0.00, false, p1.dueDate)
        assert ReceiveTransaction.list().size() == 1

        p1 = Period.get(1) // Reload data
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
        assert receiveTx.paymentDate == p1.dueDate

        contract = Contract.get(1)
        assert contract.loanBalance == 1334.655737
        /*********************** /END Verify money ****************************/

        /************************** Verify money ******************************/
        service.process(p2, 706.00, 0.00, false, p2.dueDate)
        assert ReceiveTransaction.list().size() == 2

        p2 = Period.get(2) // Reload data
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
        assert receiveTx.paymentDate == p2.dueDate

        contract = Contract.get(1)
        assert contract.loanBalance == 654.911259
        /*********************** /END Verify money ****************************/

        /************************** Verify money ******************************/
        service.process(p3, 708.00, 0.00, false, p3.dueDate)
        assert ReceiveTransaction.list().size() == 3

        p3 = Period.get(3) // Reload data
        assert p3.payoffDate == p3.dueDate

        receiveTx = ReceiveTransaction.get(3)
        assert receiveTx.amount == 708.00
        assert receiveTx.balanceForward == 654.911259
        assert receiveTx.balancePaid == 654.911259
        assert receiveTx.interestRate == 24.00
        assert receiveTx.interestPaid == 9.984713
        assert receiveTx.fee == 3.328237
        assert receiveTx.fine == 0.00
        assert receiveTx.differential == 39.775791
        assert receiveTx.isShareCapital == false
        assert receiveTx.paymentDate == p3.dueDate

        contract = Contract.get(1)
        assert contract.loanBalance == 0.00
        /*********************** /END Verify money ****************************/
    }

    void testProcessPeriodPayoff_12Months() {
        def contract = setUpMockForPeriodPayoff_12Months()
        def period

        service.interestProcessorService = [ process: { p, d ->
            switch (p.id) {
                case 1:
                    [actualInterest: 40.655737, effectedInterest: 30.491805, fee: 10.163932]
                    break
                case 2:
                    [actualInterest: 36.091588, effectedInterest: 27.068692, fee: 9.022896]
                    break
                case 3:
                    [actualInterest: 33.840765, effectedInterest: 25.380575, fee: 8.460190]
                    break
                case 4:
                    [actualInterest: 29.362388, effectedInterest: 22.021792, fee: 7.340596]
                    break
                case 5:
                    [actualInterest: 26.750469, effectedInterest: 20.062852, fee: 6.687617]
                    break
                case 6:
                    [actualInterest: 23.106708, effectedInterest: 17.330032, fee: 5.776676]
                    break
                case 7:
                    [actualInterest: 18.763429, effectedInterest: 14.072573, fee: 4.690856]
                    break
                case 8:
                    [actualInterest: 15.582756, effectedInterest: 11.687068, fee: 3.895688]
                    break
                case 9:
                    [actualInterest: 11.334174, effectedInterest: 8.500631, fee: 2.833543]
                    break
                case 10:
                    [actualInterest: 7.754838, effectedInterest: 5.816129, fee: 1.938709]
                    break
                case 11:
                    [actualInterest: 3.724937, effectedInterest: 2.793703, fee: 0.931234]
                    break
                case 12:
                    [actualInterest: 0, effectedInterest: 0, fee: 0]
                    break

                default:
                    null
            }
        } ] as PeriodService

        def expect = [
            [30.491805, 10.163932,  165.344263, 1834.655737,    0],
            [27.068692, 9.022896,   169.908412, 1664.747325,    0],
            [25.380575, 8.460190,   172.159235, 1492.588090,    0],
            [22.021792, 7.340596,   176.637612, 1315.950478,    0],
            [20.062852, 6.687617,   179.249531, 1136.700947,    0],
            [17.330032, 5.776676,   182.893292, 953.807655,     0],
            [14.072573, 4.690856,   187.236571, 766.571084,     0],
            [11.687068, 3.895688,   190.417244, 576.153840,     0],
            [8.500631,  2.833543,   194.665826, 381.488014,     0],
            [5.816129,  1.938709,   198.245162, 183.242852,     0],
            [2.793703,  0.931234,   183.242852, 0,              19.032211],
            [0,         0,          0,          0,              214]
        ]

        /************************** Verify money ******************************/
        (1..12).each {
            print "period: ${it}"
            period = Period.get(it)
            service.process(period, period.amount, 0.00, false, period.dueDate)
            assert ReceiveTransaction.list().size() == period.id

            period = Period.get(period.id) // Reload data
            assert period.payoffDate == period.dueDate

            def receiveTx = ReceiveTransaction.get(period.id)
            def index = (period.id - 1) as Integer
            assert receiveTx.amount == period.amount
            assert receiveTx.balanceForward == (index ? expect[index - 1][3] : 2000.00)
            assert receiveTx.balancePaid == expect[index][2]
            assert receiveTx.interestRate == 24.00
            assert receiveTx.interestPaid == expect[index][0]
            assert receiveTx.fee == expect[index][1]
            assert receiveTx.fine == 0.00
            assert receiveTx.differential == expect[index][4]
            assert receiveTx.isShareCapital == false
            assert receiveTx.paymentDate == period.dueDate

            contract = Contract.get(1)
            assert contract.loanBalance == expect[index][3]
            println " ==> pass"
        }
        /*********************** /END Verify money ****************************/
    }

    /**********************************
     * Commision
     *********************************/

    void testCommissionProcessPeriodPayoff() {
        def contract = setUpMockForPeriodPayoff('Commission')
        def p1 = Period.get(1)
        def p2 = Period.get(2)
        def p3 = Period.get(3)

        service.interestProcessorService = [ process: { p, d ->
            if (p.id == 1) {
                [actualInterest: 40.655737, effectedInterest: 30.491805, fee: 10.163932, cooperativeInterest: p.contract.cooperativeShare * 30.491805]
            }
            else if (p.id == 2) {
                [actualInterest: 26.255522, effectedInterest: 19.691643, fee: 6.563879, cooperativeInterest: p.contract.cooperativeShare * 19.691643]
            }
            else if (p.id == 3) {
                [actualInterest: 13.312950, effectedInterest: 9.984713, fee: 3.328237, cooperativeInterest: p.contract.cooperativeShare * 9.984713]
            }
        } ] as InterestProcessorService

        /************************** Verify money ******************************/
        service.process(p1, 706.00, 0.00, false, p1.dueDate)
        assert ReceiveTransaction.list().size() == 1

        p1 = Period.get(1) // Reload data
        assert p1.payoffDate == p1.dueDate
        //assert p1.cooperativeInterest != 0.000000


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
        assert receiveTx.paymentDate == p1.dueDate

        contract = Contract.get(1)
        assert contract.loanBalance == 1334.655737
        /*********************** /END Verify money ****************************/

        /************************** Verify money ******************************/
        service.process(p2, 706.00, 0.00, false, p2.dueDate)
        assert ReceiveTransaction.list().size() == 2

        p2 = Period.get(2) // Reload data
        assert p2.payoffDate == p2.dueDate
        assert p2.cooperativeInterest != 0.000000


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
        assert receiveTx.paymentDate == p2.dueDate
        assert p2.cooperativeInterest != 0.000000


        contract = Contract.get(1)
        assert contract.loanBalance == 654.911259
        /*********************** /END Verify money ****************************/

        /************************** Verify money ******************************/
        service.process(p3, 708.00, 0.00, false, p3.dueDate)
        assert ReceiveTransaction.list().size() == 3

        p3 = Period.get(3) // Reload data
        assert p3.payoffDate == p3.dueDate

        receiveTx = ReceiveTransaction.get(3)
        assert receiveTx.amount == 708.00
        assert receiveTx.balanceForward == 654.911259
        assert receiveTx.balancePaid == 654.911259
        assert receiveTx.interestRate == 24.00
        assert receiveTx.interestPaid == 9.984713
        assert receiveTx.fee == 3.328237
        assert receiveTx.fine == 0.00
        assert receiveTx.differential == 39.775791
        assert receiveTx.isShareCapital == false
        assert receiveTx.paymentDate == p3.dueDate
        assert p3.cooperativeInterest != 0.000000

        contract = Contract.get(1)
        assert contract.loanBalance == 0.00
        /*********************** /END Verify money ****************************/
    }

    void testCommissionProcessPeriodPayoff_12Months() {
        def contract = setUpMockForPeriodPayoff_12Months('Commission')
        def period

        service.interestProcessorService = [ process: { p, d ->
            switch (p.id) {
                case 1:
                    def mocked = [actualInterest: 40.655737, effectedInterest: 30.491805, fee: 10.163932]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 2:
                    def mocked = [actualInterest: 36.091588, effectedInterest: 27.068692, fee: 9.022896]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 3:
                    def mocked = [actualInterest: 33.840765, effectedInterest: 25.380575, fee: 8.460190]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 4:
                    def mocked = [actualInterest: 29.362388, effectedInterest: 22.021792, fee: 7.340596]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 5:
                    def mocked = [actualInterest: 26.750469, effectedInterest: 20.062852, fee: 6.687617]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 6:
                    def mocked = [actualInterest: 23.106708, effectedInterest: 17.330032, fee: 5.776676]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 7:
                    def mocked = [actualInterest: 18.763429, effectedInterest: 14.072573, fee: 4.690856]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 8:
                    def mocked = [actualInterest: 15.582756, effectedInterest: 11.687068, fee: 3.895688]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 9:
                    def mocked = [actualInterest: 11.334174, effectedInterest: 8.500631, fee: 2.833543]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 10:
                    def mocked = [actualInterest: 7.754838, effectedInterest: 5.816129, fee: 1.938709]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 11:
                    def mocked = [actualInterest: 3.724937, effectedInterest: 2.793703, fee: 0.931234]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break
                case 12:
                    def mocked = [actualInterest: 0, effectedInterest: 0, fee: 0]
                    mocked.cooperativeInterest = mocked.effectedInterest * p.contract.cooperativeShare
                    mocked
                    break

                default:
                    null
            }
        } ] as PeriodService

        def expect = [
            /***************************************************************************
            * ดอกเบี้ยตามกฎหมาย, ค่าธรรมเนียม, เหลือไปตัดเงินต้น, เงินต้นที่เหลือ, ส่วนต่าง
            ****************************************************************************/

            [30.491805, 10.163932,  165.344263, 1834.655737,    0],
            [27.068692, 9.022896,   169.908412, 1664.747325,    0],
            [25.380575, 8.460190,   172.159235, 1492.588090,    0],
            [22.021792, 7.340596,   176.637612, 1315.950478,    0],
            [20.062852, 6.687617,   179.249531, 1136.700947,    0],
            [17.330032, 5.776676,   182.893292, 953.807655,     0],
            [14.072573, 4.690856,   187.236571, 766.571084,     0],
            [11.687068, 3.895688,   190.417244, 576.153840,     0],
            [8.500631,  2.833543,   194.665826, 381.488014,     0],
            [5.816129,  1.938709,   198.245162, 183.242852,     0],
            [2.793703,  0.931234,   183.242852, 0,              19.032211],
            [0,         0,          0,          0,              214]
        ]


        expect.metaClass.fillInterestCoop = { rate ->
            delegate.each { it[5] = rate*it[0] }
        }



        /************************** Verify money ******************************/
        (1..12).each {
            print "period: ${it}"
            period = Period.get(it)
            expect.fillInterestCoop(period.contract.cooperativeShare)
            service.process(period, period.amount, 0.00, false, period.dueDate)
            assert ReceiveTransaction.list().size() == period.id

            period = Period.get(period.id) // Reload data
            assert period.payoffDate == period.dueDate

            def receiveTx = ReceiveTransaction.get(period.id)
            def index = (period.id - 1) as Integer
            assert receiveTx.amount == period.amount
            assert receiveTx.balanceForward == (index ? expect[index - 1][3] : 2000.00)
            assert receiveTx.balancePaid == expect[index][2]
            assert receiveTx.interestRate == 24.00
            assert receiveTx.interestPaid == expect[index][0]
            assert receiveTx.fee == expect[index][1]
            assert receiveTx.fine == 0.00
            assert receiveTx.differential == expect[index][4]
            assert receiveTx.isShareCapital == false
            assert receiveTx.paymentDate == period.dueDate

            contract = Contract.get(1)
            assert contract.loanBalance == expect[index][3]
            println " ==> pass"
        }
        /*********************** /END Verify money ****************************/
    }



    /***********************************
     * Flat
     **********************************/
    void testFlatPeriodProcess() {
        def getDate = { str -> Date.parse("yyyy-MM-dd", str) }

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

        service.interestProcessorService = [ process: { p, d ->
            def result = [
                [609.836066,  609.836066,  0],
                [565.57377,   565.57377,   0],
                [559.016393,  559.016393,  0],
                [516.393443,  516.393443,  0],
                [508.196721,  508.196721,  0],
                [482.786885,  482.786885,  0],
                [442.622951,  442.622951,  0],
                [431.967213,  431.967213,  0],
                [393.442623,  393.442623,  0],
                [381.147541,  381.147541,  0],
                [355.737705,  355.737705,  0],
                [298.360656,  298.360656,  0],
                [304.918033,  304.918033,  0],
                [270.491803,  270.491803,  0],
                [254.098361,  254.098361,  0],
                [221.311475,  221.311475,  0],
                [203.278689,  203.278689,  0],
                [177.868852,  177.868852,  0],
                [147.540984,  147.540984,  0],
                [127.04918,   127.04918,   0],
                [98.360656,   98.360656,   0],
                [76.229508,   76.229508,   0],
                [50.819672,   50.819672,   0],
                [22.95082,    22.95082,    0]
            ][p.id - 1 as Integer]

            [
                actualInterest: result[0],
                effectedInterest: result[1],
                fee: result[2]
            ]
        } ]

        def expect = [
            [2500,   609.836066, 0,  2500,   57500,  13790.163934,   0],
            [2500,   565.57377,  0,  2500,   55000,  13224.590164,   0],
            [2500,   559.016393, 0,  2500,   52500,  12665.573771,   0],
            [2500,   516.393443, 0,  2500,   50000,  12149.180328,   0],
            [2500,   508.196721, 0,  2500,   47500,  11640.983607,   0],
            [2500,   482.786885, 0,  2500,   45000,  11158.196722,   0],
            [2500,   442.622951, 0,  2500,   42500,  10715.573771,   0],
            [2500,   431.967213, 0,  2500,   40000,  10283.606558,   0],
            [2500,   393.442623, 0,  2500,   37500,  9890.163935,    0],
            [2500,   381.147541, 0,  2500,   35000,  9509.016394,    0],
            [2500,   355.737705, 0,  2500,   32500,  9153.278689,    0],
            [2500,   298.360656, 0,  2500,   30000,  8854.918033,    0],
            [2500,   304.918033, 0,  2500,   27500,  8550,           0],
            [2500,   270.491803, 0,  2500,   25000,  8279.508197,    0],
            [2500,   254.098361, 0,  2500,   22500,  8025.409836,    0],
            [2500,   221.311475, 0,  2500,   20000,  7804.098361,    0],
            [2500,   203.278689, 0,  2500,   17500,  7600.819672,    0],
            [2500,   177.868852, 0,  2500,   15000,  7422.95082,     0],
            [2500,   147.540984, 0,  2500,   12500,  7275.409836,    0],
            [2500,   127.04918,  0,  2500,   10000,  7148.360656,    0],
            [2500,   98.360656,  0,  2500,   7500,   7050,           0],
            [2500,   76.229508,  0,  2500,   5000,   6973.770492,    0],
            [2500,   50.819672,  0,  2500,   2500,   6922.95082,     0],
            [2500,   22.95082,   0,  2500,   0,      6900,           6900]
        ]

        def principle = 60000.00,
            interest = 0.12,
            interestlimit = 0.18,
            numberOfPeriod = 24,
            keep = 14400.00,
            contract = setUpMockForFlatPeriodPayoff(principle, interest, numberOfPeriod, keep, getDate("2012-03-01"))

        // prepare periods
        def periodList = sample_period.collect {
            def p = new Period(
                contract: contract, no: it[0], amount: it[1],
                dueDate: getDate(it[2]), status: true, payoffStatus: false
            )
            p.save()
            return p
        }

        periodList.each { period ->
            print "period: ${period.no}"

            service.flat(period, period.amount, 0.00, false, period.dueDate)
            assert ReceiveTransaction.list().size() == period.id

            period = Period.get(period.id) // Reload data
            assert period.payoffDate == period.dueDate

            def receiveTx = ReceiveTransaction.get(period.id)
            def index = (period.id - 1) as Integer
            assert receiveTx.amount == expect[index][0]
            assert receiveTx.balanceForward == (index ? expect[index - 1][4] : principle)
            assert receiveTx.balancePaid == expect[index][3]
            assert receiveTx.interestRate == interest
            assert receiveTx.interestPaid == expect[index][1]
            assert receiveTx.fee == expect[index][2]
            assert receiveTx.fine == 0.00
            assert receiveTx.differential == expect[index][6]
            assert receiveTx.isShareCapital == false
            assert receiveTx.paymentDate == period.dueDate

            contract = Contract.get(1)
            assert contract.loanBalance == expect[index][4]
            assert contract.advancedInterestBalance == expect[index][5]

            println " ===> pass"
        }
    }

    /***********************************
     * Commission
     **********************************/

}