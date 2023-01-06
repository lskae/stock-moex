package line.stockmoex.service

import com.fasterxml.jackson.databind.ObjectMapper
import line.stockmoex.mapper.MoexMapper
import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.LastDayPriceResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.moex.MoexClient
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class StockService(
    private var moexClient: MoexClient,
    private var moexMapper: MoexMapper
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getCurrentPrice(tickerRequest: TickerRequest): CurrentPriceResponse {
        val moexResponse = moexClient.getCurrentPrice()
        return moexMapper.getMoexCurrentPrice(moexResponse, tickerRequest)
    }

    @Cacheable(value = ["stable"])
    fun getLastDatPrice(tickerRequest: TickerRequest): List<LastDayPriceResponse> {
        val moexResponse = moexClient.getLastDayPrice()
        return moexMapper.getLastDatPrice(moexResponse, tickerRequest)
    }
}