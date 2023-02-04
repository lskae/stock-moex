package line.stockmoex.controller

import line.stockmoex.exception.NotValidTickerRequestException
import line.stockmoex.model.ErrorInfo
import line.stockmoex.model.commonError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Обработка исключения NotValidTickerRequestException
     * @param e исключение
     * @return ResponseEntity
     */
    @ExceptionHandler(NotValidTickerRequestException::class)
    fun handle(e: NotValidTickerRequestException): ResponseEntity<ErrorInfo> {
        log.error("Запрос содержит некорректное наименование тикеров", e)
        val body = commonError()
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }
}
