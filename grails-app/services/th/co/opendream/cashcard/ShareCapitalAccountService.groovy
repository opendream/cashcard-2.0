package th.co.opendream.cashcard

class ShareCapitalAccountService {

    // Service
    def springSecurityService

    def createAccountFromMember(memberInstance, memberAccountNumber, date) {
        def shareCapitalAccount = new ShareCapitalAccount()

        shareCapitalAccount.with {
            member = memberInstance
            createdBy = Users.get(springSecurityService.principal.id)
            balance = 0.00
            registeredDate = date
            accountNumber = memberAccountNumber
        }

        shareCapitalAccount.save()
    }
}
