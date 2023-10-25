package weather.app.exception.dto

data class ErrorResponse (
    val status: Int,
    val message: String?,
)