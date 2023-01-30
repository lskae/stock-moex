package line.stockmoex.entity

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.CreationTimestamp
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "statistic_request")
@Entity
class StatisticRequest(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    val id: UUID,

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
//        //without = [ADJUST_DATES_TO_CONTEXT_TIME_ZONE]
//        )
//    @Schema(description = "Дата запроса", example = "2022-03-22T12:10:18.789+0000")
    @CreationTimestamp
    @Column(name = "date", nullable = false, updatable = false)
    val date: ZonedDateTime = ZonedDateTime.now(),

    @Column(name = "secid", nullable = false, updatable = false)
    val secid: String,

    @Column(name = "price", nullable = false, updatable = false)
    val price: String?,
)
