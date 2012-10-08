package cashcard

class StateController {
	def memberService

    def index() { }

    def getUserInfo() {
    	render memberService.getMemberByMemberIds([38])
    }
}
