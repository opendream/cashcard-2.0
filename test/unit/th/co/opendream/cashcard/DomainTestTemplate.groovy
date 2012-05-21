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

    void verifyNotNull(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail null validation.",
            "nullable", instance.errors[field]
    }

    void verifyNotBlank(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail blank validation.",
            "blank", instance.errors[field]
    }

    void verifyUnique(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail unique validation.",
            "unique", instance.errors[field]
    }

    void verifyPass(instance, field) {
        assertTrue "${field} value = ${instance[field]} must pass all validations.",
            instance.validate([field])
    }

    void verifyFinanceNumber(clazz, field) {
        mockForConstraintsTests(clazz)

        def _test = { fieldName ->
            def instance = clazz.newInstance()

            verifyNotNull(instance, fieldName)

            instance[fieldName] = 1000.000000
            verifyPass(instance, fieldName)
        }

        if (!(field instanceof ArrayList)) {
            field = [field]
        }

        field.each {
            _test(it)
        }
    }
}