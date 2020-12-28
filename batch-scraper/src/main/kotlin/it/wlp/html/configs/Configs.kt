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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

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