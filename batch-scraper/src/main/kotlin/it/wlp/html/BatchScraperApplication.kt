package it.wlp.html

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BatchScraperApplication

fun main(args: Array<String>) {
	runApplication<BatchScraperApplication>(*args)
}
