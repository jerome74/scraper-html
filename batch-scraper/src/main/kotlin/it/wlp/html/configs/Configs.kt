package it.wlp.html.configs

import it.wlp.html.readers.LinkReader
import it.wlp.html.models.AmazonItem
import it.wlp.html.listeners.JobCompletionListener
import it.wlp.html.processes.WebScrapingProcess
import it.wlp.html.writers.ConsoleWriter
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.util.concurrent.Executor

@Configuration
@EnableBatchProcessing
class AmazonConfiguration(){

    @Autowired
    lateinit var itemWriter: ConsoleWriter

    @Autowired
    lateinit var itemReader: LinkReader

    @Autowired
    lateinit var itemProcessor: WebScrapingProcess

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var jobCompletionListener: JobCompletionListener

    @Bean
    open fun Job(): Job {
        return jobBuilderFactory.get("scraper-job")
                .listener(jobCompletionListener)
                .start(scraperStep())
                .build()
    }

    open fun scraperStep(): Step {
        return stepBuilderFactory.get("scraper-step")
                .chunk<AmazonItem, List<AmazonItem>>(1)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

}

@Component
@PropertySource("classpath:application.yml")
class ConfigProperties
{
    @Autowired
    lateinit var env : Environment

    fun getPropertes(prop: String) : String? = env.getProperty(prop)
}

@Component
@ConfigurationProperties(prefix = "scraping")
class ConfigScrape{
    var link = ""
    var parameters = ""
    var checkoutlink = ""
}

@Component
@ConfigurationProperties(prefix = "scheduling")
class ConfigSchedule{
    var repeat : Int = 0
    var timeout : Long = 0L
    var delay : Long = 0L
}

@EnableAsync
class BatchRestAsyncConfigurer : AsyncConfigurer {

    @Bean("asyncExecutor")
    override fun getAsyncExecutor(): Executor? {
        val executor = ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("BatchRestAsync-");
        executor.initialize();
        return executor;
    }


}