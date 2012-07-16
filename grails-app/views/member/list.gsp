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
		
			
			<div class="print-member-card">
				<h3 class= "pull-left">ผลการค้นหา</h3>

				<g:jasperForm controller="member"
			    action="printMemberCard"
			    id="1497"
			    jasper="membercard" 			    			    
			    >
				<g:hiddenField id="file" name="file" value="membercard"/>
				<g:hiddenField id="name" name="name" value="membercard"/>
				<g:hiddenField id="format" name="format" value="PDF"/>
				<button id="print-membercard-button" class="btn btn-primary pull-right" type="submit" >
					<i class="icon-print icon-white"></i> <g:message code="default.button.print.label"></g:message>
				</button>
				
								
			</div>
	  
	  		
				<table class="table table-striped table-bordered table-condensed member-list">
					<thead>
						<tr>
							<th>
								<input id="groupmember" type="checkbox" name="groupmember" />
							</th>
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
								<td><input id="memberIds" type="checkbox" value="${member.id}" name="memberIds" onclick="on_checked(this)"/></td>
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
			</g:jasperForm>
			<div class="pagination">
				<cashcard:paginate controller="member" action="list" total="${memberCount}" />
			</div>
	  
		<script>

			$('#groupmember').click( function() {
						if($('#groupmember').is(':checked')) {
							$('input[name=memberIds]').attr('checked', true);	
							disabledMemberCardButton(false);				
						} else {
							$('input[name=memberIds]').attr('checked', false);
							disabledMemberCardButton(true);						
						}
					});

			function disabledMemberCardButton(value) {
				$('#print-membercard-button').attr('disabled', value);
			}

			function on_checked() {
				console.log($("input[name=memberIds]").length);
				console.log($("#memberIds:checked").length);

				var checkedNum = $("#memberIds:checked").length;
				var checkboxNum = $("input[name=memberIds]").length;

				if(checkedNum==0) {
					disabledMemberCardButton(true);
				} else {

					if(checkboxNum == checkedNum) {
						$('#groupmember').attr('checked', true);
					} else {
						$('#groupmember').attr('checked', false);
					}
					disabledMemberCardButton(false);
				}				
			}

			on_checked(true);

		</script>	
	</body>
</html>