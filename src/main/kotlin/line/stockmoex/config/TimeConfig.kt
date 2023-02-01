package line.stockmoex.config

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TimeConfig {
    /**
     * Кастомный бин для создания метрик мониторинга по аннотации @Time
     */
    @Bean
    fun timeMetricConfig(meterRegistry: MeterRegistry): TimedAspect {
        return TimedAspect(meterRegistry)
    }
}
