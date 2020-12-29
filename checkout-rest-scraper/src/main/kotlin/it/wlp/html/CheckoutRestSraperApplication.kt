package it.wlp.html

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class CheckoutRestSraperApplication

fun main(args: Array<String>) {
	runApplication<CheckoutRestSraperApplication>(*args)
}
