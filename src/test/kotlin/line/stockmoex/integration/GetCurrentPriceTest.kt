package line.stockmoex.integration

import line.stockmoex.IntegrationBaseTest
import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.TickerRequest
import line.stockmoex.model.moex.MoexMarketDataResponse
import org.apache.commons.io.FileUtils
import org.json.JSONArray
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus.OK
import org.springframework.http.RequestEntity
import org.springframework.util.ResourceUtils
import java.net.URI

class GetCurrentPriceTest : IntegrationBaseTest() {

    @Test
    fun getCurrentPriceTest() {
        val getCurrentPriceUrl = "/v1/getCurrentPrice"
        val tickerRequest = getTickerRequest()
        val moexMarketDataResponse = getMoexMarketDataResponse()

        //мок для внешнего сервиса
        getWireMockStubGet(endpoints.moexCurrentPrice, moexMarketDataResponse, OK)

        val requestEntity = RequestEntity<Any>(tickerRequest, HttpMethod.POST, URI.create(baseUrl + getCurrentPriceUrl))

        //запуск интеграционного теста
        val responseEntity = testRestTemplate.exchange(
            requestEntity,
            object : ParameterizedTypeReference<List<CurrentPriceResponse>>() {})

        //тесты с полученными данными
        assertEquals(OK, responseEntity.statusCode)
        val actualCurrentPriceResponse = responseEntity.body
        assertNotNull(actualCurrentPriceResponse)

        val actual = JSONArray(objectMapper.writeValueAsString(actualCurrentPriceResponse))
        val expected = getExpectedResponse()
        JSONAssert.assertEquals(expected, actual, true)

        //проверяем состояние и количество реквестов к мосбирже,
        assertWireMockRequestNoObject(endpoints.moexCurrentPrice)
    }

    private fun getExpectedResponse(): JSONArray {
        val file = ResourceUtils.getFile("classpath:controller/current/currentPriceResponse.json")
        return JSONArray(FileUtils.readFileToString(file, "UTF-8"))
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