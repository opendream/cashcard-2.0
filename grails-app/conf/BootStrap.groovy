import th.co.opendream.cashcard.Member
<<<<<<< HEAD
import th.co.opendream.cashcard.RequestMap
import th.co.opendream.cashcard.Role
import th.co.opendream.cashcard.UsersRole
import th.co.opendream.cashcard.Users

import groovy.time.*
import static java.util.Calendar.*
import grails.util.Environment
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import th.co.opendream.cashcard.LoanType
import th.co.opendream.cashcard.Contract

class BootStrap {

    def init = { servletContext ->
    	def m1 = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
    	def m2 = new Member(identificationNumber: "1234567891234", firstname: "Noomz", lastname: "Siriwat", telNo: "111111111", gender: "MALE", address: "Opendream2")

    	m1.save()
    	m2.save()

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
        generateContract(m1, LoanType.get(1))
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
        new RequestMap(url: '/*', configAttribute: 'IS_AUTHENTICATED_ANONYMOUSLY').save()
        new RequestMap(url: '/user/**', configAttribute: 'ROLE_ADMIN').save()
        new RequestMap(url: '/role/**', configAttribute: 'ROLE_ADMIN').save()
        new RequestMap(url: '/company/**', configAttribute: 'ROLE_ADMIN').save()
        new RequestMap(url: '/member/**', configAttribute: 'ROLE_USER,ROLE_COUNTER').save()
        new RequestMap(url: '/member/payment/**', configAttribute: 'ROLE_COUNTER').save()
        new RequestMap(url: '/member/pay/**', configAttribute: 'ROLE_COUNTER').save()
        new RequestMap(url: '/member/withdraw/**', configAttribute: 'ROLE_COUNTER').save()
        new RequestMap(url: '/interestRate/**', configAttribute: 'ROLE_USER,ROLE_COUNTER').save()
        new RequestMap(url: '/report/**', configAttribute: 'ROLE_USER,ROLE_COUNTER').save()
        new RequestMap(url: '/console/**', configAttribute: 'ROLE_ADMIN').save()
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
