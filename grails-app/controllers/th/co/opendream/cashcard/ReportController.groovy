package th.co.opendream.cashcard

class ReportController {

    def overdue() { 
    	render(view:'overdue')
    }

    def doPaymentOverdueReport() {
        //println params
        if(params.selectedDate) {
            params._name = params.name
            params._file = params.file   
            params._format = params.format
    	    params.selectedDate = params.selectedDate.format("yyyy-MM-dd'T'HH:mm:ss'Z'") 
            // fixed for demo
            params.selectedDate = "$params.selectedDate_year-$params.selectedDate_month-$params.selectedDate_day"
              	
    	    chain(controller:'jasper',action:'index',params:params)
        } else {
            render(view:'overdue')
        }
    }

    def payment() {  
    	render(view:'payment', model:[type:params.paymenttype])
    }

    def doPaymentReport() {
         
        if(params.fromDate <= params.toDate) { 
            params._name = params.name
            params._file = params.file	
            params._format = params.format
    	    //params.fromDate = params.fromDate.format("yyyy-MM-dd'T'HH:mm:ss'Z'")
            params.toDate = params.toDate.next()
            // fixed for demo
            params.fromDate = "$params.fromDate_year-$params.fromDate_month-$params.fromDate_day"
    	    params.toDate = "$params.toDate_year-$params.toDate_month-$params.toDate_day"
            
    	    chain(controller:'jasper',action:'index',params:params)
        } else {
            render(view:'payment', model:[type:params._file]) 
        }
    }

    def parseDateToString(def date) {
        def calendar = Calendar.getInstance(new Locale('en', 'US'))
        calendar.setTime(date)
        calendar.format('yyyy-MM-dd')
    }
}
