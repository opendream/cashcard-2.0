<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="member.doLoan" /></title>
</head>
<body>

	<div class="container">
		<header class="page-header">
			<h1><g:message code="member.doLoan" /></h1>
		</header>
	</div>

	<div class="container">
		<g:form class="form-horizontal" action="create" controller="contract">
			<g:hiddenField name="memberId" value="${member?.id}" />
			<g:hiddenField name="loanType" value="${params?.type}" />

			<div class="control-group">
				<label class="control-label" for="amount">
					<g:message code="Loan Amount" />
				</label>
				<div class="controls">
					<g:field type="text" id="amount" name="amount" required="true" value="" pattern="\\d*(\\.\\d\\d)?" />
				</div>
			</div>

			<div class="control-group">
				<label class="control-label" for="guarantor1">
					<g:message code="Guarantor" />
				</label>
				<div class="controls">
					<input type="text" id="guarantor1" name="guarantor1" />
					<input type="text" id="guarantor2" name="guarantor2" />
				</div>
			</div>

			<div class="control-group">
				<label class="control-label" for="numberOfPeriod">
					<g:message code="Number of period" />
				</label>
				<div class="controls">
					<input type="text" id="numberOfPeriod" name="numberOfPeriod" />
				</div>
			</div>

			<div class="form-actions">
	          <button class="btn btn-primary" type="submit"><i class="icon-ok icon-white"></i> <g:message code="OK"></g:message></button>
	          <g:link action="show" id="${memberInstance?.id}"><g:message code="Cancel"></g:message></g:link>
	        </div>
		</g:form>
	</div>

</body>
</html>