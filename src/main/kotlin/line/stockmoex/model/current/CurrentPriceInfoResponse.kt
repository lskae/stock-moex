package line.stockmoex.model.current

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Лист CurrentPriceInfo")
data class CurrentPriceInfoResponse(

    @field:Schema(description = "Лист CurrentPriceInfo")
    @JsonProperty("currentInfoResponse")
    val currentPriceInfoResponse: List<CurrentPriceInfo>
)
