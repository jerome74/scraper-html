package it.wlp.html.proxies

import it.wlp.html.dtos.NotifyScraperDTO
import org.springframework.cloud.netflix.ribbon.RibbonClient
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@FeignClient("checkout-rest-scraper")
@RibbonClient("checkout-rest-scraper")
interface ScarperProxy {

    @PostMapping("checkout-rest-scraper/scraper/notify")
    fun notify(@RequestBody listNotifyScraperDTO: List<NotifyScraperDTO>): ResponseEntity<String>
}