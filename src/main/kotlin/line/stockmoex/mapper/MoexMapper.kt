package line.stockmoex.mapper

import line.stockmoex.exception.NotValidTickerRequestException
import line.stockmoex.model.PreviousDayPriceInfo
import line.stockmoex.model.PreviousDayPriceInfoResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.model.current.CurrentPriceInfo
import line.stockmoex.model.current.CurrentPriceInfoResponse
import line.stockmoex.model.moex.MoexMarketDataResponse
import line.stockmoex.model.moex.MoexSecuritiesResponse
import org.springframework.stereotype.Component

@Component
class MoexMapper {

    /**
     * Маппер для эндпоинта получения информации по котировкам в режиме реального времени
     */
    fun getMoexCurrentPrice(
        moexMarketDataResponse: MoexMarketDataResponse,
        tickerRequest: TickerRequest,
    ): CurrentPriceInfoResponse {
        val columns = moexMarketDataResponse.marketData.columns
        val listData = moexMarketDataResponse.marketData.data
            .filter { a -> a.any { it in tickerRequest.tickerList } }
        isRequestValid(listData)
        return CurrentPriceInfoResponse(
            listCurrentPriceInfo = listData
                .map { v ->
                    CurrentPriceInfo(
                        secid = v[columns.indexOf("SECID")].toString(),
                        low = v[columns.indexOf("LOW")].toString(),
                        last = v[columns.indexOf("LAST")].toString(),
                        high = v[columns.indexOf("HIGH")].toString()
                    )
                })
    }

    /**
     * Маппер для эндпоинта получения кэшированной информации по котировкам акций за предыдущий день
     */
    fun getPreviousDayPrice(
        moexSecuritiesResponse: MoexSecuritiesResponse,
        tickerRequest: TickerRequest,
    ): PreviousDayPriceInfoResponse {
        val columns = moexSecuritiesResponse.securities.columns
        val listData = moexSecuritiesResponse.securities.data
            .filter { a -> a.any { it in tickerRequest.tickerList } }
        isRequestValid(listData)
        return PreviousDayPriceInfoResponse(
            previousDayPriceInfoList = listData
                .map { v ->
                    PreviousDayPriceInfo(
                        secid = v[columns.indexOf("SECID")].toString(),
                        shortName = v[columns.indexOf("SHORTNAME")].toString(),
                        prevLegalClosePrice = v[columns.indexOf("PREVLEGALCLOSEPRICE")].toString()
                    )
                })
    }

    private fun isRequestValid(listData: List<List<Any>>) {
        if (listData.isEmpty()) {
            throw NotValidTickerRequestException("Запрос не содержит тикеров, которые торгуются на московской бирже")
        }
    }
}
