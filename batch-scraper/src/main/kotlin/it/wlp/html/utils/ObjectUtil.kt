package it.wlp.html.utils

import it.wlp.html.models.AmazonItem
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
        var keyword : String? = null
        var price : Double = 0.0
}