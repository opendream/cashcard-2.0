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

    def getMemberAccount(memberInstance) {
        def c = ShareCapitalAccount.createCriteria()
        def account = c.get() {
            eq("member", memberInstance)
        }
    }

    def deposit(account, amount, date = new Date()) {
        def updatedBalance = account.balance + amount
        // Save transaction.
        def accountTx = new AccountTransaction()

        accountTx.amount = amount
        accountTx.balanceForward = account.balance
        accountTx.balance = updatedBalance
        accountTx.paymentDate = date
        accountTx.sign = +1

        accountTx.save()

        // Update account balance
        account.balance = updatedBalance
        account.save()
    }
}
