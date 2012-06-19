package th.co.opendream.cashcard

import grails.test.mixin.*
import org.junit.*

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

abstract class DomainTestTemplate {

    def mustTestProperties = true

    def requiredProperties() {

    }

    def domainClass() {

    }

    void testProperties() {
        if (mustTestProperties) {
            def defaultDomainClass = new DefaultGrailsDomainClass(domainClass())

            def domainProperties = defaultDomainClass.persistentProperties*.name
            def missing_properties = requiredProperties() - domainProperties
            assert 0 == missing_properties.size(),
                "Domain class is missing some required properties => ${missing_properties}"
        }
    }

    void verifyNotNull(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail null validation.",
            "nullable", instance.errors[field]
    }

    void verifyNotBlank(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail blank validation.",
            "blank", instance.errors[field]
    }

    void verifyUnique(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail unique validation.",
            "unique", instance.errors[field]
    }

    void verifyPass(instance, field) {
        assertTrue "${field} value = ${instance[field]} must pass all validations.",
            instance.validate([field])
    }

    void verifyFinanceNumber(clazz, field) {
        mockForConstraintsTests(clazz)

        def _test = { fieldName ->
            def instance = clazz.newInstance()

            instance[fieldName] = null
            verifyNotNull(instance, fieldName)

            instance[fieldName] = 1000.000000
            verifyPass(instance, fieldName)
        }

        if (!(field instanceof ArrayList)) {
            field = [field]
        }

        field.each {
            _test(it)
        }
    }

    // Generator

    Member generateValidMember() {
        def utilService = [
            check_id_card: { id -> true }
        ]

        mockDomain(Member)
        def m = new Member(
            identificationNumber:'1411900088198',
            firstname: 'Siriwat',
            lastname: 'Uamngamsup',
            gender: Member.Gender.MALE,
            telNo: '0841291342',
            address: "299/99 Areeya Mandarina\nSuthisarn Vinijchai Rd.\nSamsen Nok Huaykwang\nBangkok 10310",
            balance: 0.000000,
            status: Member.Status.ACTIVE,
            memberNo: '001'
        )
        m.utilService = utilService
        m.save()
        return m
    }

   def generateValidContract(type='Effective') {
        def utilService = [
            check_id_card: { id -> true }
        ]

        mockDomain(Member)
        def m1 = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
        def m2 = new Member(identificationNumber:"3710600357102", firstname:"สม", lastname: "ขำคม", telNo: "0818526122", gender: "MALE", address: "Opendream")

        m1.utilService = utilService
        m2.utilService = utilService

        [m1, m2].each { it.save() }

        assert LoanType.count() == 0

        mockDomain(LoanType)
        def loanType = new LoanType(name: "Commission", processor: "Commission", numberOfPeriod: 3)
        loanType.cooperativeShare = 0.80
        loanType.save()

        mockDomain(Contract)
        def contract = new Contract(
            code: "ก.55-1000-20",
            member: m1,
            loanType: loanType,
            cooperativeShare: loanType.cooperativeShare,
            processor: "Effective",
            interestProcessor: "Effective",
            periodProcessor: "Effective",
            periodGeneratorProcessor: "Effective",
            loanAmount: 2000.00,
            interestRate: 2.00,
            loanBalance: 0.00,
            approvalStatus: false,
            loanReceiveStatus: false,
            guarantor1: "Keng",
            guarantor2: "Neung",
            _guarantor1: m1,
            _guarantor2: m2,
            numberOfPeriod: 3,
            approvalDate: null,
        )

        def getContact = { ->
            contract
        }

        def saveContacts = { ->
            contract.save()
            contract
        }

        return [ contract: getContact , save: saveContacts ]
    }

    void setUpPeriod(type='Effective') {
        mockFor(LoanType)
        def loanType = new LoanType(name: 'A', processor: type, interestProcessor: type, periodProcessor: type, periodGeneratorProcessor: type)
        loanType.save()


        mockFor(Member)
        def member = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
        member.utilService = [
            check_id_card: { id -> true }
        ]
        member.save()

        mockFor(Contract)
        def contract = generateValidContract().contract()
            contract.id = 1
            contract.code = "ก.55-1000-20"
            contract.member = member
            contract.loanType = loanType
            contract.processor = type
            contract.interestProcessor = loanType.interestProcessor
            contract.periodProcessor = loanType.periodProcessor
            contract.periodGeneratorProcessor = loanType.periodGeneratorProcessor
            contract.loanAmount = 2000.00
            contract.interestRate = 24.00
            contract.cooperativeShare = 0.75 // For Commission
            contract.loanBalance = 2000.00
            contract.approvalStatus = false
            contract.loanReceiveStatus = false
            contract._guarantor1 = member
            contract._guarantor2 = member
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

    void setUpPeriodFlat() {
        def loanType = new LoanType(name: 'Common', processor: 'Flat', interestProcessor: 'Flat', periodProcessor: 'Flat', periodGeneratorProcessor: 'Flat')
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
            processor: "Flat",
            interestProcessor: "Flat",
            periodProcessor: "Flat",
            periodGeneratorProcessor: "Flat",
            loanAmount: 2000.00,
            interestRate: 24.00,
            loanBalance: 2000.00,
            approvalStatus: false,
            loanReceiveStatus: false,
            guarantor1: "Keng",
            guarantor2: "Neung",
            _guarantor1: member,
            _guarantor2: member,
            numberOfPeriod: 3
        )
        contract.save()
    }

}