package th.co.opendream.cashcard

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*


import th.co.opendream.cashcard.Member.Gender
import th.co.opendream.cashcard.Member.Status


/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Mock([Member])
class MemberControllerTests {
    def utilService

    @Before
    void setUp() {
        def m1 = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
        def m2 = new Member(identificationNumber: "1234567891234", firstname: "Noomz", lastname: "Siriwat", telNo: "111111111", gender: "MALE", address: "Opendream2")

        utilService = [ check_id_card: { id -> true } ] as UtilService

        m1.utilService = utilService
        m2.utilService = utilService

        m1.save()
        m2.save()

        assert Member.count() == 2
    }

    def populateValidParams(params) {
        params.identificationNumber = '1159900100015'
        params.firstname = 'Nat'
        params.lastname = 'Weerawan'
        params.telNo = '0818526122'
        params.address = '162/37'
    }

    void testSaveInvalidMember() {
        controller.save()

        assert model.memberInstance != null
        assert view == '/member/create'
    }

    void testSaveValidMember() {
        populateValidParams(params)
        Member.metaClass.save = { -> delegate.id = 3 }
        controller.save()
        assert response.redirectedUrl == '/member/show/3'
    }

    void testListMember() {
        controller.list()
        assert Member.list().size() == model.memberList?.size()
        assert view == '/member/list'
    }

    void testVerifyMemberWithValidCardId() {
        params.cardId = "1159900100015"
        controller.verifyCard()

        assert response.redirectedUrl == '/member/show/1'
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

    void testEditInvalidMember() {
        controller.edit()

        assert response.redirectedUrl == '/error'
    }

    void testEditValidMember() {
        params.id = 1
        controller.edit()

        assert view == '/member/edit'
    }


/*
    void testUpdateInvalidMember() {
        controller.request.invalidToken = false
        controller.update()

        assert response.redirectedUrl == '/member/list'
    }
    */

}
