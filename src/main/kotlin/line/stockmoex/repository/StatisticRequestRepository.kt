package line.stockmoex.repository

import line.stockmoex.entity.StatisticRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StatisticRequestRepository : JpaRepository<StatisticRequest, String> {
}