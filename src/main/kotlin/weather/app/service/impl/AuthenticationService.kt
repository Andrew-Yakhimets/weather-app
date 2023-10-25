package weather.app.service.impl

import jakarta.persistence.EntityNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import weather.app.JwtService
import weather.app.dto.auth.AuthenticationRequest
import weather.app.dto.auth.AuthenticationResponse
import weather.app.dto.auth.RegistrationRequest
import weather.app.entities.Role
import weather.app.entities.User
import weather.app.exception.UserAlreadyRegisteredException
import weather.app.repo.UserRepository

@Service
class AuthenticationService(
    private var userRepo: UserRepository,
    private var passwordEncoder: PasswordEncoder,
    private var jwtService: JwtService
) {
    fun register(request: RegistrationRequest): AuthenticationResponse {
        if (userRepo.findByEmail(request.email) != null) {
            throw UserAlreadyRegisteredException("User with such email already exists")
        }
        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = Role.EDITOR
        )
        userRepo.save(user)
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(token = jwtToken)
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        val user = userRepo.findByEmail(request.email)
            ?: throw EntityNotFoundException("cant find user with such email:  ${request.email}")
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }
}