package line.stockmoex.repository

import io.micrometer.core.annotation.Timed
import line.stockmoex.entity.StatisticRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StatisticRequestRepository : JpaRepository<StatisticRequest, String> {

    /*
    Запрос выводит записи за текущий торговый день.
    Выглядит немного неуклюжим, но это для того, чтобы как-то задействовать nativeQuery)
     */
    @Timed(value = "metric3RecordToRepositoryStatistic")
    @Query(value = "SELECT * FROM statistic_request where date > current_date", nativeQuery = true)
    fun getStatistic(): List<StatisticRequest>

    fun findBySecid(secid: String):List<StatisticRequest>
}
