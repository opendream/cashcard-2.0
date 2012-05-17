package th.co.opendream.cashcard

class PeriodService {

    def generatePeriod(amount, numberOfPeriod) {
    	amount = amount as BigDecimal

    	def amountPerPeriod = (int)(amount / numberOfPeriod),
    		remain = ((int)amount) % numberOfPeriod

	    (0..<numberOfPeriod).collect {
	    		new Period(amount: amountPerPeriod + (it == numberOfPeriod - 1 ? remain : 0), no: it + 1)
	    }
	}
}
