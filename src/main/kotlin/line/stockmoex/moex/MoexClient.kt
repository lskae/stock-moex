package line.stockmoex.moex

import io.micrometer.core.annotation.Timed
import line.stockmoex.model.moex.MoexMarketDataResponse
import line.stockmoex.model.moex.MoexSecuritiesResponse
import line.stockmoex.moex.config.MoexClientConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@FeignClient(name = "moex-client", url = "\${stock.endpoint.moexBase}", configuration = [MoexClientConfig::class])
interface MoexClient {

    /**
     * Запрос к Московской бирже для получения информации по котировкам акций в режиме реального времени
     */
    @GetMapping(path = ["\${stock.endpoint.moexCurrentPrice}"])
    @ResponseBody
    /*
     время выполнения запроса к апи московской биржи
     */
    @Timed(value = "metric1RequestToMoex") // время выполнения запроса к апи московской биржи
    fun getCurrentPrice(): MoexMarketDataResponse


    /**
     * Запрос к Московской бирже для получения информации по котировкам акций за предыдущий торговой день
     */
    @GetMapping(path = ["\${stock.endpoint.moexPreviousDayPrice}"])
    @ResponseBody
    fun getPreviousDayPrice(): MoexSecuritiesResponse
}
