import th.co.opendream.cashcard.Member
import th.co.opendream.cashcard.LoanType
import th.co.opendream.cashcard.Contract

class BootStrap {

    def init = { servletContext ->
    	def m1 = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
    	def m2 = new Member(identificationNumber: "1234567891234", firstname: "Noomz", lastname: "Siriwat", telNo: "111111111", gender: "MALE", address: "Opendream2")

    	m1.save()
    	m2.save()

    	generateLoanType()
        generateContract(m1, LoanType.get(1))
    }
    def destroy = {
    }

    def generateLoanType() {
    	new LoanType(name: "เงินกู้สามัญ").save()
    	new LoanType(name: "เงินกู้เพื่อการศึกษา").save()
    	new LoanType(name: "เงินกู้ซื้อยานพาหนะ").save()
    	new LoanType(name: "เงินกู้ซื้อทอง").save()
    	new LoanType(name: "เงินกู้ซื้เครื่องใช้ไฟฟ้า").save()
    	new LoanType(name: "เงินกู้โดยอสังหาริมทรัพย์").save()
    	new LoanType(name: "เงินกู้โดยใช้ทรัพย์สินจำนอง").save()
    }

    def generateContract(member, loanType) {
        new Contract(
            code: "ก.55-1000-20",
            member: member,
            loanType: loanType,
            loanAmount: 2000.00,
            interestRate: 2.00,
            loanBalance: 2000.00,
            approvalStatus: false,
            loanReceiveStatus: false,
            guarantor1: "Keng",
            guarantor2: "Neung",
            numberOfPeriod: 3
        ).save()
    }
}
