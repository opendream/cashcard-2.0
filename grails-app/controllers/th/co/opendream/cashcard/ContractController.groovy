package th.co.opendream.cashcard

class ContractController {

    def periodService

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
                def periodList = periodService.generatePeriod(contract.loanAmount, contract.numberOfPeriod)
                periodList.each { period ->
                    period.contract = contract
                    period.save()
                }

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

        def c = Period.createCriteria()
        def periodList = c.list(sort: 'no', order: 'asc') {
            eq('contract', contract)
        }
        periodList = Period.findAll()

        if (contract) {
            render view: '/contract/show', model: [
                contract: contract,
                loanType: contract.loanType,
                periodList: periodList
            ]
        }
        else {
            redirect url: '/error'
        }
    }

    def approve() {
        def contract = Contract.get(params.id)

        if (!contract) {
            redirect uri: '/error'
        }
        render view: 'approve', model: [contractInstance: contract]
    }

    def doApprove() {
        def existsContract = Contract.get(params?.id)

        if (!existsContract) {
            redirect uri: '/error'
            return
        }

        existsContract.approvalDate = params.approvalDate
        existsContract.approvalStatus = true
        if  (existsContract.save()) {
            def periodList = periodService.generatePeriod(contract.loanAmount, contract.numberOfPeriod)
            def lastDueDate = existsContract.approvalDate.plus(30)
            periodList.each { period ->
                period.dueDate = lastDueDate
                period.contract = contract
                period.save()

                lastDueDate.plus(30)
            }

            redirect action: 'show', controller: 'member', id: existsContract.member.id
        }
        else {
            render view: 'approve', model: [contract: existsContract]
        }
    }

    def preparePeriod() {
        def amount = params.amount as BigDecimal,
            nop = params.nop as Integer

        if (amount && nop) {
            def periodList = periodService.generatePeriod(amount, nop)

            render view: '/contract/preparePeriod', model: [periodList: periodList]
        }
        else {
            redirect url: '/error'
        }
    }
    
    def payloan() {
        def existsContract = Contract.get(params?.id)

        if (!existsContract) {
            redirect uri: '/error'
            return
        }

       render view: 'payloan', model: [contractInstance: existsContract]
    }

    def doPayloan() {
        def existsContract = Contract.get(params?.id)

        if (!existsContract || params.payloanDate =='') {
            redirect uri: '/error'
            return
        }

        existsContract.payloanDate = params.payloanDate
        existsContract.loanReceiveStatus = true

        if (existsContract.save()) {
            redirect controller: 'member', action: 'show', id: existsContract.member.id
        }
    }
}
