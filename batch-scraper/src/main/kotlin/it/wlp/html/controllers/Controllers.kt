package it.wlp.html.controllers

import it.wlp.html.configs.ConfigProperties
import it.wlp.html.configs.ConfigSchedule
import it.wlp.html.configs.ConfigScrape
import it.wlp.html.dtos.RequestScraperDTO
import it.wlp.html.services.AsyncServices
import it.wlp.html.utils.Parameters
import it.wlp.html.utils.UtilFunString
import it.wlp.html.writers.ConsoleWriter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/api-rest")
class IRunnerController {


    @Autowired
    lateinit var asyncServices: AsyncServices

    @Autowired
    lateinit var configSchedule: ConfigSchedule

    @Autowired
    lateinit var configProperties: ConfigProperties


    val log = LoggerFactory.getLogger(IRunnerController::class.java.canonicalName)

    @PostMapping(path = ["/scraping"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun scraping(@RequestBody request : RequestScraperDTO) : ResponseEntity<String> {

        log.info("{IRunnerController} receive via scraping() timeout=${configSchedule.timeout}, repeat=${configSchedule.repeat}, delay=${configSchedule.delay}");

        if(request.external){ log.info("{IRunnerController} start by external git parameters");  Parameters.supplyAsync = asyncServices.callAsyncGit() }
        else                { log.info("{IRunnerController} start by request parameters");       Parameters.supplyAsync = asyncServices.callAsyncRequest(request) }

        return ResponseEntity.ok()
                .contentLength(configProperties.getPropertes("scraping.message.ok")!!.length.toLong())
                .contentType(MediaType.TEXT_PLAIN)
                .body(configProperties.getPropertes("scraping.message.ok"))
    }

    @GetMapping(path = ["/stop"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun scraping() : ResponseEntity<String> {

        log.info("{IRunnerController} stop process batch, is isDone = ${Parameters.supplyAsync!!.isDone}");

        Parameters.stop()

        log.info("{IRunnerController} stopped process is isCancelled -> batch=${Parameters.supplyAsync!!.isCancelled}")
        log.info("{IRunnerController} stopped process isActive ->  launch=${ Parameters.launch!!.isActive}")

        return ResponseEntity.ok()
                .contentLength(configProperties.getPropertes("scraping.message.stop")!!.length.toLong())
                .contentType(MediaType.TEXT_PLAIN)
                .body(configProperties.getPropertes("scraping.message.stop"))
    }


}