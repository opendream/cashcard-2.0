package th.co.opendream.cashcard

class ApiController {
	def kettleService
	
    def index() { 
    	println 'hello'
    	render 'hello world!'
    }

    def uploadMembers() {
        try {
            def f = request.getFile('members')
            if (f.empty) {
                render 'file cannot be empty'
                return
            }
            def result = kettleService.extractMember(f)
        } catch (e) {
            log.error(e)
            render 'upload not complete'
            return
        }
        render 'upload complete'
    }
}
