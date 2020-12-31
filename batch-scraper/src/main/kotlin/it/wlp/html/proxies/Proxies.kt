package it.wlp.html.proxies

import it.wlp.html.configs.ConfigScrape
import it.wlp.html.dtos.NotifyScraperDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.netflix.ribbon.RibbonClient
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@FeignClient(name = "checkout-rest-scraper")
@RibbonClient(name = "checkout-rest-scraper")
interface ScarperProxy {

    @PostMapping("scraper/notify")
    fun notifyToCheckout(@RequestBody listNotifyScraperDTO: List<NotifyScraperDTO>)
}

@Component
class TemplateProxies(){

    @Autowired
    lateinit var configScrape: ConfigScrape

    fun notifyToCheckout(listNotifyScraperDTO: List<NotifyScraperDTO>)
    {
        val restTemplate = RestTemplate();
        restTemplate.postForObject( configScrape.checkoutlink, listNotifyScraperDTO, NotifyScraperDTO::class.java);
    }
}