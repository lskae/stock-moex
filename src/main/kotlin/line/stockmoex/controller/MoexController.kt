package line.stockmoex.controller

import io.micrometer.core.annotation.Timed
import line.stockmoex.api.MoexApiController
import line.stockmoex.entity.StatisticRequest
import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.LastDayPriceResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.repository.StatisticRequestRepository
import line.stockmoex.service.StockService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MoexController(
    private val stockService: StockService,
    private val statisticRequestRepository: StatisticRequestRepository,
) : MoexApiController {

    @Timed(value = "metric2CurrentPrice")
    override fun getCurrentPrice(@RequestBody tickerRequest: TickerRequest): List<CurrentPriceResponse> {
        return stockService.getCurrentPrice(tickerRequest)
    }

    override fun getLastDayPrice(@RequestBody tickerRequest: TickerRequest): List<LastDayPriceResponse> {
        return stockService.getLastDatPrice(tickerRequest)
    }

    override fun getTimeStatistic(): List<StatisticRequest> {
        return statisticRequestRepository.getStatistic()
    }
}
