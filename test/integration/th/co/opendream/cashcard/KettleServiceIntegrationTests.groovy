package th.co.opendream.cashcard

import static org.junit.Assert.*
import org.junit.*
//import org.springframework.web.multipart.commons.CommonsMultipartFile

class KettleServiceIntegrationTests extends GroovyTestCase {
    def kettleService
    def file

    @Before
    void setUp() {
        
        file = new Object()
        file.metaClass.transferTo = { File f -> 'do nothing'}
    }

    @After
    void tearDown() {        
    }

    @Test
    void testExtractMemberCsv() {
        file.metaClass.originalFilename = 'member.csv'
        file.metaClass.contentType = 'text/csv'
        def result = kettleService.extractMember(file)

        assert false == result.contains('ERROR')
        println result
    }

    @Test
    void testExtractMemberCsvFails() {
        file.metaClass.originalFilename = 'member2xx.csv'
        file.metaClass.contentType = 'text/csv'

        shouldFail(RuntimeException) {
            def result = kettleService.extractMember(file)
        }
    }

    @Test
    void testExtractMemberJson() {
        file.metaClass.originalFilename = 'member.json'
        file.metaClass.contentType = "application/json"

        def result = kettleService.extractMember(file)

        assert false == result.contains('ERROR')
        println result
    }

    @Test
    void testExtractMemberJsonFails() {
        file.metaClass.originalFilename = 'member2.json'
        file.metaClass.contentType = 'application/json'
        
        shouldFail(RuntimeException) {
            def result = kettleService.extractMember(file)
        }
    }

    @Test
    void testExtractMemberTypeMismatchFails() {
        file.metaClass.originalFilename = 'member.xls'
        file.metaClass.contentType = 'application/json'
        
        shouldFail(RuntimeException) {
            def result = kettleService.extractMember(file)
        }
    }
}
