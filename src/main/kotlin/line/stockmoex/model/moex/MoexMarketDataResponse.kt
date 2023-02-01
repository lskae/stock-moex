package line.stockmoex.model.moex

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ Московской Биржи (параметр marketdata)")
data class MoexMarketDataResponse(

    @field:Schema(description = "Объект ответа Московской Биржи (параметр marketdata)")
    @JsonProperty("marketdata")
    val marketData: MarketData,
)
