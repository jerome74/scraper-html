package it.wlp.html.processes


import it.wlp.html.models.AmazonItem
import it.wlp.html.utils.Constants
import it.wlp.html.utils.Parameters
import it.wlp.html.utils.UtilFunString
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class WebScrapingProcess : ItemProcessor<AmazonItem, List<AmazonItem>> {

    val log = LoggerFactory.getLogger(WebScrapingProcess::class.java.canonicalName)

    override fun process(item: AmazonItem): List<AmazonItem>? {

        val amazonItems = mutableListOf<AmazonItem>()

        val doc = Jsoup.connect(item.link).get();
        val priceblock_ourprice : org.jsoup.nodes.Element? = doc.body().getElementById("priceblock_ourprice")

        priceblock_ourprice?.let {
            if(doc.title().contains(Parameters.keyword!!) && !Parameters.linksScaperBlackList.contains(doc.title()) ) {

                Parameters.linksScaperFound.add(AmazonItem(doc.title(), UtilFunString.getPrice(it.text()), item.link))
                log.info("[WebScrapingProcess](priceblock_ourprice) add - ${doc.title()} - AmazonItem to linksScaperFound size = ${Parameters.linksScaperFound.size}")
            }
        }

        val olp_text_box  = doc.body().getElementsByClass("olp-text-box").select("span[class=a-size-base a-color-base]")

        if(olp_text_box.size > 0){
                if(doc.title().contains(Parameters.keyword!!) ) {

                    Parameters.linksScaperFound.add(AmazonItem(doc.title(), UtilFunString.getPrice(olp_text_box.first().text()), item.link))
                    log.info("[WebScrapingProcess](olp_text_box) add - ${doc.title()} - AmazonItem to linksScaperFound size = ${Parameters.linksScaperFound.size}")
                }
        }

        val a_href_s = doc.body().select("a[class=a-link-normal a-text-normal]")


        a_href_s.stream().filter { it.attr("href").contains("/dp/")
                                    && amazonItems.size <= Constants.dipper
                                }
                .forEach {

                            val a_href_Doc = Jsoup.connect("https://www.amazon.it${it.attr("href")}").get()

                            if(a_href_Doc.title().contains(Parameters.keyword!!) && !Parameters.linksScaperBlackList.contains(a_href_Doc.title())) {
                                // inserisco in array di appoggio tutti i titoli priva di riversarli nella lista sraper
                                Parameters.linksScaperBlackList.add(a_href_Doc.title())

                                val amazonItem = AmazonItem(a_href_Doc.title(), UtilFunString.getPrice(it.text()), "https://www.amazon.it${it.attr("href")}")

                                amazonItems.add(amazonItem)
                                Parameters.linksScaperFound.add(amazonItem)

                                log.info("[WebScrapingProcess](a_href_s) add - ${a_href_Doc.title()} - AmazonItem")
                                log.info("[WebScrapingProcess](a_href_s) to amazonItems size = ${amazonItems.size}")
                                log.info("[WebScrapingProcess](a_href_s) to linksScaperFound size = ${amazonItems.size}")
                            }
                    }


        return amazonItems
    }
}