package line.stockmoex.controller

import io.micrometer.core.annotation.Timed
import line.stockmoex.api.MoexApiController
import line.stockmoex.entity.StatisticRequest
import line.stockmoex.model.PreviousDayPriceInfoResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.model.current.CurrentPriceInfoResponse
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
    override fun getCurrentPrice(@RequestBody tickerRequest: TickerRequest): CurrentPriceInfoResponse {
        return stockService.getCurrentPrice(tickerRequest)
    }

    override fun getPreviousDayPrice(@RequestBody tickerRequest: TickerRequest): PreviousDayPriceInfoResponse {
        return stockService.getPreviousDayPrice(tickerRequest)
    }

    override fun getTimeStatistic(): List<StatisticRequest> {
        return statisticRequestRepository.getStatistic()
    }
}
