package it.wlp.html.readers

import it.wlp.html.models.AmazonItem
import it.wlp.html.utils.Constants
import it.wlp.html.utils.Parameters
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component

@Component
class LinkReader : ItemReader<AmazonItem> {

    val log = LoggerFactory.getLogger(LinkReader::class.java.canonicalName)

    @BeforeStep
    fun beforeStep(stepExecution: StepExecution) {

        val jobParameters = stepExecution.getJobExecution().getJobParameters();


        amazonLink  = jobParameters.getString("amazonLink").orEmpty()
        jobParameters.parameters.remove("amazonLink")
        jobParameters.parameters.remove("UUID")

        log.info("[LinkReader] remove value of start parameters, amazonLink = $amazonLink")
    }

    var amazonLink : String? = null


    override fun read(): AmazonItem? {

        if (amazonLink.isNullOrEmpty()) {

            log.info("[LinkReader read] amazonLink is NullOrEmpty, amazonLink = $amazonLink")

            if(Parameters.linksScaper.isEmpty()){

                log.info("[LinkReader read] linksScaper is isEmpty, linksScaper = ${Parameters.linksScaper.isEmpty()}")

                log.info("[LinkReader] reader is FINISH")
                amazonLink = Constants.FINISH
            }
            else{

                log.info("[LinkReader read] amazonLink is NOT NullOrEmpty, amazonLink = $amazonLink")
                log.info("[LinkReader read] pre Parameters.linksScaper size = ${Parameters.linksScaper.size}")

                amazonLink = Parameters.linksScaper.last().link

                Parameters.linksScaper.removeLast()

                log.info("[LinkReader] post Parameters.linksScaper size = ${Parameters.linksScaper.size}")
            }
        }


        when(amazonLink){
            Constants.FINISH ->  {
                return null
            }
            else -> {

                val amazonItem = AmazonItem("AmazonItem", amazonLink!!)

                amazonLink = null

                return amazonItem
            }
        }

    }
}