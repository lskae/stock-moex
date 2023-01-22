package line.stockmoex.entity

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema

import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "statistic_request")
@Entity
class StatisticRequest(
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    val id: String,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Schema(description = "Дата запроса", example = "2022-03-22T12:10:18.789+0000")
    @Column(name = "date", nullable = false, updatable = false)
    val date: ZonedDateTime,

    @Column(name = "secid", nullable = false, updatable = false)
    val secid: String,

    @Column(name = "price", nullable = false, updatable = false)
    val price: BigDecimal,
)