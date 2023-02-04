package line.stockmoex.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Error", description = "Информация об ошибке")
data class ErrorInfo(

    @field:Schema(description = "Код ошибки", required = true, example = "1")
    val code: String,

    @field:Schema(description = "Сообщение об ошибке", example = "Что-то пошло не так", required = true)
    val message: String,
)

    /**
    * Общая возникающая при некорректном запросе.
    */
    fun commonError() = ErrorInfo(
    code = "1",
    message = "Запрос не содержит тиккеров акций, которые торгуются на Московской Бирже")


