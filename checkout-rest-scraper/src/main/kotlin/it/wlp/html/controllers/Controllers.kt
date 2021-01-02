package it.wlp.html.controllers

import it.wlp.html.configs.ConfigProperties
import it.wlp.html.dtos.NotifyScraperDTO
import it.wlp.html.services.CheckoutService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/scraper")
class CheckoutController {

    @Autowired
    lateinit var checkoutService: CheckoutService

    @Autowired
    lateinit var configProperties: ConfigProperties

    val log = LoggerFactory.getLogger(CheckoutController::class.java.canonicalName)


    @PostMapping("/notify")
    fun notify(@RequestBody listNotifyScraperDTO: List<NotifyScraperDTO>) {

        try {

            log.info("[CheckoutService] on notify endpoint recieved async listNotifyScraperDTO - ${listNotifyScraperDTO.size}")

            checkoutService.elaborate(listNotifyScraperDTO)

        } catch (e: Exception) {

            log.error("[CheckoutService] on notify ERROR", e)

        }
    }
}
