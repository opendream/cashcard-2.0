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
          <g:if test="${filename}">
            <g:link url="['action': 'showUpdateMember', params: [ 'filename': "${filename}" ] ]" >
                  ${message(code: "member.label.updatememberslist", default: "Update Members List")}
            </g:link>
          </g:if>
        </div>
  	  </g:uploadForm>
	</body>
</html>