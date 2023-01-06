package line.stockmoex.integration

import com.fasterxml.jackson.core.type.TypeReference
import line.stockmoex.IntegrationBaseTest
import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.model.moex.MoexMarketDataResponse
import org.apache.commons.io.FileUtils
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.HttpStatus.OK
import org.springframework.util.ResourceUtils

class GetCurrentPriceTest : IntegrationBaseTest() {

    @Test
    fun getCurrentPriceTest() {
        val getCurrentPriceUrl = "/v1/getCurrentPrice"
        val tickerRequest = getTickerRequest()
        val moexMarketDataResponse = getMoexMarketDataResponse()

        //мок для внешнего сервиса
        getWireMockStubGet(endpoints.moexCurrentPrice, moexMarketDataResponse, OK)

        //запуск интеграционного теста
        val responseEntity = testRestTemplate.postForEntity(
            baseUrl + getCurrentPriceUrl,
            tickerRequest,
            CurrentPriceResponse::class.java)

        //тесты с полученными данными
        assertEquals(OK, responseEntity.statusCode)
        val actualCurrentPriceResponse = responseEntity.body
        assertNotNull(actualCurrentPriceResponse)

        val actual = JSONObject(objectMapper.writeValueAsString(actualCurrentPriceResponse))
        val expected = getExpectedResponse()
        JSONAssert.assertEquals(expected, actual, true)

        //проверяем состояние реквеста
        //assertWireMockRequest(endpoints.adapterBase + endpoints.getCurrentPrice, tickerRequest)
    }

    private fun getExpectedResponse(): JSONObject {
        val file = ResourceUtils.getFile("classpath:controller/current/currentPriceResponse.json")
        return JSONObject(FileUtils.readFileToString(file, "UTF-8"))
    }

    private fun getCurrentPriceResponse(): CurrentPriceResponse {
        val file = ResourceUtils.getFile("classpath:controller/current/currentPriceResponse.json")
        return objectMapper.readValue(file, CurrentPriceResponse::class.java)
    }

    private fun getMoexMarketDataResponse(): MoexMarketDataResponse {
        val file = ResourceUtils.getFile("classpath:controller/current/moexMarketDataResponse.json")
        return objectMapper.readValue(file, MoexMarketDataResponse::class.java)
    }

    private fun getTickerRequest(): TickerRequest {
        val file = ResourceUtils.getFile("classpath:controller/current/tickerRequest.json")
        return objectMapper.readValue(file, TickerRequest::class.java)
    }

    private fun getClassFromFile(pathToFile: String): Any {
        val file = ResourceUtils.getFile(pathToFile)
        return objectMapper.readValue(file, Any::class.java)
    }


}