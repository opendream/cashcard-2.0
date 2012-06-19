package th.co.opendream.cashcard

//import java.lang.RuntimeException

class KettleService {
	def grailsApplication
	
    def extractMember(def file) {
    	def kettle = grailsApplication.config.kettle.engine.path
    	def reposPath = grailsApplication.config.kettle.repository.path

    	file.transferTo(new File("${reposPath}/fileupload/${file.originalFilename}"))

    	def process = "${kettle}/pan.sh -file:${reposPath}/extract_member.ktr -param:memberfile=${reposPath}/fileupload/${file.originalFilename} -param:extension=${file.contentType} -param:filename=${file.originalFilename}".execute()
		def result = process.text
		if(result.contains('ERROR')) {
			throw new RuntimeException(message:"ExtractMembersNotComplete")
			log.error(result)
		}
		result
    }
}
