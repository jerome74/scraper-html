package it.wlp.html.dtos

data class RequestScraperDTO(var price: Double, var link : String, var keyword : String)
data class NotifyScraperDTO(var title : String, var price: Double, var link : String)