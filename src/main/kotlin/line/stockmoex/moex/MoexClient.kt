package line.stockmoex.moex

import io.micrometer.core.annotation.Timed
import line.stockmoex.model.moex.MoexMarketDataResponse
import line.stockmoex.model.moex.MoexSecuritiesResponse
import line.stockmoex.moex.config.MoexClientConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

//TODO добавить декодер
@FeignClient(name = "moex-client", url = "\${stock.endpoint.moexBase}", configuration = [MoexClientConfig::class])
interface MoexClient {

    @GetMapping(path = ["\${stock.endpoint.moexCurrentPrice}"])
    @ResponseBody
    @Timed(value = "metric1RequestToMoex") // время выполнения запроса к апи московской биржи
    fun getCurrentPrice(): MoexMarketDataResponse

    @GetMapping(path = ["\${stock.endpoint.moexLastDayPrice}"])
    @ResponseBody
    fun getLastDayPrice(): MoexSecuritiesResponse
}
