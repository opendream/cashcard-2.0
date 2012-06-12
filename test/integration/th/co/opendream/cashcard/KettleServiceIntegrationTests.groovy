package th.co.opendream.cashcard

import static org.junit.Assert.*
import org.junit.*

class KettleServiceIntegrationTests extends GroovyTestCase {
    def kettleService
    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testExtractMemberCsv() {
        def filename = 'member.csv'
        def extension = 'text/csv'
        def result = kettleService.extractMember(filename, extension)

        assert false == result.contains('ERROR')
        println result
    }

    @Test
    void testExtractMemberCsvFails() {
        def filename = 'member2xx.csv'
        def extension = 'text/csv'

        shouldFail(RuntimeException) {
            def result = kettleService.extractMember(filename, extension)
        }
    }

    @Test
    void testExtractMemberJson() {
        def filename = 'member.json'
        def extension = 'application/json'
        def result = kettleService.extractMember(filename, extension)

        assert false == result.contains('ERROR')
        println result
    }

    @Test
    void testExtractMemberJsonFails() {
        def filename = 'member2.json'
        def extension = 'application/json'
        
        shouldFail(RuntimeException) {
            def result = kettleService.extractMember(filename, extension)  
        }
    }

    @Test
    void testExtractMemberTypeMismatchFails() {
        def filename = 'member.xls'
        def extension = 'application/json'
        
        shouldFail(RuntimeException) {
            def result = kettleService.extractMember(filename, extension)              
        }
    }
}
