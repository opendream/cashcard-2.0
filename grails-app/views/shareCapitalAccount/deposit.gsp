<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code='account.deposit.title' args='[member]' /></title>
  </head>
  <body>
  	<header class="page-header">
  		<h1><g:message code='account.deposit.title' args='[member]' /></h1>
  	</header>

  	<g:form class="form-horizontal" action="doDeposit">
  		<g:hiddenField name="id" value="${member.id}" />

      <div class="control-group">
          <label class="control-label">
              <g:message code="account.deposit.accountNumber.label" />
          </label>
          <div class="controls">
              <span class="input-xlarge uneditable-input">
                  ${shareCapitalAccount?.accountNumber}
              </span>
          </div>
      </div>

      <div class="control-group">
          <label class="control-label">
              <g:message code="account.deposit.balance.label" />
          </label>
          <div class="controls">
              <span class="input-xlarge uneditable-input">
                  <g:formatNumber type="number" number="${shareCapitalAccount?.balance}" maxFractionDigits="2" minFractionDigits="2" />
              </span>
          </div>
      </div>

  		<div class="control-group">
  			<label for="paymentDate" class="control-label">
  				<g:message code="account.deposit.paymentDate.label" />
  			</label>
  			<div class="controls">
  				<g:datePicker name="paymentDate" precision="day"  value="${accountTx?.paymentDate}"  />
  			</div>
  		</div>

  		<div class="control-group ${hasErrors(bean:accountTx,field:'amount', 'error')}">
  			<label for="amount" class="control-label">
  				<g:message code="account.deposit.amount.label" />
  			</label>
  			<div class="controls">
  				<g:field type="text" id="amount" name="amount" required="true" pattern="\\d*(\\.\\d\\d)?" value="${formatNumber(number: accountTx.amount, format: '0.00')}" required="true" />
  			</div>
  		</div>

  		<div class="form-actions">
         	<button class="btn btn-primary" type="submit">
          		<i class="icon-ok icon-white"></i>
          		<g:message code='account.deposit.submit.label' />
        	</button>

        	<g:link action="show" controller="member" id="${member.id}">
	            <g:message code="default.button.cancel.label" />
	        </g:link>
        </div>
  	</g:form>
  </body>
</html>