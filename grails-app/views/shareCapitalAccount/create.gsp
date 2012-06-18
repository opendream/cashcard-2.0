<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title>${message(code: 'account.create.title', default: 'Create Account')}</title>
  </head>
  <body>
  	<header class="page-header">
  		<h1><g:message code="account.create.title" default="Create Account" /></h1>
  	</header>

  	<g:hasErrors bean="${shareCapitalAccount}">
      <div id="errors" class="alert alert-error">
          <g:renderErrors bean="${shareCapitalAccount}" as="list"></g:renderErrors>
      </div><!-- /errors -->
    </g:hasErrors>
  	<g:form class="form-horizontal" action="doCreate">
  		<g:hiddenField name="id" value="${member.id}" />
  		<div class="control-group ${hasErrors(bean:shareCapitalAccount,field:'accountNumber', 'error')}">
  			<label for="accountNumber" class="control-label">
  				<g:message code="account.create.accountNumber.label" />
  			</label>
  			<div class="controls">
  				<input type="text" id="accountNumber" name="accountNumber" />
  			</div>
  		</div>

  		<div class="control-group ${hasErrors(bean:shareCapitalAccount,field:'registeredDate', 'error')}">
  			<label for="registeredDate" class="control-label">
  				<g:message code="account.create.registeredDate.label" />
  			</label>
  			<div class="controls">
  				<g:datePicker name="registeredDate" precision="day"  value="${shareCapitalAccount?.registeredDate}"  />
  			</div>
  		</div>

  		<div class="control-group ${hasErrors(bean:shareCapitalAccount,field:'balance', 'error')}">
  			<label for="balance" class="control-label">
  				<g:message code="account.create.balance.label" />
  			</label>
  			<div class="controls">
  				<input type="text" id="balance" name="balance" />
  			</div>
  		</div>

  		<div class="form-actions">
         	<button class="btn btn-primary" type="submit">
          		<i class="icon-ok icon-white"></i>
          		<g:message code='account.create.submit.label' />
        	</button>

        	<g:link action="show" controller="member" id="${member.id}">
	            <g:message code="default.button.cancel.label" />
	        </g:link>
        </div>
  	</g:form>
  </body>
</html>