package it.wlp.html.configs

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
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.concurrent.Executor


@Component
@PropertySource("classpath:application.yml")
class ConfigProperties
{
    @Autowired
    lateinit var env : Environment

    fun getPropertes(prop : String) : String? = env.getProperty(prop)
}

@Configuration
@EnableSwagger2
class Configs {
    @Bean
    public fun api() : Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("it.wlp.html.controllers"))
                .paths(PathSelectors.regex("/checkout-rest-scraper.*"))
                .build();
    }
}

@Component
@ConfigurationProperties(prefix = "telegram")
class ConfigTelegram{
    var urlstring = ""
    var apitoken = ""
    var chatid = ""
    var username = ""
}