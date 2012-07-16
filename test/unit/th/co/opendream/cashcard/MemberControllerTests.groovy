package th.co.opendream.cashcard

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

import grails.converters.JSON


import th.co.opendream.cashcard.Member.Gender
import th.co.opendream.cashcard.Member.Status

import org.codehaus.groovy.grails.web.servlet.mvc.SynchronizerTokensHolder
/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Mock([Member, LoanType, Contract])
class MemberControllerTests {
    def utilService

    @Before
    void setUp() {
        def m1 = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream", memberNo: "001")
        def m2 = new Member(identificationNumber: "1234567891234", firstname: "Noomz", lastname: "Siriwat", telNo: "111111111", gender: "MALE", address: "Opendream2", memberNo: "002")

        utilService = [ check_id_card: { id -> true } ] as UtilService

        m1.utilService = utilService
        m2.utilService = utilService

        m1.save()
        m2.save()

        assert Member.count() == 2
    }

    void testIndex() {
        controller.index()
        response.redirectedUrl == 'member/list'
    }

    def populateValidParams(params) {
        params.identificationNumber = '1159900100015'
        params.firstname = 'Nat'
        params.lastname = 'Weerawan'
        params.telNo = '0818526122'
        params.address = '162/37'
    }

    void testSaveInvalidMember() {
        controller.runNoService = [
            next: { key -> "001" }
        ]
        controller.save()

        assert model.memberInstance != null
        assert view == '/member/create'
    }

    void testCreate() {
        params.firstname = "Nat"
        params.lastname = "Weerawan"
        def model = controller.create()
        assert model.memberInstance.firstname == "Nat"
        assert model.memberInstance.lastname == "Weerawan"
    }

    void testSaveValidMember() {
        populateValidParams(params)
        Member.metaClass.save = { -> delegate.id = 3 }

        def count = 0

        controller.shareCapitalAccountService = [
            createAccountFromMember: { a, b, c -> count++ }
        ]

        controller.runNoService = [
            next: { key -> "001" }
        ]

        controller.save()
        assert count == 1
        assert response.redirectedUrl == '/member/show/3'
    }

    void testListMember() {
        controller.list()
        assert Member.list().size() == model.memberList?.size()
        assert view == '/member/list'

        response.reset()
        params.identificationNumber = "11"
        params.firstname = "Nat"
        params.lastname = "Wee"
        params.telNo = "11"
        controller.list()

        assert view == '/member/list'
        assert model.memberList?.size() == 1
    }

    void testVerifyMemberWithValidCardId() {
        params.cardId = "1159900100015"
        controller.verifyCard()

        assert response.redirectedUrl == '/member/show/1'
    }

    void testVerifyMemberWithInvalidCardId() {
        controller.utilService = utilService
        controller.verifyCard()
        assert view == '/member/verifyCard'

        response.reset()
        params.cardId = '1159900100015'
        Member.metaClass.'static'.findByIdentificationNumber = { s -> null }
        controller.verifyCard()
        assert view == '/member/verifyCard'
    }

    void testShow() {
    	params.id = '1'

        // Mocking
        controller.shareCapitalAccountService = [
            getMemberAccount: { m -> true }
        ]

    	controller.show()
    	assert view == '/member/show'
    }

    void testShowMemberWithoutId() {
        controller.show()
        assert response.redirectedUrl == '/member/list'
    }

    void testUpdateWithOutdatedVersionNumber() {
        def m1 = Member.get(2)
        m1.version = 1
        m1.save()

        populateValidParams(params)
        params.id = 2
        params.version = -2

        controller.update()

        assert view == '/member/edit'
        assert model.memberInstance != null
        assert model.memberInstance.errors.getFieldError('version')
    }

    void testUpdateWithNoVersion() {
        populateValidParams(params)
        params.id = 1

        controller.update()
        response.redirectedUrl == 'member/show/1'

    }

    void testEditInvalidMemberAndInvalidToken() {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_KEY] = null
        controller.edit()
        assert view == '/member/edit'
    }

    void testEditInvalidMemberValidToken() {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/member/edit'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])

        controller.edit()
        assert response.redirectedUrl == '/error'
    }

    void testEditValidMember() {
        params.id = 1
        controller.edit()

        assert view == '/member/edit'
    }

    void testEditButFailToSave() {
        Member.metaClass.save = { o -> assert 4==3 }
        controller.edit()
        assert view == '/member/edit'
    }

    void testUpdateInvalidMember() {
        controller.update()

        assert response.redirectedUrl == '/member/list'
    }

    void testToString() {
        def m1 = Member.get(1)
        assert m1.toString() == "${m1.firstname} ${m1.lastname}"
    }

    void testAjaxSearch() {
        def output = [ [id: 1, name: "สมหญิง"], [id: 2, name: "สมชาย"]]

        params.name = "สม"
        controller.memberService = [
            search: { name -> output }
        ]

        def result = controller.ajaxSearch()

        def json_output = output as JSON
        assert json_output.toString() == response.text
    }

    void testUploadMembers() {
        controller.memberService = [findUploadMembersFilename: {'member.csv'} ]
        controller.uploadMembers()
        assert view == '/member/uploadMembers'
        assert model.filename == 'member.csv'
    }

    void testUploadMembersWithoutFilename() {
        controller.memberService = [findUploadMembersFilename: {null} ]
        controller.uploadMembers()
        assert view == '/member/uploadMembers'
        assert model.filename == null
    }

    void testPrintMemberCard() {
        def printStatus
        def reportPath
        controller.memberService = [getMemberByMemberIds: {List memberIds ->
                                        [ [id:1, firstname:'one', lastname:'lastone', 
                                        identificationNumber:'1234567890121', creditUnionMemberNo:'110-00001-0001'],
                                        [id:2, firstname:'two', lastname:'lasttwo', 
                                        identificationNumber:'1234567890122', creditUnionMemberNo:'110-00001-0002'],
                                        [id:3, firstname:'three', lastname:'lastthree', 
                                        identificationNumber:'1234567890123', creditUnionMemberNo:'110-00001-0003']]
                                    } ]
        controller.metaClass.chain = {Map map -> 
                                        if(map.model.data != null) {
                                            printStatus = true
                                            reportPath = map.params.report_path
                                        } else {
                                            printStatus = false
                                            reportPath = map.params.report_path
                                        }

                                    }
        
        params.memberIds = ['1','2','3']
        controller.printMemberCard()

        assert true == printStatus
        assert '/reports' == reportPath

        params.memberIds = []
        controller.memberService = [getMemberByMemberIds: {List memberIds -> null} ]
        controller.printMemberCard()

        assert false == printStatus
        assert '/reports' == reportPath
    }
}
