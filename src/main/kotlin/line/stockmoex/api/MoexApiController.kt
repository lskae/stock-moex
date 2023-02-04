package line.stockmoex.api

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.tags.Tag
import line.stockmoex.entity.StatisticRequest
import line.stockmoex.model.LastDayPriceInfoResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.model.current.CurrentPriceInfoResponse
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import io.swagger.v3.oas.annotations.parameters.RequestBody as RequestBodySwagger

/**
 * Api сервиса.
 */
@RequestMapping(path = ["\${stock.endpoint.adapterBase}"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Validated
@Tag(name = "stock-moex-v1", description = "Адаптер для работы с API Московской биржи")
@OpenAPIDefinition(
    info = Info(
        title = "stock-moex",
        description = "Адаптер для работы с API Московской биржи»",
        version = "1.0.0"
    )
)
interface MoexApiController {

    @PostMapping(path = ["\${stock.endpoint.getCurrentPrice}"])
    @ResponseBody
    @Operation(
        summary = "Получение информации по котировкам акций в режиме реального времени",
        description = "Получение информации по котировкам акций в режиме реального времени (параметр MARKETDATA)",
        requestBody = RequestBodySwagger(description = "Лист тикеров акций по которым необходимо получить информацию")
    )
    @ResponsesAndHeader
    fun getCurrentPrice(@RequestBody tickerRequest: TickerRequest): CurrentPriceInfoResponse

    @PostMapping(path = ["\${stock.endpoint.getLastDayPrice}"])
    @ResponseBody
    @Operation(
        summary = "Получение кэшированной информации по котировкам акций за предыдущий день",
        description = "Получение кэшированной информации по котировкам акций за предыдущий день, параметр SECURITIES",
        requestBody = RequestBodySwagger(description = "Лист тикеров акций по которым необходимо получить информацию")
    )
    @ResponsesAndHeader
    fun getLastDayPrice(@RequestBody tickerRequest: TickerRequest): LastDayPriceInfoResponse

    @GetMapping(path = ["\${stock.endpoint.getTimeStatistic}"])
    @ResponseBody
    @Operation(
        summary = "Получение статистики запросов за последний торговый день",
        description = "Получение статистики запросов за последний торговый день, для учета метрик мониторинга"
    )
    @ResponsesAndHeader
    fun getTimeStatistic(): List<StatisticRequest>
}
