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

        log.info("[LinkReader] remove value of start parameters, amazonLink = $amazonLink, price = $price ")
    }

    var amazonLink : String? = null
    var price : Double? = 0.0


    override fun read(): AmazonItem? {

        if (amazonLink.isNullOrEmpty()) {

            if(Parameters.linksScaper.isEmpty()){

                log.info("[LinkReader] reader is FINISH")
                amazonLink = Constants.FINISH
            }
            else{

                log.info("[LinkReader] pre Parameters.linksScaper size = ${Parameters.linksScaper.size}")

                amazonLink = Parameters.linksScaper.last().link
                price = Parameters.linksScaper.last().price

                Parameters.linksScaper.removeLast()

                log.info("[LinkReader] post Parameters.linksScaper size = ${Parameters.linksScaper.size}")
            }
        }


        when(amazonLink){
            Constants.FINISH ->  {
                return null
            }
            else -> {

                val amazonItem = AmazonItem("AmazonItem", 0.0, amazonLink!!)

                amazonLink = null

                return amazonItem
            }
        }

    }
}