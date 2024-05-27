package by.miaskor.common.exception.handler.config

import by.miaskor.common.exception.handler.advice.ExceptionHandler
import by.miaskor.common.exception.handler.advice.ResponseHandler
import by.miaskor.common.exception.handler.controller.CustomErrorController
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration::class)
open class ExceptionHandlerAutoConfiguration {

  @Bean
  open fun exceptionHandler(): ExceptionHandler {
    return ExceptionHandler()
  }

  @Bean
  open fun responseHandler(): ResponseHandler {
    return ResponseHandler()
  }

  @Bean
  @ConditionalOnMissingBean(ErrorController::class)
  open fun customErrorController(): CustomErrorController {
    return CustomErrorController(DefaultErrorAttributes())
  }
}