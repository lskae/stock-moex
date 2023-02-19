package line.stockmoex.model.current

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Контейнер для листа объектов CurrentPriceInfo")
data class CurrentPriceInfoResponse(

    @field:Schema(description = "Лист объектов CurrentPriceInfo")
    @JsonProperty("listCurrentPriceInfo")
    val listCurrentPriceInfo: List<CurrentPriceInfo>
)
