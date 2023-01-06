package line.stockmoex.mapper

import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.LastDayPriceResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.model.moex.MoexMarketDataResponse
import line.stockmoex.model.moex.MoexSecuritiesResponse
import org.springframework.stereotype.Component

@Component
class MoexMapper {
    fun getMoexCurrentPrice(
        moexMarketDataResponse: MoexMarketDataResponse,
        tickerRequest: TickerRequest,
    ): List<CurrentPriceResponse> {
        val columns = moexMarketDataResponse.marketData.columns
        return moexMarketDataResponse.marketData.data
            .filter { a -> a.any { it in tickerRequest.tickerList } }
            .map { v ->
                CurrentPriceResponse(
                    secid = v[columns.indexOf("SECID")] as String,
                    low = v[columns.indexOf("LOW")] as Number,
                    last = v[columns.indexOf("LAST")] as Number,
                    high = v[columns.indexOf("HIGH")] as Number)
            }
    }

    fun getLastDatPrice(
        moexSecuritiesResponse: MoexSecuritiesResponse,
        tickerRequest: TickerRequest,
    ): List<LastDayPriceResponse> {
        val columns = moexSecuritiesResponse.securities.columns
        return moexSecuritiesResponse.securities.data
            .filter { a -> a.any { it in tickerRequest.tickerList } }
            .map { v ->
                LastDayPriceResponse(
                    secid = v[columns.indexOf("SECID")] as String,
                    shortName = v[columns.indexOf("SHORTNAME")] as String,
                    prevAdmitTedQuote = v[columns.indexOf("PREVADMITTEDQUOTE")] as Number)
            }
    }
}