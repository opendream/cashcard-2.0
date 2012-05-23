package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ProcessorService)
@Mock(LoanType)
class ProcessorServiceTests {

    @Before
    void setUp() {
        new LoanType(name: "A", processor: "Effective").save()
        new LoanType(name: "B", processor: "Hybrid").save()
        new LoanType(name: "C", processor: "Flat").save()
    }

    void testProcessMethod() {
        LoanType.list().each {
            assert service.process(it.processor) != null
        }
    }
}
