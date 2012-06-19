package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ShareCapitalAccount)
class ShareCapitalAccountTests {

    void testIfInheritFromAccountDomain() {
    	def account = new ShareCapitalAccount()

    	assert account.instanceOf(Account) == true
    }
}
