package it.wlp.html.utils

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import org.springframework.web.client.postForObject
import org.springframework.web.util.UriComponentsBuilder

object RestUtil{

    fun callRestEndpoit(httpUrl : String,  ){

        val restTemplate    = RestTemplate()
        val builder         = UriComponentsBuilder.fromHttpUrl(httpUrl)

    }

}