package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Лист LastDayPriceInfoResponse")
data class LastDayPriceInfoResponse(

    @field:Schema(description = "Лист LastDayPriceInfo")
    @JsonProperty("lastDayPriceInfo")
    val lastDayPriceInfo: List<LastDayPriceInfo>
)
