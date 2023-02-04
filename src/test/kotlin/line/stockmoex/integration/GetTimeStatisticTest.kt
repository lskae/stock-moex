package line.stockmoex.integration

import line.stockmoex.IntegrationBaseTest
import line.stockmoex.entity.StatisticRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.transaction.annotation.Transactional
import java.net.URI
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class GetTimeStatisticTest : IntegrationBaseTest() {

    @BeforeEach
    fun init() {
        statisticRequestRepositoryTest.deleteAll()
    }

    /**
     * Тест получения статистики за текущий торговый день
     */
    @Test
    @Transactional
    fun testGetStatistic() {

        val requestEntity = RequestEntity<Any>(
            HttpMethod.GET,
            URI.create(baseUrl + getUrlEndpoint())
        )
        val response = testRestTemplate.exchange(requestEntity,
            object : ParameterizedTypeReference<List<StatisticRequest>>() {})
        assertEquals(HttpStatus.OK, response.statusCode)
        statisticRequestRepositoryTest.saveAll(generateEntities())
        println("ddd" + statisticRequestRepositoryTest.findAll().get(0).date)
        statisticRequestRepositoryTest.updateDate(changeDate())
        assertEquals(2, statisticRequestRepositoryTest.getStatistic().size)
    }

    private fun changeDate(): ZonedDateTime {
        return ZonedDateTime.parse(
            "2019-01-01T13:12:34.231+0300",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
    }

    private fun generateEntities(): List<StatisticRequest> {
        return listOf(
            StatisticRequest(
                id = UUID.randomUUID(),
                date = ZonedDateTime.now(),
                secid = "SBER",
                price = "100"
            ),
            StatisticRequest(
                id = UUID.randomUUID(),
                secid = "GAZP",
                price = "130"
            ),
            StatisticRequest(
                id = UUID.randomUUID(),
                secid = "SVR",
                price = "150"
            ),
        )
    }

    fun getUrlEndpoint() = ("/v1/getTimeStatistic")
}
