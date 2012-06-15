package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ContractService)
@Mock([Contract, LoanType, Member, Period])
class ContractServiceTests {

    @Before
    void setUp() {
        def loanType = new LoanType(
            name: "Common",
            processor: "Effective",
            interestProcessor: "effective",
            periodProcessor: "effective",
            periodGeneratorProcessor: "effective",
            interestRate: 12.00,
            maxInterestRate: 18.00,
            numberOfPeriod: 3
        )
        loanType.save()
        println loanType.errors

        def member = new Member(
            identificationNumber:"1159900100015",
            firstname:"สมหญิง",
            lastname: "รักเรียน",
            telNo: "0818526122",
            gender: "MALE",
            address: "Opendream"
        )
        member.utilService = [ check_id_card: { id -> true } ]
        member.save()
    }

    void testCopyLoanProperties() {
        def loanType = LoanType.get(1)
        def contract = new Contract()

        service.copyLoanProperties(contract, loanType)
        assert contract.processor == "effective"
        assert contract.interestProcessor == "effective"
        assert contract.periodProcessor == "effective"
        assert contract.periodGeneratorProcessor == "effective"
        assert contract.interestRate == 12.00
        assert contract.maxInterestRate == 18.00

        loanType.interestProcessor = "flat"
        loanType.periodProcessor = "effective"
        loanType.periodGeneratorProcessor = "commission"
        loanType.interestRate = 17.00
        loanType.maxInterestRate = 21.00
        loanType.canPayAllDebt = true

        service.copyLoanProperties(contract, loanType)
        assert contract.interestProcessor == "flat"
        assert contract.periodProcessor == "effective"
        assert contract.periodGeneratorProcessor == "commission"
        assert contract.interestRate == 17.00
        assert contract.maxInterestRate == 21.00
        assert contract.canPayAllDebt == true
    }

    void testGetInterestAmountJSON() {
        def loanType = LoanType.get(1)
        def contract = new Contract(
            code: "ก.55-1000-20",
            processor: "Express",
            interestProcessor: loanType.interestProcessor,
            periodProcessor: loanType.periodProcessor,
            periodGeneratorProcessor: loanType.periodGeneratorProcessor,
            member: Member.get(1),
            loanType: loanType,
            loanAmount: 2000.00,
            interestRate: 12.00,
            maxInterestRate: 18.00,
            loanBalance: 2000.00,
            approvalStatus: false,
            loanReceiveStatus: false,
            guarantor1: '',
            guarantor2: '',
            numberOfPeriod: 3,
            signedDate: Date.parse("yyyy-MM-dd", "2012-03-01")
        )
        contract.save()
        println contract.errors

        def period = new Period(
            id: 1, contract: contract, amount: 686.00, no: 1,
            dueDate: Date.parse("yyyy-MM-dd", "2012-04-01"), status: true,
            payoffStatus: false, interestAmount: 20.00, interestOutstanding: 20.00
        ).save()

        def paymentDate = Date.parse("yyyy-MM-dd", "2012-04-11")

        // Mock service
        service.interestProcessorService = [
            process: { Period p, d ->
                [
                    actualInterest: 6.557377
                ]
            }
        ]

        def interestAmount = service.getInterestAmountOnCloseContract(period, paymentDate)
        assert interestAmount.totalDebt == 2020.00
        assert interestAmount.loanBalance == 2000.00
        assert interestAmount.goalInterest == 20.00
        assert interestAmount.realInterest == 6.557377
        assert interestAmount.callInterest == 20.00

        // ==== Part 2 ====
        contract.loanBalance = 1920.00
        contract.save()

        period.interestOutstanding = 0.00
        period.interestPaid = true
        period.save()

        // Mock service
        service.interestProcessorService = [
            process: { Period p, d ->
                [
                    actualInterest: 3.147541
                ]
            }
        ]

        interestAmount = service.getInterestAmountOnCloseContract(period, paymentDate.plus(5))
        assert interestAmount.totalDebt == 1920.00
        assert interestAmount.loanBalance == 1920.00
        assert interestAmount.goalInterest == 0.00
        assert interestAmount.realInterest == 3.147541
        assert interestAmount.callInterest == 0.00
    }
}
