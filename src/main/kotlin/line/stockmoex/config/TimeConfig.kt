package line.stockmoex.config

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TimeConfig {

    @Bean
    fun timeMetricConfig(meterRegistry: MeterRegistry): TimedAspect {
        return TimedAspect(meterRegistry)
    }
}
