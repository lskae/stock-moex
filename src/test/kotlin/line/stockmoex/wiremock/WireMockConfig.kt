package line.stockmoex.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/* Конфигурация mock web сервера */
@Configuration
class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun mockBooksService(): WireMockServer {
        return WireMockServer(9999)
    }
}