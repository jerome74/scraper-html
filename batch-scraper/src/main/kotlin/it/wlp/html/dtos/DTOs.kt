package it.wlp.html.dtos

data class RequestScraperDTO(var external: Boolean, var link : String, var parameters : String)
data class NotifyScraperDTO(var title : String, var price: Double, var link : String)