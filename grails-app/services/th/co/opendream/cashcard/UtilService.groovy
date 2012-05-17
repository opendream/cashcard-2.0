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


		if (step4 == check_digit) {
			true
		}
		else {
			false
		}
    }

    def isPayable(Contract contract) {
    	contract.approvalStatus
    }
}
