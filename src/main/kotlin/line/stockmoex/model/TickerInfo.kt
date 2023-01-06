package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TickerInfo(
    @JsonProperty("ticker")
    val ticker: String,
    @JsonProperty("columns")
    val columns: List<String>,
)