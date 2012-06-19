import th.co.opendream.cashcard.Member
import th.co.opendream.cashcard.RequestMap
import th.co.opendream.cashcard.Role
import th.co.opendream.cashcard.UsersRole
import th.co.opendream.cashcard.Users

import groovy.time.*
import static java.util.Calendar.*
import grails.util.Environment

import th.co.opendream.cashcard.LoanType
import th.co.opendream.cashcard.Contract
import th.co.opendream.cashcard.Period
import th.co.opendream.cashcard.ShareCapitalAccount
import th.co.opendream.cashcard.RunNo

class BootStrap {
    def grailsApplication
    def init = { servletContext ->

        def currentEnv = Environment.current

        if (currentEnv == Environment.PRODUCTION && Users.count() != 0) {
            return
        }

        def (m1, m2) = generateMember()

        def user = new Users(username:'admin', password:'password',
                                , enabled:true, accountExpired:false, accountLocked:false, passwordExpired:false).save()

        def roleAdmin = new Role(authority:'ROLE_ADMIN').save(failOnError: true)
        def roleCounter = new Role(authority:'ROLE_COUNTER').save(failOnError: true)
        def roleUser = new Role(authority:'ROLE_USER').save(failOnError: true)
        UsersRole.create(user, roleAdmin)
        new UsersRole(user:user, role:roleCounter).save(failOnError: true)
        new UsersRole(user:user, role:roleUser).save(failOnError: true)
        /*
        */
        createRequestMaps();

        generateLoanType()

        //asign roport realPath to system

        def reports = grailsApplication.config.jasper.dir.reports
        def realPath =  servletContext.getRealPath(reports)
        grailsApplication.config.jasper.dir.reports = realPath

        def kettle = grailsApplication.config.kettle.repository.path
        grailsApplication.config.kettle.repository.path = servletContext.getRealPath(kettle)

        development {
            def contract = generateContract(m1, LoanType.list().last())
            contract.save()

            generatePeriod(contract)
            generateShareCapitalAccount(m1, user)
            generateShareCapitalAccount(m2, user)
            generateMemberRunningNumberConfig('Member')
        }
    }
    def destroy = {

    }

    def createRequestMaps() {
        new RequestMap(url: '/js/**', configAttribute: 'IS_AUTHENTICATED_ANONYMOUSLY').save()
        new RequestMap(url: '/css/**', configAttribute: 'IS_AUTHENTICATED_ANONYMOUSLY').save()
        new RequestMap(url: '/images/**', configAttribute: 'IS_AUTHENTICATED_ANONYMOUSLY').save()
        new RequestMap(url: '/login/**', configAttribute: 'IS_AUTHENTICATED_ANONYMOUSLY').save()
        new RequestMap(url: '/logout/**', configAttribute: 'IS_AUTHENTICATED_ANONYMOUSLY').save()
        new RequestMap(url: '/j_spring_security_switch_user',
                       configAttribute: 'ROLE_SWITCH_USER,IS_AUTHENTICATED_FULLY').save()
        new RequestMap(url: '/*', configAttribute: 'ROLE_COUNTER').save()
        new RequestMap(url: '/user/**', configAttribute: 'ROLE_ADMIN').save()
        new RequestMap(url: '/role/**', configAttribute: 'ROLE_ADMIN').save()
        new RequestMap(url: '/company/**', configAttribute: 'ROLE_ADMIN').save()
        new RequestMap(url: '/member/**', configAttribute: 'ROLE_USER,ROLE_COUNTER').save()
        new RequestMap(url: '/member/list/**', configAttribute: 'ROLE_USER,ROLE_COUNTER').save()
        new RequestMap(url: '/member/payment/**', configAttribute: 'ROLE_COUNTER').save()
        new RequestMap(url: '/member/pay/**', configAttribute: 'ROLE_COUNTER').save()
        new RequestMap(url: '/member/withdraw/**', configAttribute: 'ROLE_COUNTER').save()
        new RequestMap(url: '/interestRate/**', configAttribute: 'ROLE_USER,ROLE_COUNTER').save()
        new RequestMap(url: '/report/**', configAttribute: 'ROLE_USER,ROLE_COUNTER').save()
        new RequestMap(url: '/console/**', configAttribute: 'ROLE_ADMIN').save()
    }

    def generateMember() {
        def m1 = new Member(identificationNumber:"1159900100015", firstname:"สมหญิง", lastname: "รักเรียน", telNo: "0818526122", gender: "MALE", address: "Opendream", memberNo: "001")
        def m2 = new Member(identificationNumber:"3710600357102", firstname:"สม", lastname: "ขำคม", telNo: "0818526122", gender: "MALE", address: "Opendream", memberNo: "002")

        m1.save()
        m2.save()

        [m1, m2]
    }

