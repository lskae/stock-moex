package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonProperty

data class LastDayPriceResponse(
    @JsonProperty("SECID")
    val secid: String,
    @JsonProperty("SHORTNAME")
    val shortName: String?,
    @JsonProperty("PREVADMITTEDQUOTE")
    val prevAdmitTedQuote: Number?,
)