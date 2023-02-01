package line.stockmoex.mapper

import line.stockmoex.exception.NotValidTickerRequestException
import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.LastDayPriceResponse
import line.stockmoex.model.TickerRequest
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
    ): List<CurrentPriceResponse> {
        val columns = moexMarketDataResponse.marketData.columns
        val listData = moexMarketDataResponse.marketData.data
            .filter { a -> a.any { it in tickerRequest.tickerList } }
        if (listData.isEmpty()) {
            throw NotValidTickerRequestException("Запрос не содержит тикеров, которые торгуются на московской бирже")
        }
        return listData
            .map { v ->
                CurrentPriceResponse(
                    secid = v[columns.indexOf("SECID")].toString(),
                    low = v[columns.indexOf("LOW")].toString(),
                    last = v[columns.indexOf("LAST")].toString(),
                    high = v[columns.indexOf("HIGH")].toString()
                )
            }
    }

    /**
     * Маппер для эндпоинта получения кэшированной информации
     */
    fun getLastDatPrice(
        moexSecuritiesResponse: MoexSecuritiesResponse,
        tickerRequest: TickerRequest,
    ): List<LastDayPriceResponse> {
        val columns = moexSecuritiesResponse.securities.columns
        return moexSecuritiesResponse.securities.data
            .filter { a -> a.any { it in tickerRequest.tickerList } }
            .map { v ->
                LastDayPriceResponse(
                    secid = v[columns.indexOf("SECID")].toString(),
                    shortName = v[columns.indexOf("SHORTNAME")].toString(),
                    prevAdmitTedQuote = v[columns.indexOf("PREVADMITTEDQUOTE")].toString()
                )
            }
    }
}
