<html>
  <head>
    <meta name="layout" content="main" />
    <title>${message(code: "member.vefify.title", default: "List All Member")}</title>
  </head>
  <body>
		<header class="page-header">
			<h1>${message(code: "member.vefify.title", default: "List All Member")}</h1>
		</header>

    <g:if test="${flash.error}">
        <div id="errors" class="alert alert-error">
          ${flash.error}
        </div><!-- /errors -->
    </g:if>
	  <div class="container"><div class="row"><div class="span10">
      <g:form action="verifyCard" class="form-horizontal">

    		<div class="control-group ${flash.error? 'error' : ''}">
          <label for="id-number" class="control-label">${message(code: "member.label.identificationNumber", default: "Identification Number")}</label>
          <div class="controls">
            <input id="id-number" name="cardId" type="text" placeholder="${message(code: "member.placeholder.identificationNumber", default: "Identification Number")}" />
            <span class="help-block"><g:message code="id_card_help" /></span>
          </div>
        </div>

        <div class="form-actions">
          <button class="btn btn-primary" type="submit"><i class="icon-ok icon-white"></i> ${message(code: "member.label.verify", default: "Verify")}</button>
        </div>
  	  </g:form>
	  </div></div></div>

	</body>
</html>