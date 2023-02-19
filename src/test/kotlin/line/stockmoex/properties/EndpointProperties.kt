package line.stockmoex.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Configuration
@ConfigurationProperties(prefix = "stock.endpoint")
@Validated
class EndpointProperties {
    lateinit var moexCurrentPrice: String
    lateinit var moexPreviousDayPrice: String
}
