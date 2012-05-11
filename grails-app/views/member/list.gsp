<html>
  <head>
    <meta name="layout" content="main" />
    <title>${message(code: 'member.list.title', default: 'List Members')}</title>
  </head>
  <body>
		<div class="container">
			<header class="page-header">
				<h1>${message(code: 'member.list.title', default: 'List Members')}</h1>
			</header>
		</div>

		<div class="container">
			<g:form action="list" class="form-horizontal">

				<div class="control-group ${hasErrors(bean: memberInstance, field: 'identificationNumber', 'error')}">
		          <label for="id-number" class="control-label">${message(code: 'member.label.identificationNumber', default: 'Identification Number')}</label>
		          <div class="controls">
		            <input id="id-number" name="identificationNumber" type="text" placeholder="${message(code: 'member.placeholder.identificationNumber', default: 'Identification Number')}" value="${memberInstance?.identificationNumber}">
		            <span class="help-block"><g:message code="id_card_help" /></span>
		          </div>
		        </div>

		        <div class="control-group ${hasErrors(bean: memberInstance, field: 'firstname', 'error')}">
		          <label for="first-name" class="control-label">${message(code: 'member.label.firstName', default: 'First Name')}</label>
		          <div class="controls">
		            <input id="first-name" name="firstname" type="text" placeholder="${message(code: 'member.label.firstName', default: 'First Name')}" value="${memberInstance?.firstname}">
		          </div>
		        </div>

		        <div class="control-group  ${hasErrors(bean: memberInstance, field: 'lastname', 'error')}">
		          <label class="control-label" for="last-name">${message(code: 'member.label.lastName', default: 'Last Name')}</label>
		          <div class="controls">
		            <input id="last-name" name="lastname" type="text" placeholder="${message(code: 'member.label.lastName', default: 'Last Name')}" value="${memberInstance?.lastname}">
		          </div>
		        </div>

		        <div class="control-group ${hasErrors(bean: memberInstance, field: 'telNo', 'error')}">
		          <label class="control-label" for="tel">${message(code: 'member.label.telNo', default: 'Tel No.')}</label>
		          <div class="controls">
		            <input id="tel" name="telNo" type="text" placeholder="${message(code: 'member.label.telNo', default: 'Tel.')}" value="${memberInstance?.telNo}">
		            <span class="help-block"><g:message code="id_card_help" /></span>
		          </div>
		        </div>

		        <div class="form-actions">
		          <button class="btn btn-primary" type="submit"><i class="icon-search icon-white"></i> ${message(code: 'Search')}</button>
		        </div>
			</g:form>
		</div>

	  <div class="container">
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th><g:message code="member.label.identificationNumber"></g:message></th>
						<th><g:message code="member.label.name"></g:message></th>
						<th><g:message code="member.label.telNo"></g:message></th>
						<th><g:message code="member.label.gender"></g:message></th>
						<th><g:message code="member.label.address"></g:message></th>
						<th><g:message code="member.label.status"></g:message></th>
					</tr>
				</thead>
				<tbody>
					<g:each var="member" in ="${memberList}">
						<tr>
							<td><a href="${createLink(controller:'member', action:'show', params:[id: member.id])}">${member.identificationNumber}</a></td>
							<td>${member.firstname} ${member.lastname}</td>
							<td>${member.telNo}</td>
							<td>${message(code: 'member.label.'+member?.gender.toString().toLowerCase(), default: member?.gender.toString())}</td>
							<td>${member.address}</td>
							<td><g:message code="member.label.status.${member.status}"></g:message></td>
						</tr>
					</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<cashcard:paginate controller="member" action="list" total="${memberCount}" />
			</div>
	  </div>

	</body>
</html>