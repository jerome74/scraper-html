package it.wlp.html.services

import it.wlp.html.configs.ConfigTelegram
import it.wlp.html.dtos.NotifyScraperDTO
import it.wlp.html.entities.Amazonitem
import it.wlp.html.repositories.AmazonRepository
import it.wlp.html.utils.NotifyTelegram
import it.wlp.html.utils.TelegramUtil
import it.wlp.html.utils.UtilFunString
import org.json.JSONArray
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.ws.rs.core.UriBuilder
import kotlin.jvm.Throws
import kotlin.streams.toList

@Service
class CheckoutService {

    @Autowired
    lateinit var amazonRepository: AmazonRepository

    @Autowired
    lateinit var notifyTelegram : NotifyTelegram

    @Autowired
    lateinit var configTelegram : ConfigTelegram


    @Async("asyncExecutor")
    fun callAsyncElaborate(listNotifyScraperDTO: List<NotifyScraperDTO>) : CompletableFuture<Unit> {
        val supplyAsync= CompletableFuture.supplyAsync({
            elaborate(listNotifyScraperDTO)
        })

        return supplyAsync;

    }

    val log = LoggerFactory.getLogger(CheckoutService::class.java.canonicalName)

    fun elaborate(listNotifyScraperDTO: List<NotifyScraperDTO>) {

        listNotifyScraperDTO.stream().filter {
            !amazonRepository.findByTitle(it.title).isPresent
        }
                .map {
                    Amazonitem(0, it.title, it.price, it.link)
                }.toList().stream().forEach {

                    log.info("[CheckoutService] save new Item ${it.title}")
                    amazonRepository.save(it)
                }

        log.info("__________________________________________________________")
        log.info("[CheckoutService] retrive al Amazonitem to check the price")
        log.info("__________________________________________________________")
        amazonRepository.findAll().stream().forEach {

            val doc = Jsoup.connect(it.link).get();
            val priceblock_ourprice: org.jsoup.nodes.Element? = doc.body().getElementById("priceblock_ourprice")
            val price = it.price
            val link = it.link
            val title = it.title
            log.info("#########################################")
            log.info("[CheckoutService] check AmazonItem $title")

            priceblock_ourprice?.let {

                val actualPrice = UtilFunString.getPrice(it.text())
                val desirePrice = price


                if (actualPrice <= desirePrice) {

                    log.info("[CheckoutService](priceblock_ourprice) price is ok! - $actualPrice vs $desirePrice have to notify")

                    //********************
                    TelegramUtil.notifyOnTelegram("Article [${doc.title()}] price is ok! - $link , actualPrice=$actualPrice end desirePrice=$desirePrice"
                    , configTelegram.apitoken, configTelegram.chatid)
                    //********************
                }
                else{
                    log.info("[CheckoutService](priceblock_ourprice) price is NOT ok! - $actualPrice vs $desirePrice too high")
                }
            }

            val olp_text_box = doc.body().getElementsByClass("olp-text-box").select("span[class=a-size-base a-color-base]")

            olp_text_box.stream().findFirst().ifPresent {

                val actualPrice = UtilFunString.getPrice(it.text())
                val desirePrice = price

                if (actualPrice <= desirePrice) {

                    log.info("[CheckoutService](olp-text-box) price is ok! - $actualPrice vs $desirePrice have to notify")

                    //********************
                    TelegramUtil.notifyOnTelegram("Article [${doc.title()}] price is ok! - $link , actualPrice=$actualPrice end desirePrice=$desirePrice"
                    , configTelegram.apitoken, configTelegram.chatid)
                    //********************
                }
                else{
                    log.info("[CheckoutService](olp-text-box) price is NOT ok! - $actualPrice vs $desirePrice too high")
                }

            }
            log.info("#########################################")
        }

    }
}

