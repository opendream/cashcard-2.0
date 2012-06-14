package th.co.opendream.cashcard

import groovy.time.TimeCategory

class ContractController {

    /* Service */
    def periodService,
        utilService,
        periodProcessorService,
        periodGeneratorProcessorService,
        interestProcessorService,
        messageService,
        contractService

    def create() {
        def member = Member.get(params.memberId)
        def loanType = LoanType.get(params.loanType)

        if (member && loanType) {
            params.member = member
            params.loanType = loanType

            def signedDate = params.signedDate ?: new Date()

            def contract = new Contract(params)
            contractService.copyLoanProperties(contract, loanType)
            contract.signedDate = signedDate
            contract.loanBalance = contract.loanAmount as BigDecimal

            contract._guarantor1 = Member.get(params._guarantor1)
            contract._guarantor2 = Member.get(params._guarantor2)


            def numberOfPeriod = (params.numberOfPeriod ?: 0) as Integer
            def interest = interestProcessorService.calculateInterestInMonthUnit(loanType.id, contract.loanAmount, contract.numberOfPeriod)

            if (loanType.mustKeepAdvancedInterest) {
                contract.advancedInterestBalance = interest
            }

            if (contract.save()) {
                def periodList = periodGeneratorProcessorService.generate(loanType, contract.loanAmount, numberOfPeriod)
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
            contract.numberOfPeriod = loanType.numberOfPeriod
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
            def c = Period.createCriteria()
            def periodList = c.list(sort: 'no', order: 'asc') {
                eq('contract', contract)
            }

            def totalDebt = 0.00
            periodList = periodList.collect { period ->
                def code
                if (!period.payoffStatus && period.dueDate < new Date()) {
                    code = 'late'
                    totalDebt += period.outstanding
                }
                else if (!period.payoffStatus) {
                    code = 'due'
                    totalDebt += period.outstanding
                }
                else if (period.payoffStatus) {
                    code = 'paid'
                }

                period.metaClass.payoffStatusText = message(code: "contract.show.period.tbody.payoffStatus.${code}")

                period.metaClass.effectiveReceiveTransaction = period.receiveTransaction.findAll { rtx ->
                    rtx.status
                }

                period
            }

            contract.metaClass.isPayable = utilService.isPayable(contract)
            contract.metaClass.currentPeriod = periodService.getCurrentPeriod(contract)

            render view: '/contract/show', model: [
                contract: contract,
                loanType: contract.loanType,
                periodList: periodList,
                totalDebt: totalDebt
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

            def sendsms = params.sendsms?: false

            if (sendsms) {
                messageService.sendApproved(existsContract)
            }

            redirect action: 'show', controller: 'member', id: existsContract.member.id
        }
        else {
            render view: 'approve', model: [contract: existsContract]
        }
    }

    def preparePeriod() {
        def amount = params.amount as BigDecimal,
            nop = params.nop as Integer,
            loanType = params.loanType as Integer

        if (amount && nop) {
            def periodList = periodGeneratorProcessorService.generate(loanType, amount, nop)
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
            def member = contract.member
            def receiveTx = new ReceiveTransaction()
            receiveTx.paymentDate = new Date()
            // TODO:  Integrate With Model
            // MOCKED DATA
            contract?.metaClass?.currentInterest = 300;
            contract?.metaClass?.totalDebt= contract.currentInterest + contract.loanAmount;
            // END MOCK
            if (contract && contract.approvalStatus && contract.loanReceiveStatus) {
                render view: 'payoff', model: [member: member, period: period, receiveTx: receiveTx, contract: contract]
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
        withForm {
            def period         = Period.get(params.id)
            def payAmount      = (params.payAmount         ?: '0.00') as BigDecimal
            def fine           = (params.fine           ?: '0.00') as BigDecimal
            def isShareCapital = params.isShareCapital ?: false
            def paymentDate    = params.paymentDate ?: new Date()

            if (period) {
                def contract = period.contract,
                    member = contract.member,
                    receiveTx

                try {
                    def sendsms = params.sendsms?: false
                    receiveTx = periodProcessorService.process(period, payAmount, fine, isShareCapital, paymentDate)
                    if (sendsms) {
                        messageService.sendPayoff(receiveTx)
                    }
                    redirect url: "/member/show/${period.contract.member.id}"
                }
                catch (e) {
                    println e
                    flash.error = e.message
                    render view: '/contract/payoff', model: [receiveTx: receiveTx, member: member, contract: contract, period: period, amount: payAmount, fine: fine, isShareCapital: isShareCapital]
                }
            }
            else {
                redirect url: '/error'
            }
        }
    }

}
