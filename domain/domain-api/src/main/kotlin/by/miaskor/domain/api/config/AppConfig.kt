package by.miaskor.domain.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
open class AppConfig {

  @Bean
  open fun passwordEncoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
  }
}
