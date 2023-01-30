package line.stockmoex.controller

import io.micrometer.core.annotation.Timed
import line.stockmoex.entity.StatisticRequest
import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.LastDayPriceResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.repository.StatisticRequestRepository
import line.stockmoex.service.StockService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.GetMapping

@RestController
@RequestMapping(path = ["\${stock.endpoint.adapterBase}"], produces = [APPLICATION_JSON_VALUE])
class MoexController(
    private val stockService: StockService,
    private val statisticRequestRepository: StatisticRequestRepository,
) {

    //метод получения цен в реальном времени (мониторинг)
    @PostMapping(path = ["\${stock.endpoint.getCurrentPrice}"])
    @ResponseBody
    @Timed(value = "metric2CurrentPrice")
    fun getCurrentPrice(@RequestBody tickerRequest: TickerRequest): List<CurrentPriceResponse> {
        return stockService.getCurrentPrice(tickerRequest)
    }

    //метод получения закэшированной информации по акциям
    @PostMapping(path = ["\${stock.endpoint.getLastDayPrice}"])
    @ResponseBody
    fun getLastDayPrice(@RequestBody tickerRequest: TickerRequest): List<LastDayPriceResponse> {
        return stockService.getLastDatPrice(tickerRequest)
    }

    //Получить метрики отражающие время выполнения запроса в БД за текущий день торгов
    @GetMapping(path = ["\${stock.endpoint.getTimeStatistic}"])
    @ResponseBody
    fun getTimeStatistic(): List<StatisticRequest> {
        return statisticRequestRepository.getStatistic()
    }
}
