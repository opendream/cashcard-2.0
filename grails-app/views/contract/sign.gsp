<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="contract.loaning.form.title" /></title>
</head>
<body>

	<div class="container">
		<header class="page-header">
			<h1><g:message code="contract.loaning.form.title" /></h1>
		</header>
	</div>

	<div class="container">
		
		<g:hasErrors bean="${contract}">
			<div id="errors" class="alert alert-error">
				<g:renderErrors bean="${contract}" as="list"></g:renderErrors>
			</div><!-- /errors -->
		</g:hasErrors>

		<g:form class="form-horizontal" action="create" controller="contract">
			<g:hiddenField name="memberId" value="${member.id}" />
			<g:hiddenField name="loanType" value="${loanType.id}" />

			<div class="control-group ${hasErrors(bean:contract,field:'code', 'error')}">
				<label class="control-label" for="code">
					<g:message code="contract.loaning.form.code.label" />
				</label>
				<div class="controls">
					<g:field type="text" id="code" name="code" required="true" value="${contract.code}" />
				</div>
			</div>

			<div class="control-group ${hasErrors(bean:contract,field:'loanAmount', 'error')}">
				<label class="control-label" for="loanAmount">
					<g:message code="contract.loaning.form.loanAmount.label" />
				</label>
				<div class="controls">
					<g:field type="text" id="loanAmount" name="loanAmount" required="true" pattern="\\d*(\\.\\d\\d)?" value="${formatNumber(number: contract.loanAmount, format: '0.00')}" />
				</div>
			</div>

			<div class="control-group">
				<label class="control-label" for="guarantor1">
					<g:message code="contract.loaning.form.guarantor.label" />
				</label>
				<div class="controls">

					<div class="control-group ${hasErrors(bean:contract,field:'guarantor1', 'error')}">
						<label for="guarantor1">
							<g:message code="contract.loaning.form.guarantor1.label" />
						</label>
						<input type="text" id="guarantor1" name="guarantor1" value="${contract.guarantor1}" />
					</div>

					<div class="control-group ${hasErrors(bean:contract,field:'guarantor2', 'error')}">
						<label for="guarantor2">
							<g:message code="contract.loaning.form.guarantor2.label" />
						</label>
						<input type="text" id="guarantor1" name="guarantor2" value="${contract.guarantor2}" />
					</div>					
				</div>
			</div>

			<div class="control-group ${hasErrors(bean:contract,field:'numberOfPeriod', 'error')}">
				<label class="control-label" for="numberOfPeriod">
					<g:message code="contract.loaning.form.numberOfPeriod.label" />
				</label>
				<div class="controls">
					<input type="text" id="numberOfPeriod" name="numberOfPeriod" value="${contract.numberOfPeriod}" required="true" />
				</div>
			</div>

			<div class="form-actions">
				<button class="btn btn-primary" type="submit">
					<i class="icon-ok icon-white"></i> <g:message code="default.button.ok.label"></g:message>
				</button>
			  
				<g:link action="show" controller="member" id="${member?.id}">
					<g:message code="default.button.cancel.label"></g:message>
				</g:link>
			</div>
		</g:form>
	</div>

</body>
</html>