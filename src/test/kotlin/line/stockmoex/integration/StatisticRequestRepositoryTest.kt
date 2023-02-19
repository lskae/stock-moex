package line.stockmoex.integration

import io.micrometer.core.annotation.Timed
import line.stockmoex.entity.StatisticRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

/**
 *  Репозиторий, созданный для тестов.
 */
@Repository
interface StatisticRequestRepositoryTest : JpaRepository<StatisticRequest, String> {

    /*
    Создадим метод для изменения данных в репозитории для тестов
     */
    @Modifying
    @Timed(value = "metric3RecordToRepositoryStatistic")
    @Query(
        value = "UPDATE statistic_request SET date = :changeDate where secid = 'SVR'",
        nativeQuery = true
    )
    fun updateDate(changeDate: ZonedDateTime)

    @Query(value = "SELECT * FROM statistic_request where date > current_date", nativeQuery = true)
    fun getStatistic(): List<StatisticRequest>
}
