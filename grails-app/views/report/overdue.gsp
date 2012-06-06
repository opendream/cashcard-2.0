<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="contract.sign.form.title" /></title>
</head>
<body>

	<header class="page-header">
		<h1>${message(code: 'main.menu.overduepaymentReport', default: 'รายงานหนี้ที่เกินวันกำหนดชำระจ่าย')}</h1>
	</header>

		
		<g:hasErrors bean="${contract}">
			<div id="errors" class="alert alert-error">
				<g:renderErrors bean="${contract}" as="list"></g:renderErrors>
			</div><!-- /errors -->
		</g:hasErrors>		
					
		<g:jasperForm controller="report"
		    action="doPaymentOverdueReport"
		    id="1499"
		    jasper="daily-overdue" 
		    class="form-horizontal" >
		    <div class="control-group">
			    <label class="control-label">วันที่:</label>
			    <div class="controls">
			    	<g:datePicker name="selectedDate" precision="day"   />
			    </div>
		    </div>
			    
			<g:hiddenField id="file" name="file" value="daily-overdue"/>
			<g:hiddenField id="name" name="name" value="daily-overdue"/>
			<g:hiddenField id="format" name="format" value="PDF"/>
			<g:hiddenField id="title" name="title" value="${message(code: 'main.menu.overduepaymentReport', default: 'รายงานหนี้ที่เกินวันกำหนดชำระจ่าย')}"/>
			<div class="form-actions">
				<button class="btn btn-primary" type="submit">
						<g:message code="default.button.ok.label"></g:message>
					</button>
			</div>	    
			
		</g:jasperForm>


	
</body>
</html>