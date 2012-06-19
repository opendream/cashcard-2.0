package th.co.opendream.cashcard

class ShareCapitalAccountController {
	def springSecurityService,
        shareCapitalAccountService

    def index() { }

    def create() {
    	def member = Member.get(params.id)

    	if (member) {
    		render view: 'create', model: [member: member]
    	}
    	else {
    		redirect url: '/error'
    	}
    }

    def doCreate() {
    	def member = Member.get(params.id)

    	if (member) {
    		def shareCapitalAccount = new ShareCapitalAccount()
    		shareCapitalAccount.createdBy = Users.get(springSecurityService.principal.id)
    		shareCapitalAccount.member = member

    		shareCapitalAccount.balance = params.balance ? params.balance as BigDecimal : 0
    		if (shareCapitalAccount.balance < 0) {
    			return render( view: 'create', model: [member: member, shareCapitalAccount: shareCapitalAccount])
    		}
    		shareCapitalAccount.registeredDate = params.registeredDate
    		shareCapitalAccount.accountNumber = params.accountNumber
    		if (!shareCapitalAccount.save()) {
    			return render( view: 'create', model: [member: member, shareCapitalAccount: shareCapitalAccount])
    		}
    		redirect controller: 'member', action: 'show', id: member.id
    	}
    	else {
    		redirect url: '/error'
    	}
    }

    def deposit() {
        def member = Member.get(params.id)

        if (member) {
            def accountTx = new AccountTransaction()
            def shareCapitalAccount = shareCapitalAccountService.getMemberAccount(member)
            render view: 'deposit', model: [member: member, accountTx: accountTx, shareCapitalAccount: shareCapitalAccount]
        }
        else {
            redirect url: '/error'
        }
    }

    def doDeposit() {
        def member = Member.get(params.id)

        if (member) {
            def accountTx = new AccountTransaction(params)
            def shareCapitalAccount = shareCapitalAccountService.getMemberAccount(member)

            def amount = params.amount as BigDecimal,
                paymentDate = params.paymentDate

            if (!shareCapitalAccountService.deposit(shareCapitalAccount, amount, paymentDate)) {
                render view: 'deposit', model: [member: member, accountTx: accountTx, shareCapitalAccount: shareCapitalAccount]
            }

            redirect controller: 'member', action: 'show', id: member.id
        }
        else {
            redirect url: '/error'
        }
    }
}