    def generateLoanType() {
    	new LoanType(
            name: "เงินกู้สามัญ", processor: "Effective", interestRate: 12.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: false,
            numberOfPeriod: 3, interestProcessor: "Effective", periodProcessor: "Effective",
            periodGeneratorProcessor: "Effective"
        ).save()
    	new LoanType(
            name: "เงินกู้เพื่อการศึกษา", processor: "Effective", interestRate: 6.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: false,
            numberOfPeriod: 3, interestProcessor: "Effective", periodProcessor: "Effective",
            periodGeneratorProcessor: "Effective"
        ).save()
    	new LoanType(
            name: "เงินกู้ซื้อยานพาหนะ", processor: "Flat", interestRate: 12.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: true,
            numberOfPeriod: 24, interestProcessor: "Flat", periodProcessor: "Flat",
            periodGeneratorProcessor: "Flat"
        ).save()
    	new LoanType(
            name: "เงินกู้ซื้อทอง", processor: "Effective", interestRate: 12.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: false,
            numberOfPeriod: 3, interestProcessor: "Effective", periodProcessor: "Effective",
            periodGeneratorProcessor: "Effective"
        ).save()
    	new LoanType(
            name: "เงินกู้ซื้อเครื่องใช้ไฟฟ้า", processor: "Effective", interestRate: 12.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: false,
            numberOfPeriod: 3, interestProcessor: "Effective", periodProcessor: "Effective",
            periodGeneratorProcessor: "Effective"
        ).save()
    	new LoanType(
            name: "เงินกู้โดยอสังหาริมทรัพย์", processor: "Commission",interestRate: 24.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: true,
            numberOfPeriod: 24, interestProcessor: "Commission", periodProcessor: "Commission",
            periodGeneratorProcessor: "Commission"
        ).save()
    	new LoanType(
            name: "เงินกู้โดยใช้ทรัพย์สินจำนอง", processor: "Commission", interestRate: 36.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: false,
            numberOfPeriod: 24, interestProcessor: "Commission", periodProcessor: "Commission",
            periodGeneratorProcessor: "Commission"
        ).save()
        new LoanType(
            name: "เงินกู้ด่วน", processor: "Effective", interestRate: 18.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: false,
            numberOfPeriod: 3, interestProcessor: "Effective", periodProcessor: "Effective",
            periodGeneratorProcessor: "Effective"
        ).save()
        new LoanType(
            name: "เงินกู้ด่วน (ปรับปรุงใหม่)", processor: "Effective", interestRate: 12.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: false,
            numberOfPeriod: 3, interestProcessor: "Effective", periodProcessor: "ExpressCash01",
            periodGeneratorProcessor: "ExpressCash01", canPayAllDebt: true
        ).save(flush: true) // FIXME: Don't know why it must use flush:true with postgres
    }

    def generateContract(member, loanType) {
        new Contract(
            id: 1,
            code: "ก.55-1000-20",
            processor: "Express",
            interestProcessor: loanType.interestProcessor,
            periodProcessor: loanType.periodProcessor,
            periodGeneratorProcessor: loanType.periodGeneratorProcessor,
            member: member,
            loanType: loanType,
            loanAmount: 2000.00,
            interestRate: 12.00,
            maxInterestRate: 18.00,
            loanBalance: 2000.00,
            approvalStatus: false,
            loanReceiveStatus: false,
            guarantor1: "Keng",
            guarantor2: "Neung",
            numberOfPeriod: 3,
            signedDate: Date.parse("yyyy-MM-dd", "2012-03-01"),
            canPayAllDebt: loanType.canPayAllDebt
        )
    }

    def generatePeriod(contract) {
        new Period(
            id: 1, contract: contract, amount: 686.00, no: 1,
            dueDate: Date.parse("yyyy-MM-dd", "2012-04-01"), status: true,
            payoffStatus: false, interestAmount: 20.00, interestOutstanding: 20.00,
            outstanding: 686.00
        ).save()

        new Period(
            id: 2, contract: contract, amount: 686.00, no: 2,
            dueDate: Date.parse("yyyy-MM-dd", "2012-05-01"), status: true,
            payoffStatus: false, interestAmount: 20.00, interestOutstanding: 20.00,
            outstanding: 686.00
        ).save()

        new Period(
            id: 3, contract: contract, amount: 686.00, no: 3,
            dueDate: Date.parse("yyyy-MM-dd", "2012-06-01"), status: true,
            payoffStatus: false, interestAmount: 20.00, interestOutstanding: 20.00,
            outstanding: 686.00
        ).save()
    }

    def generateShareCapitalAccount(member, user) {
        def account = new ShareCapitalAccount(
            accountNumber: "55-${member.id}",
            member: member,
            createdBy: user,
            balance: 1000.00,
            registeredDate: new Date()
        )

        account.save()

        account
    }

    def generateMemberRunningNumberConfig(key) {
        def runno = new RunNo(
            key: key,
            description: '',
            currentNo: 0,
            padSize: 4
        )

        runno.save()
        runno
    }
}
