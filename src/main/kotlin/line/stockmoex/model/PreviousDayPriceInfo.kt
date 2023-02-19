package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Информация по котировкам акций за предыдущий день")
data class PreviousDayPriceInfo(

    @field:Schema(description = "SECID", example = "SBER")
    @JsonProperty("SECID")
    val secid: String,

    @field:Schema(description = "Обозначение тикера", example = "Сбербанк")
    @JsonProperty("SHORTNAME")
    val shortName: String?,

    @field:Schema(description = "Цена закрытия предыдущего торгового дня", example = "156.4")
    @JsonProperty("PREVLEGALCLOSEPRICE")
    val prevLegalClosePrice: String?,
)
