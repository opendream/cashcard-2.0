package th.co.opendream.cashcard

class LoanType {
	String name
	String processor

    static constraints = {
    	name nullable: false, blank: false, unique: true
    	processor nullable: false, blank: false, inList: ["Effective", "Hybrid", "Flat"]
    }
}
