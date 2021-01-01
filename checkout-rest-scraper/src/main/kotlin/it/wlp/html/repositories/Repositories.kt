package it.wlp.html.repositories

import it.wlp.html.entities.Amazonitem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AmazonRepository : JpaRepository<Amazonitem, Int>{

    fun findByTitle(title: String): Optional<Amazonitem>
}

