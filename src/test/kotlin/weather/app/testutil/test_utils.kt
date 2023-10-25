package weather.app.testutil

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.testcontainers.containers.PostgreSQLContainer

val objectMapper = ObjectMapper().apply {
    registerModule(JavaTimeModule())
    registerKotlinModule()
}
