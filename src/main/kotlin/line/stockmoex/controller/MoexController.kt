package line.stockmoex.controller

import line.stockmoex.entity.StatisticRequest
import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.LastDayPriceResponse
import line.stockmoex.model.StatisticRequestDto
import line.stockmoex.model.TickerRequest
import line.stockmoex.service.StockService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
//ccc ffff
@RestController
@RequestMapping(path = ["\${stock.endpoint.adapterBase}"], produces = [APPLICATION_JSON_VALUE])
class MoexController(
    private var stockService: StockService
) {

    //метод получения цен в реальном времени (мониторинг)
    @PostMapping(path = ["\${stock.endpoint.getCurrentPrice}"])
    @ResponseBody
    fun getCurrentPrice(@RequestBody tickerRequest: TickerRequest): List<CurrentPriceResponse> {
        return stockService.getCurrentPrice(tickerRequest)
    }

    //метод получения закэшированной информации по акциям
    @PostMapping(path = ["\${stock.endpoint.getLastDayPrice}"])
    @ResponseBody
    fun getLastDayPrice(@RequestBody tickerRequest: TickerRequest): List<LastDayPriceResponse> {
        return stockService.getLastDatPrice(tickerRequest)
    }

    @PostMapping(path = ["\${stock.endpoint.saveStatistic}"])
    @ResponseBody
    fun saveStatistic(@RequestBody statisticRequestDto: StatisticRequestDto): StatisticRequest {
        return stockService.saveStatistic(statisticRequestDto)
    }
}