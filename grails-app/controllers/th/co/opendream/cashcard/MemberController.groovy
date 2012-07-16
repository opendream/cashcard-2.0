package th.co.opendream.cashcard

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class MemberController {

    def utilService, periodService, memberService, kettleService,
        shareCapitalAccountService, runNoService, grailsApplication

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.offset = params.offset ? params.int('offset') : 0
        params.max = params.max ? params.int('max') : 20

        def c = Member.createCriteria()
        def memberList = c.list(offset: params.offset, max: params.max) {
            if (params.identificationNumber) {
                ilike('identificationNumber', "%${params.identificationNumber}%")
            }
            if (params.creditUnionMemberNo) {
                ilike('creditUnionMemberNo', "%${params.creditUnionMemberNo}%")
            }
            if (params.firstname) {
                ilike('firstname', '%' + params.firstname + '%')
            }
            if (params.lastname) {
                ilike('lastname', '%' + params.lastname + '%')
            }
            if (params.telNo) {
                ilike('telNo' , '%' + params.telNo + '%')
            }
        }

        render (view: 'list', model:[memberList: memberList, memberCount: memberList.totalCount])
    }


    def create() {
        [memberInstance: new Member(params)]
    }

    def save() {
        def memberInstance = new Member(params)

        // Assign running number.
        memberInstance.memberNo = runNoService.next('Member')

        if (!memberInstance.save()) {
            render(view: "create", model: [memberInstance: memberInstance])
            return
        }

        shareCapitalAccountService.createAccountFromMember(memberInstance, memberInstance.creditUnionMemberNo, new Date())

		flash.message = message(code: 'default.created.message', args: [message(code: 'member.label', default: 'Member'), memberInstance.id])
        redirect(action: "show", id: memberInstance.id)
    }

    def show() {
        def memberInstance = Member.get(params.id)
        def slip = [doPrint: false]
        if (params.pid) {
            slip.id = params.pid
            slip.type = params.type
            slip.doPrint = true
        }
        if (!memberInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'member.label', default: 'Member'), params.id])
            redirect(action: "list")
            return
        }

        def c = Contract.createCriteria()
        def contractList = c.list(sort: 'dateCreated', order: 'asc') {
            eq('member', memberInstance)
        }

        contractList.each { contract ->
            contract.metaClass.isPayable = utilService.isPayable(contract)
            contract.metaClass.currentPeriod = periodService.getCurrentPeriod(contract)

            def pc = Period.createCriteria()
            def periodList = pc.list(sort: 'no', order: 'asc') {
                eq('contract', contract)
            }

            def totalDebt = 0.00
            periodList.each { p ->
                if (!p.payoffStatus) {
                    if (!p.cancelledDueToDebtClearance) {
                        totalDebt += p.outstanding
                    }
                }
            }
            contract.metaClass.totalDebt = totalDebt
        }

        def shareCapitalAccount = shareCapitalAccountService.getMemberAccount(memberInstance)
        render view: 'show', model: [memberInstance: memberInstance,
            contractList: contractList, shareCapitalAccount: shareCapitalAccount,
            slip: slip]
    }

    def edit() {
        def memberInstance = Member.get(params.id)
        withForm {
            if (memberInstance) {
                render(view: 'edit', model: [memberInstance: memberInstance])
            }
            else {
                redirect(uri: '/error')
            }
        }.invalidToken {
            render(view: 'edit', model: [memberInstance: memberInstance])
        }
    }

    def update() {
        def memberInstance = Member.get(params.id)
        if (!memberInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'member.label', default: 'Member'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (memberInstance.version > version) {
                memberInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'member.label', default: 'Member')] as Object[],
                          "Another user has updated this Member while you were editing")
                render(view: "edit", model: [memberInstance: memberInstance])
                return
            }
        }

        memberInstance.properties = params

        if (!memberInstance.save(flush: true)) {
            render(view: "edit", model: [memberInstance: memberInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'member.label', default: 'Member'), memberInstance.id])
        redirect(action: "show", id: memberInstance.id)
    }

    def verifyCard(String cardId) {
        flash.error = null // Clear flash

        cardId = cardId ?: ''
        def memberInstance = Member.findByIdentificationNumber("${cardId}")
        if (memberInstance) {
            redirect(action: "show", id: memberInstance.id)
        }
        else if (! memberInstance && cardId) {
            if(utilService.check_id_card(cardId)){
                flash.error = "ไม่พบสมาชิกที่มีหมายเลขบัตรประชาชน ${cardId}, ต้องการลงทะเบียนสมาชิกใหม่? โปรดไปที่ " + link(controller: "member", action: "create" , params:[identificationNumber:cardId]) { "ลงทะเบียน" }
            }
            else{
                flash.error = "ไม่พบสมาชิกที่มีหมายเลขบัตรประชาชน ${cardId}, ต้องการลงทะเบียนสมาชิกใหม่? โปรดไปที่ " + link(controller: "member", action: "create") { "ลงทะเบียน" }
            }
            render(view: 'verifyCard')
        }
        else {
            render(view: 'verifyCard')
        }
    }

    def uploadMembers() {
        def filename = memberService.findUploadMembersFilename()
        render view: "uploadMembers", model: [filename:filename?:null]
    }

    def doUploadMembers() {
        def filename
        def memberUpload
        try {
            def f = request.getFile('members')
            if (f.empty) {
                flash.error = 'file cannot be empty'
                render(view: 'uploadMembers')
                return
            }

            filename = f.originalFilename
            def result = kettleService.extractMember(f)
            memberUpload = memberService.findChangedInMemberUpload(filename)

        } catch (e) {
            log.error(e)
            flash.error = message(code: 'errors.extractMembersNotComplete')
            render(view: 'uploadMembers')
            return
        }
        render (view: "confirm", model: [newMembers: memberUpload?.newMembers, updateMembers: memberUpload?.updateMembers, unchangeMembers: memberUpload?.unchangeMembers, disabledMembers: memberUpload?.disabledMembers, filename:filename])
    }

    def showUpdateMember() {
        def filename = params.filename
        def memberUpload = memberService.findChangedInMemberUpload(filename)
        render (view: "confirm", model: [newMembers: memberUpload?.newMembers, updateMembers: memberUpload?.updateMembers, unchangeMembers: memberUpload?.unchangeMembers, disabledMembers: memberUpload?.disabledMembers, filename:filename])
    }

    def mergeMembers() {
        def filename = params.filename
        if(!filename) {
            render(view: 'uploadMembers')
            return
        }
        memberService.mergeMembers(filename)
        //memberService.removeUploadMembers(filename)
        redirect(action: "list")
    }

    def ajaxSearch() {
        def result = memberService.search(params.name)
        render result as JSON
    }

    def printMemberCard() {        
        def memberIds = []
        try {
        params.list('memberIds').each { memberIds << it.toLong() }
        def members = memberService.getMemberByMemberIds(memberIds)
        params.report_path = grailsApplication.config.jasper.dir.reports
        params._name = params.name
        params._file = params.file
        params._format = params.format

        chain(controller:'jasper',action:'index',params:params, model: [data: members])   
        } catch(e) {
            log.error(e)
            redirect(action: "list")
            return
        }     
    }
}
