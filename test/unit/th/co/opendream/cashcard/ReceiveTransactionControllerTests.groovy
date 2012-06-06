package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ReceiveTransactionController)
@Mock(ReceiveTransaction)
class ReceiveTransactionControllerTests {

    void testCancel() {

        params.id = '1'

        ReceiveTransaction.metaClass.static.get = {Serializable id ->
        	[id: 1, period: [ contract: [ id: 1 ] ] ]
        }

        controller.periodProcessorService = [
            cancelReceiveTransaction: { r -> true }
        ]
        controller.cancel()
        assert response.redirectUrl == '/contract/show/1'

        response.reset()

        controller.periodProcessorService = [
            cancelReceiveTransaction: { r -> throw Exception("Test"); }
        ]
        controller.cancel()
        assert response.redirectUrl == '/error'
    }

}
