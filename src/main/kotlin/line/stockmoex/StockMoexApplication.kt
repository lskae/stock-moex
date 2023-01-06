package line.stockmoex

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

@EnableFeignClients
@EnableScheduling
@EnableCaching
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class, RepositoryRestMvcAutoConfiguration::class])
class StockMoexApplication

fun main(args: Array<String>) {
    runApplication<StockMoexApplication>(*args)
}
