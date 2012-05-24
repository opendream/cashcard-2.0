package th.co.opendream.cashcard

import groovy.time.TimeCategory

class ContractController {

    def periodService, utilService

    def create() {
    	def member = Member.get(params.memberId)
    	def loanType = LoanType.get(params.loanType)

    	if (member && loanType) {
            params.member = member
            params.loanType = loanType

    		def contract = new Contract(params)
            contract.loanBalance = contract.loanAmount as BigDecimal
            contract.interestRate = 24.00

    		if (contract.save()) {
                def numberOfPeriod = (params.numberOfPeriod ? params.numberOfPeriod : 0) as Integer
                def totalDebt = contract.loanAmount + (contract.loanAmount * (contract.interestRate / 100 / 12) * numberOfPeriod)
                def periodList = periodService.generatePeriod(totalDebt, numberOfPeriod)
                periodList.each { period ->
                    period.contract = contract
                    period.status = false
                    period.payoffStatus = false
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
            def c = Period.createCriteria()
            def periodList = c.list(sort: 'no', order: 'asc') {
                eq('contract', existsContract)
            }

            use(TimeCategory) {
                def lastDueDate = existsContract.approvalDate + 1.month
                periodList.each { period ->
                    period.dueDate = lastDueDate
                    period.contract = existsContract
                    period.status = true
                    period.payoffStatus = false
                    period.save()

                    lastDueDate += 1.month
                }
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

        if (!existsContract || params.payloanDate =='' || !utilService.isPayable(existsContract)) {
            redirect uri: '/error'
            return
        }

        existsContract.payloanDate = params.payloanDate
        existsContract.loanReceiveStatus = true

        def transaction = new Transaction(amount: existsContract.loanAmount, sign: -1)

        if (transaction.save() && existsContract.save()) {
            redirect controller: 'member', action: 'show', id: existsContract.member.id
        }
    }

    def payoff() {
        def period = Period.get(params.id)

        if (period) {
            def contract = period.contract
            def receiveTx = new ReceiveTransaction()
            if (contract && contract.approvalStatus && contract.loanReceiveStatus) {
                render view: 'payoff', model: [period: period, receiveTx: receiveTx]
            }
            else {
                redirect url: '/error'
            }
        }
        else {
            redirect url: '/error'
        }
    }

    def doPayoff() {
        def period         = Period.get(params.id)
        def amount         = params.amount         ?: '0.00' as BigDecimal
        def fine           = params.fine           ?: '0.00' as BigDecimal
        def isShareCapital = params.isShareCapital ?: false

        if (period) {
            try {
                periodService.periodPayoff(period, amount, fine, isShareCapital, new Date())
                redirect url: "/member/show/${period.contract.member.id}"
            }
            catch (e) {
                render view: '/contract/payoff', model: [contract: period.contract, period: period, amount: amount, fine: fine, isShareCapital: isShareCapital]
            }
        }
        else {
            redirect url: '/error'
        }
    }
}
