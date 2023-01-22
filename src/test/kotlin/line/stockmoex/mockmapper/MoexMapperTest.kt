package line.stockmoex.mockmapper

import com.fasterxml.jackson.databind.ObjectMapper
import line.stockmoex.mapper.MoexMapper
import line.stockmoex.model.TickerRequest
import line.stockmoex.model.moex.MoexMarketDataResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.util.ResourceUtils

private const val LAST = 161.23

@ExtendWith(MockitoExtension::class)
class GetCurrentPriceMapperTest {
    lateinit var moexMapper: MoexMapper

    @BeforeEach
    fun init() {
        moexMapper = MoexMapper()
    }

    @Test
    fun getCurrentPriceMapper() {
        val list = moexMapper.getMoexCurrentPrice(getMoexMarketDataResponse(), getTickerRequest())
        assertEquals(LAST, list.first { a -> a.secid == "GAZP" }.last)
    }

    private fun getTickerRequest(): TickerRequest {
        return TickerRequest(tickerList = listOf("GAZP", "SBER"))
    }

    //В тестах на моках вроде как принято создавать объект "вручную", как TickerRequest выше на 31 строке,
    //но MoexMarketDataResponse большой, поэтому мне стало лень
    private fun getMoexMarketDataResponse(): MoexMarketDataResponse {
        val objectMapper = ObjectMapper()
        val file = ResourceUtils.getFile("classpath:controller/current/moexMarketDataResponse.json")
        return objectMapper.readValue(file, MoexMarketDataResponse::class.java)
    }
}