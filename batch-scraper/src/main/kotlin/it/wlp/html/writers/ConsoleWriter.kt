package it.wlp.html.writers

import it.wlp.html.models.AmazonItem
import it.wlp.html.utils.Parameters
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class ConsoleWriter : ItemWriter<List<AmazonItem>> {

    val log = LoggerFactory.getLogger(ConsoleWriter::class.java.canonicalName)

    override fun write(items: MutableList<out List<AmazonItem>>) {


        log.info("[ConsoleWriter] linksScaper size = ${Parameters.linksScaper.size}")

        if(items != null)
        {
            items[0].stream().forEach {
                log.info("amazon item : title={}, price={}", it.title, it.price);
                }

            Parameters.linksScaper.addAll(items[0])
            items[0].toMutableList().removeAll(items[0])
            log.info("[ConsoleWriter] post linksScaper size = ${Parameters.linksScaper.size}")

            Parameters.linksScaperFound.stream().forEach {
                log.info("{linksScaperFound} amazon item : title={}, price={}", it.title, it.price);
            }
        }
    }
}