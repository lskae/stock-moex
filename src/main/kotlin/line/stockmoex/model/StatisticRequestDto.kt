package line.stockmoex.model

import java.io.Serializable
import java.math.BigDecimal
import java.time.ZonedDateTime

data class StatisticRequestDto(
    val id: String,
    val date: ZonedDateTime,
    val secid: String,
    val price: BigDecimal,
) : Serializable
