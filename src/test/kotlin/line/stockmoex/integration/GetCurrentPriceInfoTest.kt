package line.stockmoex.integration

import line.stockmoex.IntegrationBaseTest
import line.stockmoex.model.current.CurrentPriceInfoResponse
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
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.util.ResourceUtils
import java.util.stream.Stream

/**
 * Интеграционный тест эндпоинта getCurrentPrice
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetCurrentPriceInfoTest : IntegrationBaseTest() {

    fun sourceParams(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(
                ParametersData(
                    pathToTickerRequest = "classpath:controller/tickerRequest.json",
                    httpStatus = OK,
                    typeOfResponse = CurrentPriceInfoResponse::class.java,
                    pathToResponse = "classpath:controller/current/currentPriceInfoResponse.json"
                )
            ),
            Arguments.of(
                ParametersData(
                    pathToTickerRequest = "classpath:controller/failTickerRequest.json",
                    httpStatus = BAD_REQUEST,
                    typeOfResponse = ErrorInfo::class.java,
                    pathToResponse = "classpath:controller/errorInfo.json"
                )
            )
        )
    }

    /**
     * Интеграционный тест эндпоинта getCurrentPrice.
     * Два сценария:
     * 1. Успешный ответ от московской биржи
     * 2. В процессе работы приложения возникла ошибка
     * Параметры для тестов берутся из аргументов параметров sourceParams
     */
    @ParameterizedTest
    @MethodSource("sourceParams")
    fun getCurrentPriceParamsTest(param: ParametersData) {

        //ответ внешнего сервиса
        val moexMarketDataResponse = getMoexMarketDataResponse()
        //мок для внешнего сервиса
        getWireMockStubGet(endpoints.moexCurrentPrice, moexMarketDataResponse, OK)

        //запуск интеграционного теста
        val responseEntity = testRestTemplate.postForEntity(
            (baseUrl + getUrlEndpoint()),
            getClassFromFile(param.pathToTickerRequest, TickerRequest::class.java),
            param.typeOfResponse
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

    private fun <T> getClassFromFile(pathToFile: String, clas: Class<T>): T {
        val file = ResourceUtils.getFile(pathToFile)
        return objectMapper.readValue(file, clas)
    }

    fun getUrlEndpoint() = ("/v1/getCurrentPrice")
}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}

