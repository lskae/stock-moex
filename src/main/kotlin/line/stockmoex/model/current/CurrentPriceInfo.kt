package line.stockmoex.model.current

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Информация по котировкам акций в режиме реального времени")
data class CurrentPriceInfo(

    @field:Schema(description = "SECID", example = "SBER")
    @JsonProperty("SECID")
    val secid: String,

    @field:Schema(description = "Текущая цена на торгах", example = "200.2")
    @JsonProperty("LAST")
    val last: String?,

    @field:Schema(description = "Наименьшая цена за текущий торговый день", example = "199.2")
    @JsonProperty("LOW")
    val low: String?,

    @field:Schema(description = "Наибольшая цена за текущий торговый день", example = "202.2")
    @JsonProperty("HIGH")
    val high: String?,
)
