package th.co.opendream.cashcard

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class MemberController {

    def utilService, periodService, memberService, kettleService

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
        if (!memberInstance.save()) {
            render(view: "create", model: [memberInstance: memberInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'member.label', default: 'Member'), memberInstance.id])
        redirect(action: "show", id: memberInstance.id)
    }

    def show() {
        def memberInstance = Member.get(params.id)
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
                    totalDebt += p.outstanding
                }
            }
            contract.metaClass.totalDebt = totalDebt
        }

        render view: 'show', model: [memberInstance: memberInstance, contractList: contractList]
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
            flash.error = "ไม่พบสมาชิกที่มีหมายเลขบัตรประชาชน ${cardId}, ต้องการลงทะเบียนสมาชิกใหม่? โปรดไปที่ " + link(controller: "member", action: "create") { "ลงทะเบียน" }
            render(view: 'verifyCard')
        }
        else {
            render(view: 'verifyCard')
        }
    }

    def uploadMembers() {
        render view: "uploadMembers"
    }

    def doUploadMembers() {
        try {
            def kettlePath = grailsApplication.config.kettle.repository.path
            def f = request.getFile('members') 
                  
            if (f.empty) {
                flash.error = 'file cannot be empty'
                render(view: 'uploadMembers')
                return
            } 
            
            f.transferTo(new File("${kettlePath}/fileupload/${f.originalFilename}"))        
            def result = kettleService.extractMember(f.originalFilename, f.contentType)            
        } catch (e) {
            log.error(e)
            flash.error = message(code: 'errors.extractMembersNotComplete')
        }      
        render view: "uploadMembers"

    def ajaxSearch() {
        def result = memberService.search(params.name)
        render result as JSON
    }
}
