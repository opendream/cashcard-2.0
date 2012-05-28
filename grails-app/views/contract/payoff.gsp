<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="contract.payoff.title" args="[period.contract.code, period.no]" /></title>
	</head>
	<body>
		<div class="container">
			<header class="page-header">
				<g:link controller="member" action="show" id="${member.id}">
					<em><i class="icon-arrow-left"></i><g:message code="back.to.member" args="[period.contract.member]" /></em>
				</g:link>
				<h1><g:message code="contract.payoff.title" args="[period.contract.code, period.no]" /></h1>
			</header>
		</div>

		<div class="container" >
			<g:form action="doPayoff" class="form-horizontal">
				<g:hiddenField name="id" value="${period.id}" />

				<div class="control-group">
					<label class="control-label">
						<g:message code="contract.payoff.form.dueDate.label" />
					</label>
					<div class="controls">
						<span class="input-xlarge uneditable-input"><g:formatDate date="${period.dueDate}" format="EEEE dd MMMM yyyy" /></span>
					</div>
				</div>

				<div class="control-group ${hasErrors(bean:period,field:'amount', 'error')}">
					<label class="control-label">
						<g:message code="contract.payoff.form.amount.label" />
					</label>
					<div class="controls">
						<g:field type="text" id="amount" name="amount" required="true" pattern="\\d*(\\.\\d\\d)?" value="${formatNumber(number: period.amount, format: '0.00')}" />
					</div>
				</div>

				<div class="control-group ${hasErrors(bean:period,field:'fine', 'error')}">
					<label class="control-label">
						<g:message code="contract.payoff.form.fine.label" />
					</label>
					<div class="controls">
						<g:field type="text" id="fine" name="fine" pattern="\\d*(\\.\\d\\d)?" value="${formatNumber(number: receiveTx?.fine, format: '0.00')}" />
					</div>
				</div>

				<div class="control-group ${hasErrors(bean:period,field:'isShareCapital', 'error')}">
					<label class="control-label">
						<g:message code="contract.payoff.form.isShareCapital.label" />
					</label>
					<div class="controls">
						<label class="checkbox">
							<g:field type="checkbox" id="isShareCapital" name="isShareCapital" value="${receiveTx?.isShareCapital}" />
							<g:message code="contract.payoff.form.isShareCapital.checkbox.label" />
						</label>
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
