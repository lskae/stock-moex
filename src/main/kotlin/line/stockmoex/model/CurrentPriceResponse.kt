package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CurrentPriceResponse(
    @JsonProperty("SECID")
    val secid: String,
    @JsonProperty("LAST")
    val last: String?,
    @JsonProperty("LOW")
    val low: String?,
    @JsonProperty("HIGH")
    val high: String?,
)
