package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Объект ответа на запрос о получении информации по котировкам акций в режиме реального времени")
data class CurrentPriceResponse(

    @field:Schema(description = "SECID", example = "SBER")
    @JsonProperty("SECID")
    val secid: String,

    @field:Schema(description = "Текущая цена на торгах", example = "200.2")
    @JsonProperty("LAST")
    val last: String?,

    @field:Schema(description = "Низшая цена за текущий торговый день", example = "199.2")
    @JsonProperty("LOW")
    val low: String?,

    @field:Schema(description = "Высокая цена за текущий торговый день", example = "202.2")
    @JsonProperty("HIGH")
    val high: String?,
)
