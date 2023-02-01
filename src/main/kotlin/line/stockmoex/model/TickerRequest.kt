package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Запрос со списком тикеров")
data class TickerRequest(

    @field:Schema(description = "Список тикеров", example = "SBER, GAZP")
    @JsonProperty("tickerList")
    val tickerList: List<String>,
)
