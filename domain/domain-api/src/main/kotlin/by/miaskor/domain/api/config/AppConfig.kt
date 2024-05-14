package by.miaskor.domain.api.config

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

private const val BCRYPT = "bcrypt"
private const val NOOP = "noop"

@Configuration
open class AppConfig {

  @Bean
  open fun passwordEncoder(): PasswordEncoder {
    return DelegatingPasswordEncoder(
      BCRYPT,
      mapOf(
        BCRYPT to BCryptPasswordEncoder(),
        NOOP to NoOpPasswordEncoder.getInstance()
      )
    )
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
