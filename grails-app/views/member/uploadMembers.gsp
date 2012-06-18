<html>
  <head>
    <meta name="layout" content="main" />
    <title>${message(code: "member.uploadMembers.title", default: "Upload Members")}</title>
  </head>
  <body>
		<header class="page-header">
			<h1>${message(code: "member.uploadMembers.title", default: "Upload Members")}</h1>
		</header>

    <g:if test="${flash.error}">
        <div id="errors" class="alert alert-error">
          ${flash.error}
        </div><!-- /errors -->
    </g:if>
      <g:uploadForm action="doUploadMembers" class="form-horizontal">

    		<div class="control-group ${flash.error? 'error' : ''}">
          <label for="id-number" class="control-label">${message(code: "member.label.fileUpload", default: "File Upload")}</label>
          <div class="controls">

            
            <input type="file" name="members" required="true"/>              
            

            
            <span class="help-block"><g:message code="file_upload_help" /></span>

          </div>
        </div>

        <div class="form-actions">
          <button class="btn btn-primary" type="submit"><i class="icon-upload icon-white"></i> ${message(code: "member.label.upload", default: "Upload")}</button>
        </div>
  	  </g:uploadForm>

    <g:if test="${fileUpload}">
        <label class="control-label" for="tel">${message(code: 'member.label.newuser', default: 'สมาชิกใหม่')}</label>
        <table class="table table-striped table-bordered table-condensed member-list">
          <thead>
            <tr>
              <th class='identificationNumber span2'><g:message code="member.label.identificationNumber"></g:message></th>            
              <th><g:message code="member.label.name"></g:message></th>
              <th><g:message code="member.label.telNo"></g:message></th>
              <th><g:message code="member.label.gender"></g:message></th>
              <th><g:message code="member.label.address"></g:message></th>
              <th><g:message code="member.label.member_id" default="เลขที่อ้างอิง"></g:message></th>
              <th><g:message code="member.label.creditUnionMemberNo" default="หมายเลขสมาชิก"></g:message></th>
            </tr>
          </thead>
          <tbody>
            <g:each var="member" in ="${newMembers}">
              <tr>              
                <td class='identificationNumber span2'><a href="${createLink(controller:'member', action:'show', params:[id: member.id])}">${member.identificationNumber}</a></td>
                <td>${member.firstname} ${member.lastname}</td>
                <td>${member.telNo}</td>
                <td>${message(code: 'member.label.'+member?.gender.toString().toLowerCase(), default: member?.gender.toString())}</td>
                <td>${member.address}</td>
                <td>${member.id}</td>
                <td>${member.creditUnionMemberNo}</td>
              </tr>
            </g:each>
          </tbody>
        </table>

        <label class="control-label" for="tel">${message(code: 'member.label.updateuser', default: 'สมาชิกที่มีการเปลี่ยนแปลงข้อมูล')}</label>
        <table class="table table-striped table-bordered table-condensed member-list">
          <thead>
            <tr>
              <th class='identificationNumber span2'><g:message code="member.label.identificationNumber"></g:message></th>            
              <th><g:message code="member.label.name"></g:message></th>
              <th><g:message code="member.label.telNo"></g:message></th>
              <th><g:message code="member.label.gender"></g:message></th>
              <th><g:message code="member.label.address"></g:message></th>
              <th><g:message code="member.label.member_id" default="เลขที่อ้างอิง"></g:message></th>
              <th><g:message code="member.label.creditUnionMemberNo" default="หมายเลขสมาชิก"></g:message></th>
            </tr>
          </thead>
          <tbody>
            <g:each var="member" in ="${updateMembers}">
              <tr>              
                <td class='identificationNumber span2'><a href="${createLink(controller:'member', action:'show', params:[id: member.id])}">${member.identificationNumber}</a></td>
                <td>${member.firstname} ${member.lastname}</td>
                <td>${member.telNo}</td>
                <td>${message(code: 'member.label.'+member?.gender.toString().toLowerCase(), default: member?.gender.toString())}</td>
                <td>${member.address}</td>
                <td>${member.id}</td>
                <td>${member.creditUnionMemberNo}</td>
              </tr>
            </g:each>
          </tbody>
        </table>

        <label class="control-label" for="tel">${message(code: 'member.label.unchangeuser', default: 'สมาชิกที่ไม่มีการเปลี่ยนแปลงข้อมูล')}</label>
        <table class="table table-striped table-bordered table-condensed member-list">
          <thead>
            <tr>
              <th class='identificationNumber span2'><g:message code="member.label.identificationNumber"></g:message></th>
              <th><g:message code="member.label.name"></g:message></th>
              <th><g:message code="member.label.telNo"></g:message></th>
              <th><g:message code="member.label.gender"></g:message></th>
              <th><g:message code="member.label.address"></g:message></th>
              <th><g:message code="member.label.status"></g:message></th>
            </tr>
          </thead>
          <tbody>
            <g:each var="member" in ="${unchangeMembers}">
              <tr>
                <td class='identificationNumber span2'><a href="${createLink(controller:'member', action:'show', params:[id: member.id])}">${member.identificationNumber}</a></td>
                <td>${member.firstname} ${member.lastname}</td>
                <td>${member.telNo}</td>
                <td>${message(code: 'member.label.'+member?.gender.toString().toLowerCase(), default: member?.gender.toString())}</td>
                <td>${member.address}</td>
                <td><g:message code="member.label.status.${member.status}"></g:message></td>
              </tr>
            </g:each>
          </tbody>
        </table>

        <label class="control-label" for="tel">${message(code: 'member.label.disableuser', default: 'สมาชิกที่ยกเลิกการเป็นสมาชิก')}</label>
        <table class="table table-striped table-bordered table-condensed member-list">
          <thead>
            <tr>
              <th class='identificationNumber span2'><g:message code="member.label.identificationNumber"></g:message></th>
              <th><g:message code="member.label.name"></g:message></th>
              <th><g:message code="member.label.telNo"></g:message></th>
              <th><g:message code="member.label.gender"></g:message></th>
              <th><g:message code="member.label.address"></g:message></th>
              <th><g:message code="member.label.status"></g:message></th>
            </tr>
          </thead>
          <tbody>
            <g:each var="member" in ="${disabledMembers}">
              <tr>
                <td class='identificationNumber span2'><a href="${createLink(controller:'member', action:'show', params:[id: member.id])}">${member.identificationNumber}</a></td>
                <td>${member.firstname} ${member.lastname}</td>
                <td>${member.telNo}</td>
                <td>${message(code: 'member.label.'+member?.gender.toString().toLowerCase(), default: member?.gender.toString())}</td>
                <td>${member.address}</td>
                <td><g:message code="member.label.status.${member.status}"></g:message></td>
              </tr>
            </g:each>
          </tbody>
        </table>
        <g:form action="updateMembers" class="form-horizontal">
          <g:hiddenField name="fileUpload" value="${fileUpload}" />
          <div class="form-actions">
            <button class="btn btn-primary" type="submit"><i class="icon-ok icon-white"></i> ${message(code: 'member.label.confirm', default: 'Confirm')}</button>
          </div>
      </g:form>
    </g:if>  

	</body>
</html>