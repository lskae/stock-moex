package line.stockmoex.integration

import line.stockmoex.IntegrationBaseTest
import line.stockmoex.model.CurrentPriceResponse
import line.stockmoex.model.ErrorInfo
import line.stockmoex.model.TickerRequest
import line.stockmoex.model.moex.MoexMarketDataResponse
import org.apache.commons.io.FileUtils
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.http.RequestEntity
import org.springframework.util.ResourceUtils
import java.net.URI
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetCurrentPriceTest : IntegrationBaseTest() {


    fun sourceParams(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(
                ParametersData(
                    pathToTickerRequest = "classpath:controller/current/tickerRequest.json",
                    httpStatus = OK,
                    typeOfResponseClass = typeReference<List<CurrentPriceResponse>>(),
                    pathToResponse = "classpath:controller/current/currentPriceResponse.json"
                )
            ),
            Arguments.of(
                ParametersData(
                    pathToTickerRequest = "classpath:controller/current/failTickerRequest.json",
                    httpStatus = BAD_REQUEST,
                    typeOfResponseClass = typeReference<ErrorInfo>(),
                    pathToResponse = "classpath:controller/current/errorInfo.json"
                )
            )
        )
    }

    /**
     * Интеграционный тест эндпоинта getCurrentPrice
     */
    @ParameterizedTest
    @MethodSource("sourceParams")
    fun <T> getCurrentPriceSuccessTest(param: ParametersData<T>) {
        val getCurrentPriceUrl = "/v1/getCurrentPrice"
        val moexMarketDataResponse = getMoexMarketDataResponse()

        //мок для внешнего сервиса
        getWireMockStubGet(endpoints.moexCurrentPrice, moexMarketDataResponse, OK)

        val requestEntity = RequestEntity<Any>(
            getClassFromFile(param.pathToTickerRequest, TickerRequest::class.java),
            HttpMethod.POST,
            URI.create(baseUrl + getCurrentPriceUrl)
        )
        //запуск интеграционного теста
        val responseEntity = testRestTemplate.exchange(
            requestEntity,
            param.typeOfResponseClass
        )

        //тесты с полученными данными
        assertEquals(param.httpStatus, responseEntity.statusCode)
        val appResponse = responseEntity.body as Any
        assertNotNull(appResponse)

        val actual = JSONObject(objectMapper.writeValueAsString(appResponse))
        val expected = getExpectedResponse(param.pathToResponse)
        JSONAssert.assertEquals(expected, actual, true)

        //проверяем состояние и количество реквестов к мосбирже,
        assertWireMockRequestNoObject(endpoints.moexCurrentPrice)
    }

    private fun getExpectedResponse(pathToResponse: String): JSONObject {
        val file = ResourceUtils.getFile(pathToResponse)
        return JSONObject(FileUtils.readFileToString(file, "UTF-8"))
    }

    private fun getMoexMarketDataResponse() =
        getClassFromFile("classpath:controller/current/moexMarketDataResponse.json", MoexMarketDataResponse::class.java)

    private fun getTickerRequest() =
        getClassFromFile("classpath:controller/current/tickerRequest.json", TickerRequest::class.java)

    private fun getFailTickerRequest() =
        getClassFromFile("classpath:controller/current/failTickerRequest.json", TickerRequest::class.java)

    private fun <T> getClassFromFile(pathToFile: String, clas: Class<T>): T {
        val file = ResourceUtils.getFile(pathToFile)
        return objectMapper.readValue(file, clas)
    }

    data class ParametersData<T>(
        val pathToTickerRequest: String,
        val httpStatus: HttpStatus,
        val typeOfResponseClass: ParameterizedTypeReference<T>,
        val pathToResponse: String
    )
}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}

