package line.stockmoex.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ на запрос о получении кэшированной информации по котировкам акций за предыдущий день")
data class LastDayPriceResponse(

    @field:Schema(description = "SECID", example = "SBER")
    @JsonProperty("SECID")
    val secid: String,

    @field:Schema(description = "Обозначение тикера", example = "Сбербанк")
    @JsonProperty("SHORTNAME")
    val shortName: String?,

    @field:Schema(description = "Цена предыдущего дня", example = "156.4")
    @JsonProperty("PREVADMITTEDQUOTE")
    val prevAdmitTedQuote: String?,
)
