package cashcard

class MachineController {
    static allowedMethods = [getUserInfo: 'POST']

	def memberService

    def index() { }

    def getMobileNumber() {
    	println params
    	if (params.cardId) {
	    	render memberService.getMemberByIdentificationNumber(params.cardId)
    	}
    	else {
    		-1
    	}
    }

    def getUserInfo() {
    	render memberService.getMemberByMemberIds([38])
    }
}
