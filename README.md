# stock-moex
Данный микросервис **stock-moex** осуществляет взаимодействие с API Московской биржи

## Общая схема работы микросервиса
<img width="1791" alt="1111" src="https://user-images.githubusercontent.com/121794893/219948960-4436148a-34e2-40e2-853a-14042707119a.png">


## Используемое API
Московская Биржа предоставляет инструменты для работы со своим API https://www.moex.com/a2193

Описание параметров http://ftp.moex.com/pub/ClientsAPI/ASTS/Bridge_Interfaces/Equities/Equities44_Broker_Russian.ht
## Описание возможностей микросервиса
Базовый URL к микросервису: http://localhost:8080/stock-moex-app-api/v1
- **/getCurrentPrice** - получение информации по котировкам акций в режиме реального времени в ходе торгов на Московской Бирже.
  URL запроса к Московской Бирже:
  https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.json?iss.meta=off&iss.only=marketdata&**marketdata**
- **/getPreviousDayPrice** - получение кэшированной информации по котировкам акций за предыдущий торговый день.
  Информация сохраняется в кэше микросервиса, потому что котировки за предыдущий торговый день не меняются в момент вызова эндпоинта в данном микросервисе, поэтому целесообразной использовать **Cacheable** и **caffeine** для экономии ресурсов и времени.
  https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.json?iss.meta=off &iss.only=securities&**securities**

- **/getTimeStatistic** - логирование информации о запросах по эндпоинту **/getCurrentPrice** для построения метрик мониторинг для отслеживания статистики по интересующим бизнес параметрам. В данном микросервисе мы отслеживаем то, насколько участники торгов интересуются информацией по тикеру SBER.

## Описание стека технологий
- Микровсерис разработан на **Kotlin** 213-1.8.10(Java 11) + **Spring Boot** (2.6.9)
- Для обращения к внешним сервисам используется **Spring Cloud OpenFeign** (интерфейс src/main/kotlin/line/stockmoex/moex/MoexClient.kt)
  Включено логирование запросов к FeignClient **logging:level: DEBUG** в файле конфигурации src/main/resources/application.yml
- Взаимодействие с БД происходит в  **PostgreSQL**, контейнер которой поднят в Docker, файл для создания **docker-compose.yaml**, который располагается в корне приложения stock-moex
- Создание таблиц и версионирование **Liquibase**, скрипты создания таблиц в src/main/resources/liquibase
- Мониторинг Spring Boot c помощью **Spring Actuator** и **Prometheus** с графическим отображением метрик в **Grafana**, образы Prometheus и Grafana вместе с приложением также поднимаются в **docker-compose.yaml**, конфиги для метрик в **stock-moex/config**
  Используются как стандартные метрики **@Timed** так и настраиваимые **meterRegistry.gauge("metricCustomMaxPrice", value)** (метрика позволяющая отслеживать число запросов по конкретной бумаге (например SBER))
- Для работы с БД используется JPA Репозиторий с кастомными **нативными SQL** запросами в интерфейсе src/main/kotlin/line/stockmoex/repository/StatisticRequestRepository.kt
- **Кастомные аннотации** для объединения типовых Swagger аннотаций в src/main/kotlin/line/stockmoex/api/ResponsesAndHeader.kt
- Генерация **openapi** документа **src/main/resources/stock-moex.yml** по **swagger** аннотациям в тесте **fun openApiDocGeneration()** в src/test/kotlin/line/stockmoex/GeneralControllerTest.kt
- Перехват ошибок от внешних сервисов и ошибок возникающих в процессе работы приложения осуществляет **RestControllerAdvice** на основе объекта src/main/kotlin/line/stockmoex/model/ErrorInfo.kt
- Тестирование осуществляется с помощью **интеграционных** тестов src/test/kotlin/line/stockmoex/integration с поднятием контекста на основе  testResttemplae и **юнит** тестов src/test/kotlin/line/stockmoex/mockmapper/MoexMapperTest.kt.
  **Интеграционные** тесты используют **WireMock** для создания заглушек для внешних сервисов **fun createWireMockStub** в src/test/kotlin/line/stockmoex/IntegrationBaseTest.kt.
- Для интеграционных тестов используются **параметризованные тесты** для оптимизации кода и сокращения его объема **fun sourceParams(): Stream<Arguments>**
- Плагин для тестов **jacoco** присутствует
- Чтение json файлов, с помощью **objectMapper**
- Плагин для "чистого" кода **Detekt**


## Docker

Для запуска приложения stock-moex, PostgreSQL, Grafana, Kibana из docker образа выполнить из корневого каталога /stock-moex команду:

```sh
docker compose up
```
Сборка образа приложения:
```sh
docker build -t lskae/stock-moex .
```
Образ приложения на https://hub.docker.com/ :
```sh
https://hub.docker.com/repository/docker/lskae/stock-moex/general
```
