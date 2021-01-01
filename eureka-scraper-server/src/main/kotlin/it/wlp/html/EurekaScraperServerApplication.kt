package it.wlp.html

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class EurekaScraperServerApplication

fun main(args: Array<String>) {
	runApplication<EurekaScraperServerApplication>(*args)
}
