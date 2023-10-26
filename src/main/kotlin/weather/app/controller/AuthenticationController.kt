package weather.app.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import weather.app.dto.auth.AuthenticationRequest
import weather.app.dto.auth.AuthenticationResponse
import weather.app.service.impl.AuthenticationService
import weather.app.dto.auth.RegistrationRequest
import java.util.logging.Logger

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private var service: AuthenticationService
) {
    val log = Logger.getLogger(this.javaClass.name)

    @PostMapping("/register")
    fun register(@RequestBody request: RegistrationRequest): AuthenticationResponse {
        log.info("Request on registration")
        return service.register(request)
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthenticationRequest): AuthenticationResponse {
        log.info("Request on authentication")
        return service.authenticate(request)
    }
}