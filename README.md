Данный микросервис **stock-moex** осуществляет взаимодействие с API Московской биржи
<img width="1792" alt="Снимок экрана 2023-02-19 в 12 05 56" src="https://user-images.githubusercontent.com/121794893/219939017-32a0a5ae-755f-4eff-8a47-60cf62a3961b.png">

Общая схема

**1. Постановка бизнес-задачи:**

Московская биржа предоставляет инструменты для работы со своим API https://www.moex.com/a2193
Описание параметров http://ftp.moex.com/pub/ClientsAPI/ASTS/Bridge_Interfaces/Equities/Equities44_Broker_Russian.htm

Данный микросервис имеет три эндпоинта:

**/getCurrentPrice** - получение информации по котировкам акций в режиме реального времени в ходе торгов на Московской бирже
https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.json?iss.meta=off&iss.only=marketdata&**marketdata**

**/getPreviousDayPrice** - получение кэшированной информации по котировкам акций за предыдущий торговый день. 
Информация сохраняется в кэше микросервиса, потому что котировки за предыдущий торговый день не меняются в момент вызова эндпоинта
в данном микросервисе, поэтому целесообразной использовать Cacheable для экономии ресурсов и времени.
https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.json?iss.meta=off &iss.only=securities&**securities**

**/getTimeStatistic** - логгирование информации о запросах по эндпоинту getCurrentPrice для построения метрик мониторинг 
для отслеживания статистики по интересующим бизнес параметрам. В данном микросервисе мы отслеживаем то, 
насколько участники торгов интересуются информацией по тикеру SBER.


**2. Описание стека технологий**

Микровсерис разработан на **Kotlin** 213-1.8.10(java 11) + **Spring Boot** (2.6.9)<br>
Для обращения к внешним сервисам используется **Spring Cloud OpenFeign**<br>
БД **PostgreSQL**, контейнер которой поднят в **Docker**, файл для создания docker-compose.yaml<br>
Создание таблиц и версионирование **Liquibase**<br>
Мониторинг Spring Boot c помощью **Spring Actuator** и **Prometheus** с графическим отображением метрик в **Grafana**,<br>
образ которой вместе с приложением также поднимаем в docker-compose.yaml
Для работы с БД используется JPA Репозиторий с кастомными нативными **SQL** запросами<br>
**Кастомные аннотации** для объединения типовых Swagger аннотаций src/test/kotlin/line/stockmoex/GeneralControllerTest.kt fun openApiDocGeneration() <br>
Генерация **openapi** документа по **swagger** аннотациям<br>
Перехват ошибок от внешних сервисов и ошибок возникающих в процессе работы приложения осуществляет **RestControllerAdvice**
<br>
Тестирование осуществляется с помощью **интеграционных** тестов и **юнитов** тесты.<br>
Интеграционные тесты используют **WireMock** для создания заглушек для внешних сервисов.<br>
Чтение json файлов, с помощью objectMapper<br>
Для интеграционных тестов используются **параметризованные тесты** для оптимизации кода и сокращение его объема<br>
