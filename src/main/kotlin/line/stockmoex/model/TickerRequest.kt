package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TickerRequest(
    @JsonProperty("tickerList")
    val tickerList: List<String>,
)
