package th.co.opendream.cashcard

class ContractController {

    def index() { }

    def create() {
    	def member = Member.get(params.memberId)
    	def loanType = LoanType.get(params.loanType) 

    	if (member && loanType) {
    		def contract = new Contract(params)
    		contract.save()

    		if (contract.id) {
    			redirect action: 'show', id: contract.id
    		}
    		else {
    			redirect action: 'doLoan', controller: 'member', id: member.id, params: [type: loanType.id]
    		}
    	}
    	else {
    		redirect url: '/error'
    	}
    }
}
