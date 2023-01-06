package line.stockmoex.model.moex

import com.fasterxml.jackson.annotation.JsonProperty

data class Securities(
    @JsonProperty("columns")
    val columns: List<String>,
    @JsonProperty("data")
    val data: List<List<Any>>,
)