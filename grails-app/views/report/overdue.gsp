<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="contract.sign.form.title" /></title>
</head>
<body>

	<div class="container">
		<header class="page-header">
			<h1>${message(code: 'main.menu.overduepaymentReport', default: 'รายงานหนี้ที่เกินวันกำหนดชำระจ่าย')}</h1>
		</header>
	</div>

	<div class="container">
		
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
			<div class="form-actions">    
			    <g:jasperButton format="pdf" jasper="daily-overdue" class="btn btn-primary" text="สั่งพิมพ์" />		    
			</div>    
		</g:jasperForm>

				
	</div>

			
		
	</div>

	
</body>
</html>