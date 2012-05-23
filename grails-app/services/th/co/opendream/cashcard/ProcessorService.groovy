package th.co.opendream.cashcard

class ProcessorService {

    def process(processor) {
        def processorName = processor.toLowerCase()
        this."$processorName"()
    }

    def effective() {
        'effective'
    }

    def flat() {
        'flat'
    }

    def hybrid() {
        'hybrid'
    }
}
