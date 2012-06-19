package th.co.opendream.cashcard

import th.co.opendream.cashcard.Member.Status

class MemberService {

    def runNoService

    def search(input) {
        def (name, surname) = input.tokenize(" ")
        def c = Member.createCriteria()
        def members = c.list(sort:"firstname") {
            if (surname == null) {
                or {
                    ilike ('firstname', "%${name}%")
                    ilike ('lastname', "%${name}%")
                }
            }
            else {
                and {
                    ilike ('firstname', "%${name}%")
                    ilike ('lastname', "%${surname}%")
                }
            }
        }
        members.collect {
            [id: it.id, name: it.toString()]
        }
    }

    def findNewMembers(def filename) {
        // change ValidCreditUnionMemberId -> ValidCreditUnionMemberNo
        def newMembers = TempMember.findAllByValidAndValidCreditUnionMemberNoAndFilename(false, false, filename)
    }

    def findUpdateMembers(def filename) {
        // change ValidCreditUnionMemberId -> ValidCreditUnionMemberNo
        def updateMembers = TempMember.findAllByValidAndValidCreditUnionMemberNoAndFilename(false, true, filename)
    }

    def findUnChangeMembers(def filename) {
        def unchangeMembers = TempMember.findAllByValidAndFilename(true, filename)
    }

    def findDisabledMembers(def filename) {
        def uploadMembers = []
        TempMember.findAllByFilename(filename).collect(uploadMembers) { it.creditUnionMemberNo }
        def c = Member.createCriteria()
        def disabledMembers = c.list(sort:"firstname") {
            not {
                    inList ('creditUnionMemberNo', uploadMembers)
                }
        }
        disabledMembers
    }

    def findChangedInMemberUpload(def filename) {
        def members = [:]
        members.newMembers = findNewMembers(filename)
        members.updateMembers = findUpdateMembers(filename)
        members.unchangeMembers = findUnChangeMembers(filename)
        members.disabledMembers = findDisabledMembers(filename)
        members
    }

    def mergeMembers(def filename) {
        def members = findChangedInMemberUpload(filename)

        def disableMembers = members.disabledMembers
        disableMembers.each {
            def member = Member.findByCreditUnionMemberNo(it.creditUnionMemberNo)
            member.status = Status.DELETED
            member.save()
        }

        def updateMembers = members.updateMembers
        updateMembers.each {
            def member = Member.findByCreditUnionMemberNo(it.creditUnionMemberNo)
            def currentIdentificationNumber = member.identificationNumber
            def currentAddress = member.address
            def currentTelNo = member.telNo
            member.properties = it.properties
            if(!it.identificationNumber) {
                member.identificationNumber = currentIdentificationNumber
            }
            if(!it.address) {
                member.address = it.address
            }
            if(!it.telNo) {
                member.telNo = it.telNo
            }
            member.status = Status.ACTIVE
            member.save()
            // update share capital
        }

        def newMembers = members.newMembers
        newMembers.each {
            def member = new Member()
            member.properties = it.properties
            if (!member.memberNo) {
                member.memberNo = runNoService.next('Member', member.dateCreated)
            }
            member.save()
            //insert new share capital
        }
    }
}
