<!doctype html>

<r:require modules="bootstrap"/>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="SHORTCUT ICON" href="${resource(dir: 'images', file: 'favicon.ico')}" />
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'tipsy.css')}" />
    
		<g:layoutHead/>
		<r:layoutResources />
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'cashcard.css')}" />
    <g:javascript src="jquery.tipsy.js" />
    
	</head>
	<body>
		<div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="brand" href="${createLink(controller:'member', action:'verifyCard')}"><g:message code="project.name"></g:message></a>
          <ul class="nav">
            <li class="#"><a href="${createLink(controller:'member', action:'verifyCard')}">${message(code: 'main.menu.home', default: 'Home')}</a></li>
            <li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">${message(code: 'main.menu.member', default: 'Member')} <b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="${createLink(controller:'member', action:'create')}">${message(code: 'main.menu.register', default: 'Register')}</a></li>
								<li><a href="${createLink(controller:'member', action:'verifyCard')}">${message(code: 'main.menu.verifyCard', default: 'Verify Card')}</a></li>
								<li><a href="${createLink(controller:'member', action:'list')}">${message(code: 'main.menu.listMember', default: 'List')}</a></li>
							</ul>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">${message(code: 'main.menu.report', default: 'Report')}<b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li><a href="${createLink(controller:'report', action:'balance')}">${message(code: 'main.menu.payment', default: 'สรุปยอดเงินกู้คงค้าง')}</a></li>
                <li><a href="${createLink(controller:'report', action:'dailyTransaction')}">${message(code: 'main.menu.payment', default: 'สรุปรายการรับ/จ่าย')}</a></li>
                <li><a href="${createLink(controller:'report', action:'dailyInterest')}">${message(code: 'main.menu.dailyInterest', default: 'สรุปดอกเบี้ย')}</a></li>
                <li><a href="${createLink(controller:'report', action:'dailyDiff')}">${message(code: 'main.menu.dailyDiff', default: 'สรุปรายการรับ/จ่ายเงิน แทนสหกรณ์อื่น')}</a></li>
                <li><a href="${createLink(controller:'report', action:'dailyDiffReceive')}">${message(code: 'main.menu.dailyDiff', default: 'สรุปรายการรับ/จ่ายเงิน จากสหกรณ์อื่น')}</a></li>
                <li><a href="${createLink(controller:'report', action:'settlement')}">${message(code: 'main.menu.dailyDiff', default: 'สรุป Net Settlement')}</a></li>
                <li><a href="${createLink(controller:'report', action:'relate')}">${message(code: 'main.menu.dailyDiff', default: 'เงินข้ามสหกรณ์')}</a></li>
              </ul>
            </li>
          </ul>

          <sec:ifLoggedIn>
            <div class="btn-group pull-right">

              <a href='#' class="btn dropdown-toggle" data-toggle="dropdown">
                <i class="icon-user"></i>
                <sec:username/>
                <span class="caret"></span>
              </a>

              <ul class="dropdown-menu">
                <li>
                  <g:link controller='logout'>${message(code:'cashcard.logout.label', default: 'Logout')}</g:link>
                </li>
              </ul>

            </div>
            </sec:ifLoggedIn>
        </div>
      </div>
    </div>

    <div class="container">
	    <div class="content row">
        <div class="span10 offset1"><div class="row">
				  <g:layoutBody/>
        </div></div>
				<g:javascript library="application"/>
				<r:layoutResources />
			</div>

			<footer>
				<p align="center">&copy; Opendream</p>
			</footer>
		</div>

	</body>
</html>
