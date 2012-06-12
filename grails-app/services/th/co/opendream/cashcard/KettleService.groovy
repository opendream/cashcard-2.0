package th.co.opendream.cashcard

//import java.lang.RuntimeException

class KettleService {
	def grailsApplication
	
    def extractMember(def filename, def extension) {
    	def kettle = grailsApplication.config.kettle.engine.path
    	def reposPath = grailsApplication.config.kettle.repository.path

    	def process = "${kettle}/pan.sh -file:${reposPath}/extract_member.ktr -param:memberfile=${reposPath}/fileupload/${filename} -param:extension=${extension}".execute()
		def result = process.text
		if(result.contains('ERROR')) {
			throw new RuntimeException(message:"ExtractMembersNotComplete")
			log.error(result)
		}
		result
    }
}
