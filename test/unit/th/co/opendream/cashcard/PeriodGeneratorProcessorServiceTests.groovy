package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PeriodGeneratorProcessorService)
@Mock([LoanType, Member, Period, Contract])
class PeriodGeneratorProcessorServiceTests {
    @Before
    void setUp() {
    }

    /***********************************
     * Effective
     **********************************/
    void setUpPeriod(type='Effective') {
        def loanType = new LoanType(name: 'Common', processor: type)
        loanType.save()

        def member = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
        member.utilService = [
            check_id_card: { id -> true }
        ]
        member.save()

        def contract = new Contract(
            id: 1,
            code: "ก.55-1000-20",
            member: member,
            loanType: loanType,
            loanAmount: 2000.00,
            cooperativeShare: 0.75,
            interestRate: 24.00,
            loanBalance: 2000.00,
            approvalStatus: false,
            loanReceiveStatus: false,
            guarantor1: "Keng",
            guarantor2: "Neung",
            numberOfPeriod: 3
        )
        contract.save()

        mockDomain(Period, [
            [id: 1, contract: contract, amount: 706.00, no: 1,
             dueDate: new Date().plus(10), status: true, payoffStatus: true],
            [id: 2, contract: contract, amount: 706.00, no: 2,
             dueDate: new Date().plus(20), status: true, payoffStatus: false],
            [id: 3, contract: contract, amount: 708.00, no: 3,
             dueDate: new Date().plus(30), status: true, payoffStatus: false]
        ])
    }

/*
    void testEffective() {
        def amount = 2000,
            numberOfPeriod = 3;
        def periodList = service.effective(amount, numberOfPeriod)
        periodList.each {
            println it.dump()
        }
    }
    */

    void testGenerateEffective() {
        def type = 'Effective'
        def loanType = new LoanType(name: 'Common', processor: 'Effective', interestRate: 24.00)

        def amount = 2000,
            numberOfPeriod = 3;


        def generated = service.generate(loanType, amount, numberOfPeriod)
        def effected = service.effective(amount, numberOfPeriod, loanType.interestRate)

        assert (generated*.amount).equals(effected*.amount)
    }
}
