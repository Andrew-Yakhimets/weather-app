package weather.app.dto.auth

data class AuthenticationRequest(
    var email: String,
    var password: String
)
