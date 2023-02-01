package line.stockmoex.model.moex

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ Московской Биржи (параметр securities)")
data class MoexSecuritiesResponse(

    @field:Schema(description = "Объект ответа Московской Биржи (параметр securities)")
    @JsonProperty("securities")
    val securities: Securities,
)
