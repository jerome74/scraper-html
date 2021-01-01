package it.wlp.html.utils

import it.wlp.html.configs.ConfigTelegram
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.regex.Pattern
import kotlin.jvm.Throws

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

@Component
class NotifyTelegram : TelegramLongPollingBot() {

    @Autowired
    lateinit var configTelegram: ConfigTelegram

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
            val response =  SendMessage();
            val chatId = message.getChatId();
            response.setChatId(chatId.toString());
            val text = message.getText();
            response.setText(text);

            execute(response);
            log.info("Sent message \"{}\" to {}", text, chatId);

        }
    }
}