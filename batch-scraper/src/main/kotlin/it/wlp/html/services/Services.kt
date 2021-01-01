package it.wlp.html.services

import it.wlp.html.configs.ConfigProperties
import it.wlp.html.configs.ConfigSchedule
import it.wlp.html.configs.ConfigScrape
import it.wlp.html.controllers.IRunnerController
import it.wlp.html.utils.Parameters
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
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class AsyncServices {

    @Autowired
    lateinit var configProperties: ConfigProperties

    @Autowired
    lateinit var configScrape: ConfigScrape

    @Autowired
    lateinit var configSchedule: ConfigSchedule

    @Autowired
    lateinit var jobLauncher: JobLauncher

    @Autowired
    lateinit var job: Job

    val log = LoggerFactory.getLogger(AsyncServices::class.java.canonicalName)


    @Async("asyncExecutor")
    fun callAsync() : CompletableFuture<Unit>{
         val supplyAsync= CompletableFuture.supplyAsync({
            runBlocking<Unit> {
                withTimeout(configSchedule.timeout) {
                    repeat(configSchedule.repeat) {

                        val maps = mutableMapOf<String, JobParameter>()

                        val arrayLink = JSONArray()

                        maps.put("amazonLink", JobParameter(configScrape.link));
                        maps.put("UUID", JobParameter(UUID.randomUUID().toString().substring(0, 8)));

                        Parameters.keyword = configScrape.keyword

                        val jobParameters = JobParameters(maps);
                        log.info("{IRunnerController} run jobLauncher");
                        val jobExecution = jobLauncher.run(job, jobParameters);

                        delay(configSchedule.delay)
                    }
                }
            }
        })

        return supplyAsync;

    }

}