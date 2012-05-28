package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PeriodGeneratorProcessorService)
@Mock([LoanType, Member, Period, Contract])
class PeriodGeneratorProcessorServiceTests {
/*
    void testEffective() {
        def amount = 2000,
            numberOfPeriod = 3;
        def periodList = service.effective(amount, numberOfPeriod)
        periodList.each {
            println it.dump()
        }
    }
    */

    void testSomething() {
        fail "Implement me"
    }
}
