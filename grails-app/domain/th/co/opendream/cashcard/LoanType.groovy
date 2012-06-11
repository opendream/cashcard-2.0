package th.co.opendream.cashcard

class LoanType {
	String name
	String processor
	String interestProcessor
	String periodProcessor
	String periodGeneratorProcessor
	BigDecimal cooperativeShare = 0.00 // Percent ที่ได้คือ %ของเงินที่จ่ายจริง
	BigDecimal interestRate = 0.00
	BigDecimal maxInterestRate = 18.00
	Boolean mustKeepAdvancedInterest = false
	Integer numberOfPeriod


    static constraints = {
    	name nullable: false, blank: false, unique: true
    	processor nullable: false, blank: false, inList: ["Effective", "Commission", "Flat"]
		interestProcessor blank: false
		periodProcessor blank: false
		periodGeneratorProcessor blank: false
    }
}
