package line.stockmoex.model.moex

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Объект ответа Московской Биржи (параметр marketdata)")
data class MarketData(

    @field:Schema(description = "Параметры объекта ответа Московской Биржи (параметр marketdata)")
    @JsonProperty("columns")
    val columns: List<String>,

    @field:Schema(description = "Значения объекта ответа Московской Биржи (параметр marketdata)")
    @JsonProperty("data")
    val data: List<List<Any>>,
)
