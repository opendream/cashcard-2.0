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

class BootStrap {
    def grailsApplication
    def init = { servletContext ->
        if (Users.count() != 0) {
            return
        }

    	def m1 = new Member(identificationNumber:"1159900100015", firstname:"สมหญิง", lastname: "รักเรียน", telNo: "0818526122", gender: "MALE", address: "Opendream")

    	m1.save()

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
        //generateContract(m1, LoanType.get(1))

        //asign roport realPath to system

        def reports = grailsApplication.config.jasper.dir.reports
        def realPath =  servletContext.getRealPath(reports)
        grailsApplication.config.jasper.dir.reports = realPath

        def kettle = grailsApplication.config.kettle.repository.path
        grailsApplication.config.kettle.repository.path = servletContext.getRealPath(kettle)
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
            periodGeneratorProcessor: "ExpressCash01"
        ).save()
    }

    def generateContract(member, loanType) {
        /*
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
        */
    }
}
