package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MessageService)
class MessageServiceTests {

    void testSend() {
    	def sendCounter = 0,
            member = [
                telNo: '66841291342'
            ]

        service.openmsngrClientService = [
        	sendMessage: { msisdn, message -> ++sendCounter }
        ]

        service.metaClass.makeMSISDN = { n -> '66841291342' }

        service.send(member, 'Hello, sms')
        assert sendCounter == 1
    }

    void testSendApproved() {
        def contract = [
            member: [ id: 1 ],
            loanAmount: 2000.00,
            loanType: [ name: "Common" ],
            approvalDate: new Date()
        ]

        def counter = 0
        service.messageSource = [
            getMessage: { code, args, default_message, locale -> "you get a message, lucky man" }
        ]
        service.metaClass.send = { member, message -> ++counter }

        service.sendApproved(contract)
        assert counter == 1
    }

    void testSendPayoff() {
        def receiveTx = [
            period: [
                id: 1,
                no: 3,
                dueDate: new Date(),
                contract: [
                    member: [ id: 1 ],
                    loanAmount: 2000.00,
                    loanType: [ name: "Common" ],
                    approvalDate: new Date()
                ]
            ],
            amount: 300.00
        ]

        def counter = 0
        service.messageSource = [
            getMessage: { code, args, default_message, locale -> "you get a message, lucky man" }
        ]
        service.metaClass.send = { member, message -> ++counter }

        service.sendPayoff(receiveTx)
        assert counter == 1
    }

    void testIsMobileNumber() {
        assert service.isMobileNumber("020010002") == false
        assert service.isMobileNumber("053001002") == false
        assert service.isMobileNumber("66020010002") == false
        assert service.isMobileNumber("6653001002") == false
        assert service.isMobileNumber("66840010002") == true
        assert service.isMobileNumber("+66840010002") == true
    }

    void testMakeMSISDN() {
        def ret = false
        service.metaClass.isMobileNumber = { n -> ret }
        assert service.makeMSISDN("020010002") == null
        assert service.makeMSISDN("053001002") == null

        ret = true
        assert service.makeMSISDN("0840010002") == "66840010002"
        assert service.makeMSISDN("0841291342") == "66841291342"
    }
}
