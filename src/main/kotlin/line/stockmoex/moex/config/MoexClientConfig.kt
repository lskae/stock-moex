package line.stockmoex.moex.config

import feign.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MoexClientConfig {

    @Bean
    fun feignLogger(): Logger.Level {
        return Logger.Level.FULL
    }
}
