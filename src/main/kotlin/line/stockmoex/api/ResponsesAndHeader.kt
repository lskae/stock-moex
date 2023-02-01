package line.stockmoex.api

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import line.stockmoex.model.ErrorInfo

/**
 *  Идентификатор запросов. Используется для сквозной трассировки запросов в headers.
 */
private const val RQUID = "b788d7ea-d511-11ec-9d64-0242ac120002"
private const val DESCRIPTION = "Уникальный идентификатор запроса. Используется для сквозной трассировки запросов. " +
        "Способ генерации - UUID"

@Target(AnnotationTarget.FUNCTION)
@Retention(value = AnnotationRetention.RUNTIME)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        headers = [Header(
            name = "RqUID",
            required = true,
            description = DESCRIPTION,
            schema = Schema(type = "string", example = RQUID)
        )]
    ),
    ApiResponse(
        responseCode = "400",
        content = [Content(schema = Schema(implementation = ErrorInfo::class))],
        headers = [Header(
            name = "RqUID",
            required = true,
            description = DESCRIPTION,
            schema = Schema(type = "string", example = RQUID)
        )]
    ),
    ApiResponse(
        responseCode = "500",
        content = [Content(schema = Schema(implementation = ErrorInfo::class))],
        headers = [Header(
            name = "RqUID",
            required = true,
            description = DESCRIPTION,
            schema = Schema(type = "string", example = RQUID)
        )]
    ),
)
@Parameter(
    name = "RqUID",
    description = DESCRIPTION,
    required = true,
    `in` = ParameterIn.HEADER,
    schema = Schema(type = "string", example = RQUID)
)
/**
 * Аннотация для создания Responses и Header.
 */
annotation class ResponsesAndHeader
