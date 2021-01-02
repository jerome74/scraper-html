package it.wlp.html.utils

import com.google.gson.Gson
import it.wlp.html.configs.ConfigTelegram
import it.wlp.html.repositories.AmazonRepository
import it.wlp.html.services.CheckoutService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.regex.Pattern
import javax.ws.rs.core.UriBuilder
import kotlin.jvm.Throws
import kotlin.streams.toList

object Constants {

    const val rgex = """\d{1,3}(?:[.,]\d{3})*(?:[.,]\d{2})"""
}

object UtilFunString {

    fun getPrice(strElement: String): Double {

        val pat = Pattern.compile(Constants.rgex).matcher(strElement.replace(",", ".").trim())

        if (pat.find())
            if (pat.group(0).split(".").size > 2)
                return pat.group(0).replaceFirst(".", "").toDouble()
            else
                return pat.group(0).toDouble()

        return 0.0

    }
}

object TelegramUtil{

    @Throws(Exception::class)
    fun notifyOnTelegram(text: String, apitoken : String, chatid : String ) {

        val message = text
        val TOKEN = apitoken
        val CHAT_ID = chatid

        val client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();

        val builder = UriBuilder
                .fromUri("https://api.telegram.org")
                .path("/{token}/sendMessage")
                .queryParam("chat_id",CHAT_ID )
                .queryParam("text", message);

        val request = HttpRequest.newBuilder()
                .GET()
                .uri(builder.build("bot" + TOKEN))
                .timeout(Duration.ofSeconds(5))
                .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
        println(response.body())

    }
}

@Component
class NotifyTelegram : TelegramLongPollingBot() {

    @Autowired
    lateinit var configTelegram: ConfigTelegram

    @Autowired
    lateinit var amazonRepository: AmazonRepository

    val log = LoggerFactory.getLogger(NotifyTelegram::class.java.canonicalName)

    override fun getBotToken(): String {
        return configTelegram.apitoken
    }

    override fun getBotUsername(): String {
        return configTelegram.username
    }

    @Throws(TelegramApiException::class)
    override fun onUpdateReceived(update: Update?) {

        if (update!!.hasMessage()) {
            val message = update.getMessage();
            val text = message.getText().trim();

            val list = amazonRepository.findAll().stream().filter {
                it.title.indexOf("$text") != -1 }.forEach {
                TelegramUtil.notifyOnTelegram("${it.link}\n\n price to monitoring=${it.price}", configTelegram.apitoken, configTelegram.chatid)
            }

            /*log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
            val gson = Gson().toJson(list)
            log.info("[NotifyTelegram] gson=$gson")
            var moritoringElement = "key ${text.trim()} produce this list: \n\n $gson"
            log.info("[NotifyTelegram] moritoringElement=$moritoringElement")
            log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")

            TelegramUtil.notifyOnTelegram(moritoringElement, configTelegram.apitoken, configTelegram.chatid)*/
        }
    }
}