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
    <link rel="SHORTCUT ICON" href="${resource(dir: 'images', file: 'credit-union-logo-blue.png')}" />
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'credit-union-logo.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'credit-union-logo.png')}">

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
          <a class="brand" href="${createLink(controller:'member', action:'verifyCard')}">
            <r:img uri="/images/credit-union-logo.png" />
            <g:message code="project.name" />
          </a>
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
                <li><a href="${createLink(controller:'report', action:'overdue')}">${message(code: 'main.menu.overduepaymentReport', default: 'รายงานหนี้ที่เกินวันกำหนดชำระจ่าย')}</a></li>
                <li><a href="${createLink(controller:'report', action:'payment', params:[paymenttype:'daily-payloan'])}">${message(code: 'main.menu.payloanReport', default: 'รายงานการจ่ายเงินกู้')}</a></li>
                <li><a href="${createLink(controller:'report', action:'payment', params:[paymenttype:'daily-payoff'])}">${message(code: 'main.menu.payoffReport', default: 'รายงานการชำระเงินกู้ของลูกหนี้')}</a></li>                
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
