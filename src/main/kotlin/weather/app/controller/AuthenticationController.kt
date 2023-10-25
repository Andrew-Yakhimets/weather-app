package weather.app.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import weather.app.dto.auth.AuthenticationRequest
import weather.app.dto.auth.AuthenticationResponse
import weather.app.service.impl.AuthenticationService
import weather.app.dto.auth.RegistrationRequest

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private var service: AuthenticationService
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegistrationRequest): AuthenticationResponse {
        return service.register(request)
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthenticationRequest): AuthenticationResponse {
        return service.authenticate(request)
    }
}