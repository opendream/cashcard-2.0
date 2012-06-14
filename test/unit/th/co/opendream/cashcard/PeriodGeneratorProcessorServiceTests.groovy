package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PeriodGeneratorProcessorService)
@Mock([LoanType, Member, Period, Contract, InterestProcessorService])
class PeriodGeneratorProcessorServiceTests {
    def loanType, interestProcessorService

    @Before
    void setUp() {
        loanType = new LoanType(
            name: 'Common',
            processor: 'Effective',
            interestProcessor: 'Effective',
            periodProcessor: 'Effective',
            periodGeneratorProcessor: 'Effective',
            interestRate: 24.00,
            numberOfPeriod: 3
        ).save()

        service.interestProcessorService = new InterestProcessorService()
        service.periodService = [
            beforeInsert: { p ->
                p.outstanding = p.amount

                p
            }
        ]
    }

    void testProcess() {
        def loanType = LoanType.get(1)

        def effectiveCounter = 0
        service.metaClass.effective = { l, a, n ->
            effectiveCounter++
        }

        loanType.periodGeneratorProcessor = 'Effective'
        loanType.save()
        service.generate(loanType, 2, 3)
        assert effectiveCounter == 1

        def flatCounter = 0
        service.metaClass.flat = { l, a, n ->
            flatCounter++
        }

        loanType.periodGeneratorProcessor = 'Flat'
        loanType.save()
        service.generate(loanType, 2, 3)
        assert effectiveCounter == 1
        assert flatCounter == 1
    }

    void testEffective() {
        def amount = 2000,
            numberOfPeriod = 3;
        def periodList = service.effective(amount, numberOfPeriod, loanType.interestRate)
    }

    void testCommission() {
        def amount = 2000,
            numberOfPeriod = 3;
        def periodList = service.commission(amount, numberOfPeriod, loanType.interestRate)
    }

    void testFlat() {
        def amount = 2000,
        numberOfPeriod = 3;
        def periodList = service.flat(amount, numberOfPeriod, loanType.interestRate)
    }

    void testGenerateEffective() {
        def amount = 2000,
            numberOfPeriod = 3;


        def generated = service.generate(loanType, amount, numberOfPeriod)
        def effected = service.effective(amount, numberOfPeriod, loanType.interestRate)

        assert (generated*.amount).equals(effected*.amount)

        // Test With Id
        generated = service.generate(loanType.id as Integer, amount, numberOfPeriod)
        assert (generated*.amount).equals(effected*.amount)
    }

    void testGeneratePeriodEffective() {
        def periodList = service.effective(1000.00, 3, 24.00)

        assert periodList[0].amount == 353
        assert periodList[1].amount == 353
        assert periodList[2].amount == 353
        assert periodList[0].outstanding == 353
        assert periodList[1].outstanding == 353
        assert periodList[2].outstanding == 353
        assert periodList.size() == 3

        periodList = service.effective(1000.00, 6, 24.00)

        assert periodList[0].amount == 186
        assert periodList[1].amount == 186
        assert periodList[2].amount == 186
        assert periodList[3].amount == 186
        assert periodList[4].amount == 186
        assert periodList[5].amount == 186
        assert periodList[0].outstanding == 186
        assert periodList[1].outstanding == 186
        assert periodList[2].outstanding == 186
        assert periodList[3].outstanding == 186
        assert periodList[4].outstanding == 186
        assert periodList[5].outstanding == 186
        assert periodList.size() == 6
    }

    void testGeneratePeriodCommission() {
        def periodList = service.commission(1000.00, 3, 24.00)

        assert periodList[0].amount == 353
        assert periodList[1].amount == 353
        assert periodList[2].amount == 353
        assert periodList[0].outstanding == 353
        assert periodList[1].outstanding == 353
        assert periodList[2].outstanding == 353
        assert periodList.size() == 3

        periodList = service.commission(1000.00, 6, 24.00)

        assert periodList[0].amount == 186
        assert periodList[1].amount == 186
        assert periodList[2].amount == 186
        assert periodList[3].amount == 186
        assert periodList[4].amount == 186
        assert periodList[5].amount == 186
        assert periodList[0].outstanding == 186
        assert periodList[1].outstanding == 186
        assert periodList[2].outstanding == 186
        assert periodList[3].outstanding == 186
        assert periodList[4].outstanding == 186
        assert periodList[5].outstanding == 186
        assert periodList.size() == 6
    }

    void testGeneratePeriodFlat() {
        def periodList = service.flat(1000.00, 3, 24.00)

        assert periodList[0].amount == 333
        assert periodList[1].amount == 333
        assert periodList[2].amount == 333
        assert periodList[0].outstanding == 333
        assert periodList[1].outstanding == 333
        assert periodList[2].outstanding == 333
        assert periodList.size() == 3

        periodList = service.flat(1000.00, 6, 24.00)

        assert periodList[0].amount == 166
        assert periodList[1].amount == 166
        assert periodList[2].amount == 166
        assert periodList[3].amount == 166
        assert periodList[4].amount == 166
        assert periodList[5].amount == 166
        assert periodList[0].outstanding == 166
        assert periodList[1].outstanding == 166
        assert periodList[2].outstanding == 166
        assert periodList[3].outstanding == 166
        assert periodList[4].outstanding == 166
        assert periodList[5].outstanding == 166
        assert periodList.size() == 6
    }

    void testGeneratePeriodExpressCash01() {
        def periodList = service.expresscash01(1000.00, 3, 24.00)

        assert periodList[0].amount == 353
        assert periodList[1].amount == 353
        assert periodList[2].amount == 353
        assert periodList[0].outstanding == 353
        assert periodList[1].outstanding == 353
        assert periodList[2].outstanding == 353
        assert periodList[0].interestAmount == 20
        assert periodList[1].interestAmount == 20
        assert periodList[2].interestAmount == 20
        assert periodList[0].interestOutstanding == 20
        assert periodList[1].interestOutstanding == 20
        assert periodList[2].interestOutstanding == 20
        assert periodList[0].interestPaid == false
        assert periodList[1].interestPaid == false
        assert periodList[2].interestPaid == false
        assert periodList.size() == 3

        periodList = service.expresscash01(1000.00, 6, 24.00)

        assert periodList[0].amount == 186
        assert periodList[1].amount == 186
        assert periodList[2].amount == 186
        assert periodList[3].amount == 186
        assert periodList[4].amount == 186
        assert periodList[5].amount == 186
        assert periodList[0].outstanding == 186
        assert periodList[1].outstanding == 186
        assert periodList[2].outstanding == 186
        assert periodList[3].outstanding == 186
        assert periodList[4].outstanding == 186
        assert periodList[5].outstanding == 186
        assert periodList[0].interestAmount == 20
        assert periodList[1].interestAmount == 20
        assert periodList[2].interestAmount == 20
        assert periodList[3].interestAmount == 20
        assert periodList[4].interestAmount == 20
        assert periodList[5].interestAmount == 20
        assert periodList[0].interestOutstanding == 20
        assert periodList[1].interestOutstanding == 20
        assert periodList[2].interestOutstanding == 20
        assert periodList[3].interestOutstanding == 20
        assert periodList[4].interestOutstanding == 20
        assert periodList[5].interestOutstanding == 20
        assert periodList[0].interestPaid == false
        assert periodList[1].interestPaid == false
        assert periodList[2].interestPaid == false
        assert periodList[3].interestPaid == false
        assert periodList[4].interestPaid == false
        assert periodList[5].interestPaid == false
        assert periodList.size() == 6
    }
}
