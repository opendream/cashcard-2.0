package th.co.opendream.cashcard

import groovy.text.SimpleTemplateEngine

class RunNoService {

	def engine = new SimpleTemplateEngine()

    def next(key, date= new Date()) {
    	def runno

        while (1) {
            try {
                runno = RunNo.findByKey(key)
                runno.currentNo += 1
                runno.save()
                break
            } catch (org.hibernate.StaleObjectStateException se) {
                // do it again
            }
        }

        return format(runno, date)
    }

    def hasKey(key) {
        return RunNo.findByKey(key) ? true : false
    }

    def currentNumberString(currentNo, padSize) {
    	currentNo = currentNo.toString()
        def zeroSize = (padSize) ? '0' * (padSize - currentNo.size()) : ''
        return zeroSize + currentNo
    }

    def format(runno, today) {
        def template
        if (runno.template) {
            template = runno.template
        } else {
            def prefix = runno.prefix ?: ""
            def suffix = runno.suffix ?: ""
            template = prefix + '{current()}' + suffix
        }

        def tmp = engine.createTemplate(template.replace("{current(", "\${current("))
        def result = tmp.make([
            mm  : (today.month+1>9) ? ""+(today.month+1) : "0"+(today.month+1),
            Ayy : (today.year + 1900).toString().getAt(2..3),
            Byy : (today.year + 1900 +543).toString().getAt(2..3),
            Ayyyy : (today.year + 1900).toString(),
            Byyyy : (today.year + 2443).toString(),
            current : { size ->
            	currentNumberString(runno.currentNo, size ?: runno.padSize)
            }
        ])
        return result.toString()
    }
}
