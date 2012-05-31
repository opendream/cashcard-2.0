package th.co.opendream.cashcard

class PrintoutService {

    def getPayoffPrintout(transId) {
    	def result = [:]    	
    	def trans = ReceiveTransaction.get(transId)
    	def member = trans.period.contract.member
    	result.identificationNumber = member.identificationNumber
    	result.memberName = member.toString()
    	result.loanType = trans.period.contract.loanType.name
    	result.paymentDate = trans.paymentDate
    	result.amount = trans.amount
    	result.peroidNo = trans.period.no
    	result.peroidBalance = trans.period.amount - trans.amount
    	result
    }
}
