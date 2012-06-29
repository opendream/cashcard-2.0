<html>
  <head>
    <meta name="layout" content="main" />
    <title>${message(code: 'member.list.title', default: 'List Members')}</title>
  </head>
  <body>
		<header class="page-header">
			<h1>${message(code: 'member.list.title', default: 'List Members')}</h1>
		</header>

		
			<g:form action="list" class="form-horizontal">

				<div class="control-group">
		          <label for="id-number" class="control-label">${message(code: 'member.label.identificationNumber', default: 'Identification Number')}</label>
		          <div class="controls">
		            <input id="id-number" name="identificationNumber" type="text" placeholder="${message(code: 'member.placeholder.identificationNumber', default: 'Identification Number')}" value="${params?.identificationNumber}">
		            <span class="help-block"><g:message code="id_card_help" /></span>
		          </div>
		        </div>

		        <div class="control-group">
		          <label for="credit-union-number" class="control-label">${message(code: 'member.label.creditUnionMemberNo', default: 'Credit union member Number')}</label>
		          <div class="controls">
		            <input id="credit-union-number" name="creditUnionMemberNo" type="text" placeholder="${message(code: 'member.placeholder.creditUnionMemberNo', default: 'Credit union member Number')}" value="${params?.creditUnionMemberNo}">
		            <span class="help-block"><g:message code="id_card_help" /></span>
		          </div>
		        </div>

		        <div class="control-group">
		          <label for="first-name" class="control-label">${message(code: 'member.label.firstName', default: 'First Name')}</label>
		          <div class="controls">
		            <input id="first-name" name="firstname" type="text" placeholder="${message(code: 'member.label.firstName', default: 'First Name')}" value="${params?.firstname}">
		          </div>
		        </div>

		        <div class="control-group">
		          <label class="control-label" for="last-name">${message(code: 'member.label.lastName', default: 'Last Name')}</label>
		          <div class="controls">
		            <input id="last-name" name="lastname" type="text" placeholder="${message(code: 'member.label.lastName', default: 'Last Name')}" value="${params?.lastname}">
		          </div>
		        </div>

		        <div class="control-group">
		          <label class="control-label" for="tel">${message(code: 'member.label.telNo', default: 'Tel No.')}</label>
		          <div class="controls">
		            <input id="tel" name="telNo" type="text" placeholder="${message(code: 'member.label.telNo', default: 'Tel.')}" value="${params?.telNo}">
		            <span class="help-block"><g:message code="id_card_help" /></span>
		          </div>
		        </div>

		        <div class="form-actions">
		          <button class="btn btn-primary" type="submit"><i class="icon-search icon-white"></i> ${message(code: 'Search')}</button>
		        </div>
			</g:form>
		

	  
			<table class="table table-striped table-bordered table-condensed member-list">
				<thead>
					<tr>
						<th class='identificationNumber span2'><g:message code="member.label.identificationNumber"></g:message></th>
						<th class='creditUnionMemberNo'><g:message code="member.label.creditUnionMemberNo"></g:message></th>
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
							<td class='identificationNumber span2'><a href="${createLink(controller:'member', action:'show', params:[id: member.id])}">${member.identificationNumber}</a></td>
							<td class='creditUnionMemberNo'>${member.creditUnionMemberNo}</td>
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
	  

	</body>
</html>