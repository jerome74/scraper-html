package it.wlp.html

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import javax.ws.rs.core.UriBuilder

@SpringBootApplication
@EnableDiscoveryClient
class CheckoutRestSraperApplication

fun main(args: Array<String>) {
	runApplication<CheckoutRestSraperApplication>(*args)
}
