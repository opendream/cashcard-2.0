package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PeriodGeneratorProcessorService)
@Mock([LoanType, Member, Period, Contract])
class PeriodGeneratorProcessorServiceTests {
    def loanType

    @Before
    void setUp() {
        loanType = new LoanType(
            name: 'Common',
            processor: 'Effective',
            interestRate: 24.00,
            numberOfPeriod: 3
        ).save()
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
}
