package line.stockmoex

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@EnableFeignClients
@EnableScheduling
@EnableCaching
@EnableJpaRepositories(basePackages = ["line.stockmoex"])
@SpringBootApplication
class StockMoexApplication

fun main(vararg args: String) {
    runApplication<StockMoexApplication>(*args)
}
