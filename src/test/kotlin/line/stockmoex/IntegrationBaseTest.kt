package line.stockmoex

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import line.stockmoex.integration.StatisticRequestRepositoryTest
import line.stockmoex.properties.EndpointProperties
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import javax.servlet.ServletContext

/**
 * Базовый класс для интеграционных тестов
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMetrics
class IntegrationBaseTest {

    /**
     * servlet: context-path
     */
    @Autowired
    lateinit var context: ServletContext

    /**
     * Кастомный бин wireMockServer
     */
    @Autowired
    lateinit var wireMockServer: WireMockServer

    //Порт для тестов
    @LocalServerPort
    private val localServerPort = 0

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    //создадим тестовый репозиторий, где будет свобода создания и изменения данных для тестов
    @Autowired
    lateinit var statisticRequestRepositoryTest: StatisticRequestRepositoryTest

    var baseUrl: String? = null

    @BeforeEach
    fun initAll() {
        baseUrl = "http://localhost:$localServerPort${context.contextPath}"
        wireMockServer.resetAll()
    }

    @Autowired
    lateinit var endpoints: EndpointProperties

    //мок для GET запросов
    fun getWireMockStubGet(
        testUrl: String,
        expectedResponse: Any,
        httpStatus: HttpStatus = OK,
        delayTime: Int = 0,
    ) {
        val mappingBuilder = WireMock.get(urlEqualTo(testUrl))
        return createWireMockStub(mappingBuilder, expectedResponse, httpStatus, delayTime)
    }

    //мок для POST запросов
    fun getWireMockStubPost(
        testUrl: String,
        expectedResponse: Any?,
        httpStatus: HttpStatus = OK,
        delayTime: Int = 0,
    ) {
        val mappingBuilder = WireMock.post(urlEqualTo(testUrl))
        return createWireMockStub(mappingBuilder, expectedResponse, httpStatus, delayTime)
    }

    // Проверка состояния и кол-ва запросов
    fun assertWireMockRequest(url: String, obj: Any) {
        wireMockServer.verify(
            1, RequestPatternBuilder.newRequestPattern()
                .withRequestBody(EqualToJsonPattern(objectMapper.valueToTree(obj), true, true))
                .withUrl(url)
        )
    }

    // Проверка состояния и кол-ва запросов без объекта на вход
    fun assertWireMockRequestNoObject(url: String) {
        wireMockServer.verify(1, getRequestedFor(urlEqualTo(url)))
    }

    //базовый конфиг мока
    private fun createWireMockStub(
        mappingBuilder: MappingBuilder,
        expectedResponse: Any?,
        httpStatus: HttpStatus,
        delayTime: Int,
    ) {
        val builder = WireMock.aResponse()
            .withFixedDelay(delayTime)
            .withStatus(httpStatus.value())
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        if (expectedResponse != null) {
            builder.withBody(objectMapper.writeValueAsString(expectedResponse))
        }
        wireMockServer.stubFor(mappingBuilder.willReturn(builder))
    }

    data class ParametersData(
        val pathToTickerRequest: String,
        val httpStatus: HttpStatus,
        val typeOfResponse: Class<*>,
        val pathToResponse: String
    )

}
