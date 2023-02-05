Данный микросервис осуществляет взаимодействие с API Московской биржи и имеет три endpoint
Постановка бизнес-задачи
Постановка тех задания
Регламентв выполнения техническо части

spring, kotlin
feign cloud
docker (загрузка образов и упаковка проекта)
работа с БД (postgres)
ликвибейз
создание аннотаций
тесты с разными профилями lля пг и h2
кастомные нативные sql 
prometheus, kibana

wiremock для интеграционных тестов
параметризованные тесты, поигрался с шрифтами с дженериками в котлине
тесты с jdbc template


swagger (добавить генерацию по апи в файл)
objectMapper
перехватчик кастомных эксепшинов
внедрить логгер фейн запросов (мб внешний логгер на основе фейна или логбука)
нарисовть схему
использовать шедулер Scheduled
мониторинг эндпоинта с актуальной ценой для мониторинга https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.json?iss.meta=off&iss.only=marketdata&marketdata.columns=SECID,LAST,LCURRENTPRICE

мониторинг эндпоинта с ценой за предыдущий день для кэша https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.json?iss.meta=off &iss.only=securities&securities.columns=SECID,PREVLEGALCLOSEPRICE,FACEVALUE

«Cтабильная» цена PREVLEGALCLOSEPRICE находится в securities, а «переменная» цена LCURRENTPRICE в marketdata, например: Если нужны какие-то динамические данные: текущая цена, исторические данные, то markets https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.json?iss.meta=off&iss.only=marketdata&marketdata.columns=SECID,LAST,LCURRENTPRICE

https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.json?iss.meta=off&iss.only=securities&securities.columns=SECID,PREVLEGALCLOSEPRICE,FACEVALUE

LCURRENTPRICE Официальная текущая цена, рассчитываемая как средневзвешенная цена сделок заключенных за последние 10 минут PREVADMITTEDQUOTE Признаваемая котировка предыдущего дня, цена за одну ценную бумагу

Пример сдвоенного json https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQTF/securities.json?iss.meta=off &iss.only=marketdata,securities&marketdata.columns=SECID,LCURRENTPRICE&securities.columns=SECID,PREVLEGALCLOSEPRICE,FACEVALUE

Описания параметров http://ftp.moex.com/pub/ClientsAPI/ASTS/Bridge_Interfaces/Equities/Equities44_Broker_Russian.htm
