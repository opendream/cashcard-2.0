package th.co.opendream.cashcard

class ReceiveTransactionController {

	def periodProcessorService

    def cancel() {
    	def receiveTx = ReceiveTransaction.get(params.id)

    	try {
    		periodProcessorService.cancelReceiveTransaction(receiveTx)

    		redirect url: "/contract/show/${receiveTx.period.contract.id}"
    	}
    	catch (Exception e) {
    		println e
    		redirect url: "/error"
    	}
    }
}
