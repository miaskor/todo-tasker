package by.miaskor.domain.api.config

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
open class AppConfig {

  @Bean
  open fun passwordEncoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Bean
  open fun configurer(
    @Value("\${spring.application.name}") applicationName: String?,
  ): MeterRegistryCustomizer<MeterRegistry> {
    return MeterRegistryCustomizer { registry: MeterRegistry ->
      registry.config().commonTags("application", applicationName)
    }
  }
}
