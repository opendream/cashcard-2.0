package th.co.opendream.cashcard

class UtilService {
    def check_id_card(id) {
		def check_digit = id[-1] as Integer
		def mulList = []
		def sum = 0
		def step4

		//Collect
		id.reverse().toList().eachWithIndex { it, i ->
		  mulList << it.toInteger() * (i+1)
		}

		sum = mulList[1..-1].sum()
		step4 = 11-sum%11

		// number in step 4 has 2 digits, then get only the first digit.
		if (step4 > 9) {
			step4 -= 10
		}

		if (step4 == check_digit && id.size() == 13) {
			true
		}
		else {
			false
		}
    }

    def isPayable(contract) {
    	contract.approvalStatus && !contract.loanReceiveStatus
    }

    def moneyRoundUp(amount) {
		[0.00, 0.25, 0.50, 0.75, 1.00].collect { it + (amount as BigInteger) }.find { it >= amount }
	}
}
