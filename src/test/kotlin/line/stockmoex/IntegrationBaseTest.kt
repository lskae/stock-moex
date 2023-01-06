package line.stockmoex

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import line.stockmoex.properties.EndpointProperties
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import javax.servlet.ServletContext

//Базовый класс для интеграционных тестов
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMetrics
class IntegrationBaseTest {

    //servlet: context-path
    @Autowired
    lateinit var context: ServletContext

    //кастомный бин wireMockServer
    @Autowired
    lateinit var wireMockServer: WireMockServer

    //Порт для тестов
    @LocalServerPort
    private val localServerPort = 0

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    var baseUrl: String? = null

    @BeforeEach
    fun initAll() {
        baseUrl = "http://localhost:$localServerPort${context.contextPath}"
        wireMockServer.resetAll()
    }

    @Autowired
    lateinit var endpoints: EndpointProperties

    //мок для get запросов
    fun getWireMockStubGet(
        testUrl: String,
        expectedResponse: Any,
        httpStatus: HttpStatus = OK,
        delayTime: Int = 0,
    ) {
        val mappingBuilder = WireMock.get(WireMock.urlEqualTo(testUrl))
        return createWireMockStub(mappingBuilder, expectedResponse, httpStatus, delayTime)
    }

    //мок для post запросов
    fun getWireMockStubPost(
        testUrl: String,
        expectedResponse: Any?,
        httpStatus: HttpStatus = OK,
        delayTime: Int = 0,
    ) {
        val mappingBuilder = WireMock.post(WireMock.urlEqualTo(testUrl))
        return createWireMockStub(mappingBuilder, expectedResponse, httpStatus, delayTime)
    }

    fun assertWireMockRequest(url: String, obj: Any) {
        wireMockServer.verify(
            1, RequestPatternBuilder.newRequestPattern()
                .withRequestBody(EqualToJsonPattern(objectMapper.valueToTree(obj), true, true))
                .withUrl(url)
        )
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
}