package th.co.opendream.cashcard

import grails.test.mixin.*
import org.junit.*

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

abstract class DomainTestTemplate {

    def mustTestProperties = true

    abstract def requiredProperties()
    abstract def domainClass()

    void testProperties() {
        if (mustTestProperties) {
            def defaultDomainClass = new DefaultGrailsDomainClass(domainClass())

            def domainProperties = defaultDomainClass.persistentProperties*.name
            def missing_properties = requiredProperties() - domainProperties
            assert 0 == missing_properties.size(),
                "Domain class is missing some required properties => ${missing_properties}"
        }
    }
}