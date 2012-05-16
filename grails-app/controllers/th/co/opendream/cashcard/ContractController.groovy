package th.co.opendream.cashcard

class ContractController {

    def index() { }

    def create() {
    	def member = Member.get(params.memberId)
    	def loanType = LoanType.get(params.loanType) 

    	if (member && loanType) {
            params.member = member
            params.loanType = loanType

    		def contract = new Contract(params)
            contract.loanBalance = contract.loanAmount

    		if (contract.save()) {
    			redirect action: 'show', controller: 'member', id: member.id
    		}
    		else {
    			render view: '/contract/sign', model: [
                    member: member,
                    loanType: loanType,
                    contract: contract
                ]
    		}
    	}
    	else {
    		redirect url: '/error'
    	}
    }

    def selectLoanType() {
        def member = Member.get(params.id)

        if (member) {
            def availableLoanType = LoanType.list()
            render view: 'selectLoanType', model: [
                member: member,
                availableLoanType: availableLoanType
            ]
        }
        else {
            redirect uri: '/error'
        }
    }

    def sign() {
        def member = Member.get(params.id)
        def loanType = LoanType.get(params.type)
        def contract = new Contract()

        // Make blank for form
        contract.loanAmount = null

        if (member && loanType) {
            render view: 'sign', model: [
                member: member,
                loanType: loanType,
                contract: contract
            ]
        }
        else {
            redirect uri: '/error'
        }
    }

    def show() {
        def contract = Contract.get(params.id)

        if (contract) {
            render view: '/contract/show', model: [
                contract: contract,
                loanType: contract.loanType
            ]
        }
        else {
            redirect url: '/error'
        }
    }
}
