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

    def setUpMockForPeriodPayoff_12Months() {
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
        def contract = setUpMockForPeriodPayoff()
        def p1 = Period.get(1)
        def p2 = Period.get(2)
        def p3 = Period.get(3)

        service.metaClass.calculateInterestFormulaOne = { p, d ->
            if (p.id == 1) {
                [actualInterest: 40.655737, effectedInterest: 30.491805, fee: 10.163932]
            }
            else if (p.id == 2) {
                [actualInterest: 26.255522, effectedInterest: 19.691643, fee: 6.563879]
            }
            else if (p.id == 3) {
                [actualInterest: 13.312950, effectedInterest: 9.984713, fee: 3.328237]
            }
        }

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
        assert receiveTx.balancePaid == 654.911259
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

    void testPeriodPayoff_12Months() {
        def contract = setUpMockForPeriodPayoff_12Months()
        def period

        service.metaClass.calculateInterestFormulaOne = { p, d ->
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
        }

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
            service.periodPayoff(period, period.amount, 0.00, false, period.dueDate)
            assert ReceiveTransaction.list().size() == period.id

            period = Period.get(period.id) // Reload data
            assert period.payoffStatus == true
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

            contract = Contract.get(1)
            assert contract.loanBalance == expect[index][3]
            println " ==> pass"
        }
        /*********************** /END Verify money ****************************/
    }

    void testCalculateFormulaOne_withZeroBalance() {
        setUpPeriod()

        def contract = Contract.get(1)
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

        def result = service.calculateInterestFormulaOne(period, period.dueDate)
        assert result.actualInterest == 0.00
        assert result.effectedInterest == 0.00
        assert result.fee == 0.00
    }
}
