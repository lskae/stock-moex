package line.stockmoex.exception

/**
 * Исключения обрабатывающее некорректные запросы к микросервису
 */
class NotValidTickerRequestException(message: String) : RuntimeException(message)
