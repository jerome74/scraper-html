package it.wlp.html.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Amazonitem (@Id
                     @GeneratedValue(strategy = GenerationType.AUTO)
                     var id : Int = 0
                                   , var title : String = ""
                                   , var price : Double = 0.0
                                   , var link : String = "")