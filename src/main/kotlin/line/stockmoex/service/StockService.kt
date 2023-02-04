package line.stockmoex.service

import io.micrometer.core.instrument.MeterRegistry
import line.stockmoex.entity.StatisticRequest
import line.stockmoex.mapper.MoexMapper
import line.stockmoex.model.LastDayPriceInfoResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.model.current.CurrentPriceInfo
import line.stockmoex.model.current.CurrentPriceInfoResponse
import line.stockmoex.moex.MoexClient
import line.stockmoex.repository.StatisticRequestRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
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

    fun getCurrentPrice(tickerRequest: TickerRequest): CurrentPriceInfoResponse {
        val moexResponse = moexClient.getCurrentPrice()
        val currentInfoResponse = moexMapper.getMoexCurrentPrice(moexResponse, tickerRequest)

        /**
         * Подсчет значения для кастомной метрики для максимальной цены по определенному тикеру
         */
        getCustomMetricForMaxValue(currentInfoResponse.currentPriceInfoResponse)

        // Сохранение статистики для прометеуса
        val listStatisticRequest = currentInfoResponse.currentPriceInfoResponse
            .map { a -> getStatisticRequest(a) }
        statisticRequestRepository.saveAll(listStatisticRequest)

        return currentInfoResponse
    }

    /**
     * Использование кэша
     */
    @Cacheable(value = ["stable"])
    fun getLastDatPrice(tickerRequest: TickerRequest): LastDayPriceInfoResponse {
        val moexResponse = moexClient.getLastDayPrice()
        return moexMapper.getLastDatPrice(moexResponse, tickerRequest)
    }

    fun getStatisticRequest(currentPriceInfo: CurrentPriceInfo): StatisticRequest {
        return StatisticRequest(
            id = UUID.randomUUID(),
            secid = currentPriceInfo.secid,
            price = currentPriceInfo.last
        )
    }

    //кастомная метрика позволяющая отслеживать число запросов по конкретной бумаге (например SBER)
    private fun getCustomMetricForMaxValue(listCurrentPriceInfo: List<CurrentPriceInfo>) {
        val maxPrice = listCurrentPriceInfo.count { a -> a.equals("SBER") }
        value.set(maxPrice)
    }
}
