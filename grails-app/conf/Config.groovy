// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }


grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']


// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// set per-environment serverURL stem for creating absolute links
environments {
    development {
        grails.logging.jul.usebridge = true
        // Added by openmsngr-grails-client plugin:
        openmsngr.grails.client.uri='http://dminer.in.th:8090'
        openmsngr.grails.client.auth='/OpenMessenger/api/auth'
        openmsngr.grails.client.ping='/OpenMessenger/api/ping'
        openmsngr.grails.client.sendMessage='/OpenMessenger/api/event/sendPersonalMessage'
        openmsngr.grails.client.eventId='1'
        openmsngr.grails.client.username='admin'
        openmsngr.grails.client.password='openpubyesroti!'

        // Added by openmsngr-grails-client plugin: (config rest plugin)
        rest.https.keystore.path='web-app/certs/messenger.jks'
        rest.https.keystore.pass='openpubyesroti!'
        rest.https.cert.hostnameVerifier='ALLOW_ALL'

        //path to Kettle home
        //kettle.engine.path='/home/cashcard/data-integration'
        kettle.engine.path='/home/boyone/data-integration'
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"

        // Added by openmsngr-grails-client plugin:
        openmsngr.grails.client.uri='https://messenger.opendream.org'
        openmsngr.grails.client.auth='/api/auth'
        openmsngr.grails.client.ping='/api/ping'
        openmsngr.grails.client.sendMessage='/api/sendPersonalMessage'
        openmsngr.grails.client.eventId=''
        openmsngr.grails.client.username=''
        openmsngr.grails.client.password=''

        // Added by openmsngr-grails-client plugin: (config rest plugin)
        rest.https.keystore.path='web-app/certs/messenger.jks'
        rest.https.keystore.pass='openpubyesroti!'
        rest.https.cert.hostnameVerifier='ALLOW_ALL'

        //path to Kettle home
        kettle.engine.path='/home/cashcard/data-integration'
    }
    test {
        grails.logging.jul.usebridge = false

        //path to Kettle home
        kettle.engine.path='/home/boyone/data-integration'        
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}

grails.plugins.springsecurity.successHandler.defaultTargetUrl="/member/verifyCard"

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'th.co.opendream.cashcard.Users'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'th.co.opendream.cashcard.UsersRole'
grails.plugins.springsecurity.authority.className = 'th.co.opendream.cashcard.Role'
grails.plugins.springsecurity.requestMap.className = 'th.co.opendream.cashcard.RequestMap'
grails.plugins.springsecurity.securityConfigType = 'Requestmap'
grails.plugins.springsecurity.useBasicAuth = true
grails.plugins.springsecurity.basic.realmName = "Cash Card"
grails.plugins.springsecurity.filterChain.chainMap = [
   '/api**': 'JOINED_FILTERS,-exceptionTranslationFilter',
   '/api/**': 'JOINED_FILTERS,-exceptionTranslationFilter',
   '/**': 'JOINED_FILTERS,-basicAuthenticationFilter,-basicExceptionTranslationFilter'
]

jasper.dir.reports = '/reports'

kettle.repository.path='/kettle'