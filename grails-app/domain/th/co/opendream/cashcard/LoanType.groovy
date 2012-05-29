package th.co.opendream.cashcard

class LoanType {
	String name
	String processor
	BigDecimal interestRate = 0.00
	BigDecimal maxInterestRate = 18.00
	Boolean mustKeepAdvancedInterest = false
	Integer numberOfPeriod


    static constraints = {
    	name nullable: false, blank: false, unique: true
    	processor nullable: false, blank: false, inList: ["Effective", "Commission", "Flat"]
    }
}
