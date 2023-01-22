package line.stockmoex.service

import line.stockmoex.entity.StatisticRequest
import line.stockmoex.mapper.MoexMapper
import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.LastDayPriceResponse
import line.stockmoex.model.StatisticRequestDto
import line.stockmoex.model.TickerRequest
import line.stockmoex.moex.MoexClient
import line.stockmoex.repository.StatisticRequestRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class StockService(
    private var moexClient: MoexClient,
    private var moexMapper: MoexMapper,
    private var statisticRequestRepository: StatisticRequestRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getCurrentPrice(tickerRequest: TickerRequest): List<CurrentPriceResponse> {
        val moexResponse = moexClient.getCurrentPrice()
        return moexMapper.getMoexCurrentPrice(moexResponse, tickerRequest)
    }

    @Cacheable(value = ["stable"])
    fun getLastDatPrice(tickerRequest: TickerRequest): List<LastDayPriceResponse> {
        val moexResponse = moexClient.getLastDayPrice()
        return moexMapper.getLastDatPrice(moexResponse, tickerRequest)
    }

    fun saveStatistic(statisticRequestDto: StatisticRequestDto): StatisticRequest {
        val statisticRequest = StatisticRequest(
            id = statisticRequestDto.id,
            secid = statisticRequestDto.secid,
            price = statisticRequestDto.price,
            date = ZonedDateTime.now()
        )
        return statisticRequestRepository.save(statisticRequest)
    }
}