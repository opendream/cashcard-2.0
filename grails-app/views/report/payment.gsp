<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="contract.sign.form.title" /></title>
</head>
<body>

	<header class="page-header">
		<h1><g:if test="${type == 'daily-payloan'}">
				${message(code: 'main.menu.payloanReport', default: 'รายงานการจ่ายเงินกู้')}
			</g:if>
			<g:if test="${type == 'daily-payoff'}">
				${message(code: 'main.menu.payoffReport', default: 'รายงานการชำระเงินกู้ของลูกหนี้')}
			</g:if></h1>
	</header>

	<div class="container"><div class="row"><div class="span10">
		
		<g:hasErrors bean="${contract}">
			<div id="errors" class="alert alert-error">
				<g:renderErrors bean="${contract}" as="list"></g:renderErrors>
			</div><!-- /errors -->
		</g:hasErrors>
			

				
		<g:jasperForm controller="report"
		    action="doPaymentReport"
		    id="1498"
		    jasper="${type}"
		    class="form-horizontal" >

		    <div class="control-group">
			    <label class="control-label">ตั้งแต่วันที่:</label>
			    <div class="controls">
					<g:datePicker name="fromDate" precision="day"/>
				</div>
			</div>

			<div class="control-group">
				<label class="control-label">ถึงวันที่:</label>
				<div class="controls">
					<g:datePicker name="toDate" precision="day"  />
				</div>
			</div>
			
			<g:hiddenField id="file" name="file" value="${type}"/>
			<g:hiddenField id="name" name="name" value="${type}"/>
			<g:hiddenField id="format" name="format" value="PDF"/>
			<div class="form-actions">
				<button class="btn btn-primary" type="submit">
						<g:message code="default.button.ok.label"></g:message>
					</button>
			</div>
		</g:jasperForm>
				

				
			

			
		
	</div></div></div>

	
</body>
</html>