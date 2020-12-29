package it.wlp.html.controllers

import it.wlp.html.configs.ConfigProperties
import it.wlp.html.configs.ConfigScrape
import it.wlp.html.utils.Parameters
import it.wlp.html.utils.UtilFunString
import org.json.JSONArray
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api-rest")
class IRunnerController {


    @Autowired
    lateinit var configProperties: ConfigProperties

    @Autowired
    lateinit var configScrape: ConfigScrape

    @Autowired
    lateinit var jobLauncher: JobLauncher

    @Autowired
    lateinit var job: Job


    @GetMapping(path = ["/scraping"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun scraping(): ResponseEntity<String> {

        val  maps = mutableMapOf<String, JobParameter>()

        val arrayLink = JSONArray()

        maps.put("amazonLink", JobParameter(configScrape.link));

        Parameters.keyword = configScrape.keyword
        Parameters.price = UtilFunString.getPrice(configScrape.price)

        val jobParameters = JobParameters(maps);
        val jobExecution = jobLauncher.run(job, jobParameters);

        return ResponseEntity.ok()
                .contentLength(configProperties.getPropertes("scraping.message.ok")!!.length.toLong())
                .contentType(MediaType.TEXT_PLAIN)
                .body(configProperties.getPropertes("scraping.message.ok"))
    }
}