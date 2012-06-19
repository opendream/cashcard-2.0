package th.co.opendream.cashcard

import th.co.opendream.cashcard.Member.Status

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MemberService)
@Mock([Member, UtilService, TempMember])
class MemberServiceTests {

    @Before
    void setUp() {
        /*def utilService = [
            check_id_card: { id -> true }
        ]*/
        def utilService = new Object()
        utilService.metaClass.check_id_card = { id -> true }
        Member.metaClass.utilService = utilService

        def m1 = new Member(identificationNumber:"1159900100015", firstname:"สมหญิง",
            lastname: "รักเรียน", telNo: "0818526122", gender: "MALE", address: "Opendream", creditUnionMemberId:1, creditUnionMemberNo:'001', memberNo: "001")

        def m2 = new Member(identificationNumber:"141190088198", firstname:"สมหนุ่ม",
            lastname: "รักเรียน", telNo: "0818526133", gender: "MALE", address: "Opendream", creditUnionMemberId:2, creditUnionMemberNo:'002', memberNo: "002")

        def m3 = new Member(identificationNumber:"141190088111", firstname:"delete",
            lastname: "deleteme", telNo: "0818526133", gender: "MALE", address: "Opendream", creditUnionMemberId:5, creditUnionMemberNo:'005', memberNo: "003")

        // not update member
        def tmpMember1 = new TempMember(id:1, identificationNumber:'1159900100015', creditUnionMemberNo: '001', telNo:'0818526122', gender:"MALE", firstname:"สมหญิง", lastname: "รักเรียน", address: "Opendream", valid:true, validIdentificationNumber:true, validTelNo:true, validFirstname: true, validLastname:true, validGender:true, validCreditUnionMemberNo:true, validCreditUnionMemberId:true, validAddress:true)

        // update member with firstname
        def tmpMember2 = new TempMember(id:2, identificationNumber:'141190088198', creditUnionMemberNo: '002', telNo:'0818526133', gender:"MALE", firstname:"สมหนุ่ม", lastname: "รักเรียน", address: "Opendream", valid:false, validIdentificationNumber:true, validTelNo:true, validFirstname: false, validLastname:true, validGender:true, validCreditUnionMemberNo:true, validCreditUnionMemberId:true, validAddress:true)

        // new member
        def tmpMember3 = new TempMember(id:3, identificationNumber:'141190081118', creditUnionMemberNo: '003', telNo:'0818526133', gender:"MALE", firstname:"สมหนุ่ม", lastname: "รักเรียน", address: "Opendream", valid:false, validIdentificationNumber:false, validTelNo:false, validFirstname: false, validLastname:false, validGender:false, validCreditUnionMemberNo:false, validCreditUnionMemberId:false, validAddress:false)

        def tmpMember4 = new TempMember(id:4, identificationNumber:'14119008117', creditUnionMemberNo: '004', telNo:'0818526133', gender:"MALE", firstname:"สมหนุ่ม", lastname: "รักเรียน", address: "Opendream", valid:false, validIdentificationNumber:false, validTelNo:false, validFirstname: false, validLastname:false, validGender:false, validCreditUnionMemberNo:false, validCreditUnionMemberId:false, validAddress:false)


        //m1.utilService = utilService
        //m2.utilService = utilService
        //m3.utilService = utilService

        m1.save()
        m2.save()
        m3.save()

        //mockDomain(TempMember, [tmpMember1, tmpMember2, tmpMember3])

        tmpMember1.save()
        tmpMember2.save()
        tmpMember3.save()
        tmpMember4.save()

    }

    void testValidSearch() {
        //search partial name
        def results

        results = service.search("สม")
        assert results.size() == 2
        results.each {
            assertTrue it.name.contains("สม")
        }

        // Search Exactly firstname
        results = service.search("สมหญิง")
        assert results.size() == 1

        // Search from surname
        results = service.search("รักเรียน")
        assert results.size() == 2

        // Search Exactly firstname+Lastname
        results = service.search("สมหญิง รักเรียน")
        assert results.size() == 1
    }

    void testFindNewMember() {
        def list = service.findNewMembers()
        assert null != list
        assert 2 == list.size()
    }

    void testFindUpdateMember() {
        def list = service.findUpdateMembers()
        assert null != list
        assert 1 == list.size()
    }

    void testFindDisabledMember() {
        def list = service.findDisabledMembers()
        list.each { println "creditUnionMemberId "+it.creditUnionMemberId }
        assert null != list
        assert 1 == list.size()
    }

    void testUnChangeMember() {
        def list = service.findUnChangeMembers()
        assert null != list
        assert 1 == list.size()
        println list.size()
    }

    void testFindChangedInMemberUpload() {
        def members = service.findChangedInMemberUpload()

        assert 2 == members.newMembers.size()
        assert 1 == members.updateMembers.size()
        assert 1 == members.disabledMembers.size()
        assert 1 == members.unchangeMembers.size()
    }

    void testMergeMembers() {
        assert 3 == Member.count()

        def count = 999
        service.runNoService = [
            next: { a, b -> "00" + count++ }
        ]
        def members = service.mergeMembers()

        assert 5 == Member.count()
        assert 4 == Member.findAllByStatus(Status.ACTIVE).size()

        assert Status.DELETED == Member.get(3).status
    }
}
