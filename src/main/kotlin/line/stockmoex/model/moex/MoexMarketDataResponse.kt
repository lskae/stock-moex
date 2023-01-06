package line.stockmoex.model.moex

import com.fasterxml.jackson.annotation.JsonProperty

data class MoexMarketDataResponse(
    @JsonProperty("marketdata")
    val marketData: MarketData,
)