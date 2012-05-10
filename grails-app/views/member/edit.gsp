<%@ page import="th.co.opendream.cashcard.Member.Gender" %>
<html>
  <head>
    <meta name="layout" content="main" />
    <title>แก้ไขข้อมูลสมาชิก</title>
  </head>
  <body>
		<div class="container">
			<header class="page-header">
				<h1>แก้ไขข้อมูลสมาชิก</h1>
			</header>
		</div>
    <g:hasErrors bean="${memberInstance}">
      <div id="errors" class="alert alert-error">
          <g:renderErrors bean="${memberInstance}" as="list"></g:renderErrors>
      </div><!-- /errors -->
    </g:hasErrors>
    <div class="container" >
      <g:form action="update" class="form-horizontal" useToken="true">

        <g:hiddenField name="id" value="${memberInstance?.id}" />
        <g:hiddenField name="version" value="${memberInstance?.version}" />


        <div class="control-group ${hasErrors(bean: memberInstance, field: 'identificationNumber', 'error')}">
          <label for="id-number" class="control-label">หมายเลขบัตรประชาชน</label>
          <div class="controls">
            <input id="id-number" name="identificationNumber" type="text" placeholder="กรุณาใส่หมายเลขบัตรประชาชน 13 หลัก" value="${memberInstance?.identificationNumber}">
          </div>
        </div>

        <div class="control-group ${hasErrors(bean: memberInstance, field: 'firstname', 'error')}">
          <label for="first-name" class="control-label">ชื่อ</label>
          <div class="controls">
            <input id="first-name" name="firstname" type="text" placeholder="${message(code: 'member.label.firstName', default: 'First Name')}" value="${memberInstance?.firstname}">
          </div>
        </div>

        <div class="control-group  ${hasErrors(bean: memberInstance, field: 'lastname', 'error')}">
          <label class="control-label" for="last-name">นามสกุล</label>
          <div class="controls">
            <input id="last-name" name="lastname" type="text" placeholder="${message(code: 'member.label.lastName', default: 'Last Name')}" value="${memberInstance?.lastname}">
          </div>
        </div>

        <div class="control-group ${hasErrors(bean: memberInstance, field: 'telNo', 'error')}">
          <label class="control-label" for="tel">หมายเลขโทรศัพท์</label>
          <div class="controls">
            <input id="tel" name="telNo" type="text" placeholder="${message(code: 'member.label.telNo', default: 'Tel No.')}" value="${memberInstance?.telNo}">
          </div>
        </div>

        <div class="control-group ${hasErrors(bean: memberInstance, field: 'gender', 'error')}">
          <label class="control-label">เพศ</label>
          <div class="controls">
            <div class="btn-group" data-toggle="buttons-radio">

              <label class="btn ${memberInstance?.gender == Gender?.MALE? 'active': null}">ชาย<input style="display:none;" type="radio" name="gender" ${memberInstance?.gender == Gender?.MALE? 'checked': null} value="MALE"/> </label>
              <label class="btn ${memberInstance?.gender == Gender?.FEMALE? 'checked': null}">หญิง<input style="display:none;" type="radio" name="gender" ${memberInstance?.gender == Gender?.FEMALE? 'checked': null} value="FEMALE"/> </label>
            </div>
          </div>
        </div>

        <div class="control-group ${hasErrors(bean: memberInstance, field: 'address', 'error')}">
          <label class="control-label" for="address">ที่อยู่</label>
          <div class="controls">
            <textarea id="address" name="address" type="text" placeholder="${message(code: 'member.label.address', default: 'Address')}">${memberInstance?.address}</textarea>
          </div>
        </div>


        <div class="form-actions">
          <button class="btn btn-primary" type="submit"><i class="icon-ok icon-white"></i> ปรับปรุง</button>
          <g:link action="show" id="${memberInstance?.id}">ยกเลิก</g:link>
        </div>
      </g:form>
    </div>

  </body>
</html>