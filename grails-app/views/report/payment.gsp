<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="contract.sign.form.title" /></title>
</head>
<body>

	<div class="container">
		<header class="page-header">
			<h1><g:message code="contract.sign.form.title" /></h1>
		</header>
	</div>

	<div class="container">
		
		<g:hasErrors bean="${contract}">
			<div id="errors" class="alert alert-error">
				<g:renderErrors bean="${contract}" as="list"></g:renderErrors>
			</div><!-- /errors -->
		</g:hasErrors>
			<div class="control-group">
				<label class="control-label">
					วันที่
				</label>
				<div class="controls">
					<g:jasperForm controller="report"
				    action="doPaymentReport"
				    id="1498"
				    jasper="${type}" >
				    from:
					<g:datePicker name="fromDate" precision="day"  />
					to:
					<g:datePicker name="toDate" precision="day"   />

				    <g:jasperButton format="pdf" jasper="${type}" text="pdf" />

				    

				</g:jasperForm>
				</div>

				
			</div>

			
		
	</div>

	
</body>
</html>