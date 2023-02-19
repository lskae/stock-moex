package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Контейнер для листа объектов PreviousDayPriceInfo")
data class PreviousDayPriceInfoResponse(

    @field:Schema(description = "Лист PreviousDayPriceInfo")
    @JsonProperty("previousDayPriceInfoList")
    val previousDayPriceInfoList: List<PreviousDayPriceInfo>
)
