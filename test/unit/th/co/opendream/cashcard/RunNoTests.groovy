package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(RunNo)
class RunNoTests extends DomainTestTemplate {

    def requiredProperties() {
       ['key', 'prefix', 'suffix', 'currentNo', 'description', 'padSize',
       'template']
    }

    def domainClass() {
    	RunNo.class
    }

    void testValidateKey() {
    	mockForConstraintsTests(RunNo)

    	def runNo = new RunNo()

    	verifyNotNull(runNo, 'key')

    	def exists = new RunNo(key: 'Member', currentNo: 0, description: 'test member', padSize: 4)
    	exists.save()

    	runNo.key = 'Member'
    	verifyUnique(runNo, 'key')

    	verifyNotNull(runNo, 'description')
    	verifyNotNull(runNo, 'padSize')
    	verifyNotNull(runNo, 'currentNo')

    	assertTrue runNo.validate(['prefix'])
    	assertTrue runNo.validate(['suffix'])
    	assertTrue runNo.validate(['template'])
    }
}
