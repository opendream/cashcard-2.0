package th.co.opendream.cashcard

class PrintoutService {

    def getPayoffPrintout(transId) {
    	def result = [:]
    	def trans = ReceiveTransaction.get(transId)
    	def member = trans.period.contract.member

    	result.identificationNumber = member.identificationNumber
        result.creditUnionMemberNo  = member.creditUnionMemberNo
    	result.member = member.toString()
    	result.loanType = trans.period.contract.loanType.name
    	result.contractCode = trans.period.contract.code
    	result.code = trans.id
    	result.paymentDate = trans.paymentDate
    	result.amount = trans.amount
    	result.periodNo = trans.period.no
    	result.periodAmount = trans.period.amount
    	result.periodBalance = trans.period.amount - trans.amount
    	result
    }

    def getPayloanPrintout(contractId) {
        def result = [:]
        def contract = Contract.get(contractId)

        def member = contract.member

        result.identificationNumber = member.identificationNumber
        result.creditUnionMemberNo = member.creditUnionMemberNo
        result.loanAmount = contract.loanAmount
        result.member = member.toString()
        result.contractCode = contract.code
        result.loanType = contract.loanType.name
        result.numberOfPeriod = contract.numberOfPeriod
        result.code = contract.id
        result.payloanDate = contract.payloanDate

        result
    }
}
