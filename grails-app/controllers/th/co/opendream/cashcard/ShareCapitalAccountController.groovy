package th.co.opendream.cashcard

class ShareCapitalAccountController {
	def springSecurityService
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
}
