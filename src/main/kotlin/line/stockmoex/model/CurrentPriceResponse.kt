package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CurrentPriceResponse(
    @JsonProperty("SECID")
    val secid: String,
    @JsonProperty("LAST")
    val last: Number?,
    @JsonProperty("LOW")
    val low: Number?,
    @JsonProperty("HIGH")
    val high: Number?,
)