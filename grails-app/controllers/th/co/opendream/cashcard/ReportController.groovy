package th.co.opendream.cashcard

class ReportController {

    def overdue() { 
    	render(view:'overdue')
    }

    def doPaymentOverdueReport() {
        println params
        if(params.selectedDate) {
            params._name = params.name
            params._file = params.file   
            params._format = params.format
    	    params.selectedDate = params.selectedDate.format("yyyy-MM-dd'T'HH:mm:ss'Z'")       	
    	    chain(controller:'jasper',action:'index',params:params)
        } else {
            render(view:'overdue')
        }
    }

    def payment() {  
    	render(view:'payment', model:[type:params.paymenttype])
    }

    def doPaymentReport() {
        println params 
        if(params.fromDate <= params.toDate) { 
            params._name = params.name
            params._file = params.file	
            params._format = params.format
    	    params.fromDate = params.fromDate.format("yyyy-MM-dd'T'HH:mm:ss'Z'")
    	    params.toDate = params.toDate.next().format("yyyy-MM-dd'T'HH:mm:ss'Z'")
    	    chain(controller:'jasper',action:'index',params:params)
        } else {
            render(view:'payment', model:[type:params._file]) 
        }
    }
}
