package it.wlp.html.utils

import it.wlp.html.models.AmazonItem
import kotlinx.coroutines.Job
import org.springframework.batch.core.JobExecution
import java.util.concurrent.CompletableFuture
import java.util.regex.Pattern

object Constants {

    const val rgex = """\d{1,3}(?:[.,]\d{3})*(?:[.,]\d{2})"""
    const val dipper = 10
    const val FINISH = "FINISH"
    val newItems = mutableListOf<AmazonItem>()

}

object UtilFunString {


    fun getPrice(strElement: String): Double {

        val pat = Pattern.compile(Constants.rgex).matcher(strElement.replace(",", ".").trim())

        if (pat.find())
            if (pat.group(0).split(".").size > 2)
                return pat.group(0).replaceFirst(".", "").toDouble()
            else
                return pat.group(0).toDouble()

        return 0.0

    }

}


object Parameters{

        val linksScaper = mutableListOf<AmazonItem>()
        val linksScaperFound = mutableListOf<AmazonItem>()
        val linksScaperBlackList = mutableListOf<String>()
        var scraperPattern = mutableMapOf<String,String>()
        var launch : Job? = null
        var jobParameters : JobExecution? = null
        var supplyAsync : CompletableFuture<Unit>? = null

        fun stop(){

            supplyAsync!!.cancel(true)
            launch!!.cancel()
            linksScaper.removeAll(linksScaper)
            linksScaperFound.removeAll(linksScaperFound)
            linksScaperBlackList.removeAll(linksScaperBlackList)
            scraperPattern = mutableMapOf<String,String>()
        }

        fun ckeck(title : String) : Boolean{

            var found = false

            scraperPattern.keys.stream().forEach {
                if(title.contains(it)) {
                    found = true
                    return@forEach
                }
            }

            return found
        }

        fun getPrice(title : String) : Double{

        var price : Double = 0.0

        scraperPattern.keys.stream().forEach {
            if(title.contains(it)) {
                price = UtilFunString.getPrice(scraperPattern.get(it)!!)
                return@forEach
            }
        }

        return price
        }

        fun addScraperParameters(parameters : String){

            if(parameters.contains(",")){
                parameters.split(",").stream().forEach{
                    val scraperElement = it.split("=")
                    scraperPattern.put(scraperElement[0],scraperElement[1])
                }
            }
            else{
                val scraperElement = parameters.split("=")
                scraperPattern.put(scraperElement[0],scraperElement[1])
            }
        }

}