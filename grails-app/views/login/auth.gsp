<!doctype html>
<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="cashcard.system.login" /></title>
</head>

<body>

	<div class="container"><div class="row"><div class="span10">
		<div class="row show-grid">
			
      <div class="span6">
        <r:img uri="/images/logo_creditunion.gif" width="100%" />
      </div>
			<div class="well span3">

  			<form action='${postUrl}' method='POST' id='login'>
  				<fieldset>
  					<legend><g:message code="cashcard.system.login" /></legend>
  						<div class="clearfix">
  							<label for="xlInput"><g:message code="cashcard.system.login.username" /></label>
  								<div class="input">
  									<input id="xlInput" class="span3" type="text" size="10" name="j_username">
  								</div>
  						</div>
              <br />
  						<div class="clearfix">
  							<label for="xlInput"><g:message code="cashcard.system.login.password" /></label>
  								<div class="input">
  									<input id="xlInput" class="span3" type="password" size="10" name="j_password">
  									<g:hiddenField name="${rememberMeParameter}" id='remember_me' value="true"/>
  								</div>
  						</div>
              <hr />
  						<div class="clearfix">
  							<div class="input">
  							<div class="inline-inputs">	
  						<button id="login-button" class="btn btn-primary" type="submit"><g:message code="cashcard.system.login.button.login.label" /></button>
  						<button class="btn" type="reset"><g:message code="cashcard.system.login.button.cancel.label" /></button>
  						</div>
  						</div>
  						</div>
  												
  				</fieldset>							
  			</form>
      </div>
		</div>

	</div></div></div>

  </body>
</html>