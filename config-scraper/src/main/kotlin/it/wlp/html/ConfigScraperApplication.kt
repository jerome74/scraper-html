package it.wlp.html

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@SpringBootApplication
@EnableConfigServer
class ConfigScraperApplication

fun main(args: Array<String>) {
	runApplication<ConfigScraperApplication>(*args)
}
