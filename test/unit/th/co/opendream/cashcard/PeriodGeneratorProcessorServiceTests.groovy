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
            interestRate: 24.00,
            numberOfPeriod: 3
        ).save()

        service.interestProcessorService = new InterestProcessorService()
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

        def periodList = service.generate(1, 1000.00, 3)

        assert periodList[0].amount == 353
        assert periodList[1].amount == 353
        assert periodList[2].amount == 354
        assert periodList.size() == 3

        periodList = service.generate(1, 1000.00, 6)

        assert periodList[0].amount == 186
        assert periodList[1].amount == 186
        assert periodList[2].amount == 186
        assert periodList[3].amount == 186
        assert periodList[4].amount == 186
        assert periodList[5].amount == 190
        assert periodList.size() == 6
    }

    void testGeneratePeriodCommission() {
        def loanType = LoanType.get(1)
        loanType.processor = 'Commission'
        loanType.save()

        def periodList = service.generate(1, 1000.00, 3)

        assert periodList[0].amount == 353
        assert periodList[1].amount == 353
        assert periodList[2].amount == 354
        assert periodList.size() == 3

        periodList = service.generate(1, 1000.00, 6)

        assert periodList[0].amount == 186
        assert periodList[1].amount == 186
        assert periodList[2].amount == 186
        assert periodList[3].amount == 186
        assert periodList[4].amount == 186
        assert periodList[5].amount == 190
        assert periodList.size() == 6
    }

    void testGeneratePeriodFlat() {
        def loanType = LoanType.get(1)
        loanType.processor = 'Flat'
        loanType.save()

        def periodList = service.generate(1, 1000.00, 3)

        assert periodList[0].amount == 333
        assert periodList[1].amount == 333
        assert periodList[2].amount == 334
        assert periodList.size() == 3

        periodList = service.generate(1, 1000.00, 6)

        assert periodList[0].amount == 166
        assert periodList[1].amount == 166
        assert periodList[2].amount == 166
        assert periodList[3].amount == 166
        assert periodList[4].amount == 166
        assert periodList[5].amount == 170
        assert periodList.size() == 6
    }
}
