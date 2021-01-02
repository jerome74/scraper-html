package it.wlp.html.writers

import it.wlp.html.configs.ConfigScrape
import it.wlp.html.dtos.NotifyScraperDTO
import it.wlp.html.models.AmazonItem
import it.wlp.html.proxies.ScarperProxy
import it.wlp.html.utils.Parameters
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.streams.toList

@Component
class ConsoleWriter : ItemWriter<List<AmazonItem>> {

    val log = LoggerFactory.getLogger(ConsoleWriter::class.java.canonicalName)

    @Autowired
    lateinit var scarperProxy: ScarperProxy

    @Autowired
    lateinit var configScrape: ConfigScrape

    override fun write(items: MutableList<out List<AmazonItem>>) {


        log.info("[ConsoleWriter] linksScaper size = ${Parameters.linksScaper.size}")

        if (items != null) {
            items[0].stream().forEach {
                log.info("amazon item : title={}", it.title);
            }

            Parameters.linksScaper.addAll(items[0])
            items[0].toMutableList().removeAll(items[0])
            log.info("[ConsoleWriter] post linksScaper size = ${Parameters.linksScaper.size}")
            log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            Parameters.linksScaperFound.stream().forEach {
                log.info("{linksScaperFound} amazon item : title={}", it.title);
            }
            log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
        }

        runBlocking<Unit> {
            callCheckout()
        }
    }

    suspend fun callCheckout() {
        if (Parameters.linksScaperFound.isNotEmpty()) {
            log.info("--------------------------------------------");
            log.info("{linksScaperFound} notify via callCheckout()");
            scarperProxy.notifyToCheckout(Parameters.linksScaperFound.stream().map { NotifyScraperDTO(it.title, Parameters.getPrice(it.title), it.link) }.toList())
            log.info("--------------------------------------------");
        }

    }

}
