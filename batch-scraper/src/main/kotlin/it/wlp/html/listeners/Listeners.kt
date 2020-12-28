package it.wlp.html.listeners

import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.stereotype.Component

@Component
class JobCompletionListener : JobExecutionListenerSupport() {

    val log = LoggerFactory.getLogger(JobCompletionListener::class.java.canonicalName)

    override fun afterJob(jobExecution : JobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            //implementare il nuovo repository ( per salvare i found ) che con un nuovo step verificherà i prezzi
            // e l'eventuale inserimento di nuovo link
            log.info("BATCH JOB COMPLETED SUCCESSFULLY");
        }
    }

}