package line.stockmoex.model.moex

import com.fasterxml.jackson.annotation.JsonProperty

data class MarketData(
    @JsonProperty("columns")
    val columns: List<String>,
    @JsonProperty("data")
    val data: List<List<Any>>,
)
