package line.stockmoex

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import java.io.File
import java.nio.file.Paths

class GeneralControllerTest : IntegrationBaseTest() {

    /**
     * Тест загрузки контекста и работоспособности endpoint актуатора.
     */
    @Test
    fun contextLoads() {
        val actuatorInfo = testRestTemplate.getForEntity(
            "/actuator/info",
            String::class.java
        )
        assertEquals(HttpStatus.OK, actuatorInfo.statusCode)
    }

    /**
     * Тест readiness пробы.
     */
    @Test
    fun testReadiness() {
        val readiness = testRestTemplate.getForEntity("/actuator/health/readiness", HealthStatus::class.java)
        assertEquals(HttpStatus.OK, readiness.statusCode)
        Assertions.assertNotNull(readiness.body)
        assertEquals("UP", readiness.body?.status)
    }

    /**
     * Генерация спецификации по аннотациям open-api
     */
    @Test
    fun openApiDocGeneration() {
        val jsonSpec = testRestTemplate.getForEntity<String>("/v3/api-docs")
        assertEquals(HttpStatus.OK, jsonSpec.statusCode)
        val mapper = ObjectMapper(YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES))
        val jsonNodeTree = mapper.readTree(jsonSpec.body)
        // т.к. в тестах каждый раз используется разный порт,
        // нужно явно установить значение servers.url, чтобы не модифицировался файл спецификации
        (jsonNodeTree.path("servers")[0] as ObjectNode)
            .put("url", "http://localhost:8080/stock-moex-app-api")
        val ymlSpec = mapper.writeValueAsString(jsonNodeTree)
        val resourceDirectory = Paths.get("src", "main", "resources", "stock-moex.yml")
        val absolutePath = resourceDirectory.toFile().absolutePath
        File(absolutePath).writeText(ymlSpec)
    }
}

/**
 * dto для readiness и liveness.
 */
data class HealthStatus(
    val status: String
)
