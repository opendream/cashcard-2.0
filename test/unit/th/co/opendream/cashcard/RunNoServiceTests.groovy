package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(RunNoService)
@Mock([RunNo])
class RunNoServiceTests {

	def generateValidRunNo(key) {
		def runNo = new RunNo(
			key: key,
			description: '',
			currentNo: 0,
			padSize: 4
		)
		runNo.save()
		runNo
	}

    void testNext() {
    	generateValidRunNo('Member')

    	def count = 0
    	service.metaClass.format = { runno, date ->
    		count++
    		runno.currentNo
    	}

    	def no = service.next('Member')
    	assert count == 1
    	assert no == 1

    	no = service.next('Member')
    	assert count == 2
    	assert no == 2
    }

    void testHasKey() {
        generateValidRunNo('Member')

        assertTrue service.hasKey('Member')

        assert service.hasKey('NoKey') == false
    }

    void testCurrentNumberString() {
    	generateValidRunNo('Member')

    	assert service.currentNumberString(1, 4) == '0001'
    }

    void testFormat() {
    	def runno = generateValidRunNo('Member')

    	// Default (no template)
    	def date = Date.parse("yyyy-MM-dd", "2012-01-01")
    	assert service.format(runno, date) == "0000"

    	runno.currentNo = 123
    	assert service.format(runno, date) == "0123"

    	// Custom prefix, suffix and template
    	runno.prefix = "CC-"
    	runno.suffix = "-OK"
    	assert service.format(runno, date) == "CC-0123-OK"

    	runno.template = "CC-{current()}-GO"
    	assert service.format(runno, date) == "CC-0123-GO"

    	runno.template = '$mm-$Ayy-$Byy-$Ayyyy-$Byyyy-{current(5)}'
    	assert service.format(runno, date) == "01-12-55-2012-2555-00123"
    }
}
