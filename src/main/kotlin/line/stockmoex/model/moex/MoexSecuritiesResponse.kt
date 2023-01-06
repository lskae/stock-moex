package line.stockmoex.model.moex

import com.fasterxml.jackson.annotation.JsonProperty

data class MoexSecuritiesResponse(
    @JsonProperty("securities")
    val securities: Securities,
)