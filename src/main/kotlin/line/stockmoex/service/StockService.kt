package line.stockmoex.service

import io.micrometer.core.instrument.MeterRegistry
import line.stockmoex.entity.StatisticRequest
import line.stockmoex.mapper.MoexMapper
import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.LastDayPriceResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.moex.MoexClient
import line.stockmoex.repository.StatisticRequestRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@Service
class StockService(
    private val moexClient: MoexClient,
    private val moexMapper: MoexMapper,
    private val statisticRequestRepository: StatisticRequestRepository,
    meterRegistry: MeterRegistry,
) {
    private var value: AtomicInteger = AtomicInteger()

    /**
     * Инициализация кастомная метрика для максимальной цены
     */
    init {
        meterRegistry.gauge("metricCustomMaxPrice", value)
    }

    fun getCurrentPrice(tickerRequest: TickerRequest): List<CurrentPriceResponse> {
        val moexResponse = moexClient.getCurrentPrice()
        val listCurrentPrice = moexMapper.getMoexCurrentPrice(moexResponse, tickerRequest)

        /**
         * Подсчет значения для кастомной метрики для максимальной цены по определенному тикеру
         */
        getCustomMetricForMaxValue(listCurrentPrice)

        /**
         * Cохранение статистики для прометеуса
         */
        val listStatisticRequest = listCurrentPrice.map { a -> getStatisticRequest(a) }
        statisticRequestRepository.saveAll(listStatisticRequest)

        return listCurrentPrice
    }

    /**
     * Использование кэша
     */
    @Cacheable(value = ["stable"])
    fun getLastDatPrice(tickerRequest: TickerRequest): List<LastDayPriceResponse> {
        val moexResponse = moexClient.getLastDayPrice()
        return moexMapper.getLastDatPrice(moexResponse, tickerRequest)
    }

    fun getStatisticRequest(currentPriceResponse: CurrentPriceResponse): StatisticRequest {
        return StatisticRequest(
            id = UUID.randomUUID(),
            secid = currentPriceResponse.secid,
            price = currentPriceResponse.last,
            date = LocalDateTime.now().atZone(ZoneId.of("Europe/Moscow"))
        )
    }

    //кастомная метрика позволяющая отслеживать число запросов по конкретной бумаге (например SBER)
    private fun getCustomMetricForMaxValue(listCurrentPrice: List<CurrentPriceResponse>) {
        val maxPrice = listCurrentPrice.count { a -> a.equals("SBER") }
        value.set(maxPrice)
    }
}
