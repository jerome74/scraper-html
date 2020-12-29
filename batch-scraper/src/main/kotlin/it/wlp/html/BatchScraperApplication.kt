package it.wlp.html

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = ["it.wlp.api.html.proxies"])
class BatchScraperApplication

fun main(args: Array<String>) {
	runApplication<BatchScraperApplication>(*args)
}
