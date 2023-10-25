package weather.app.dto

import java.time.LocalDate

data class WeatherDto(
    var id: String?,
    var date: LocalDate,
    var lat: Float,
    var lon: Float,
    var city: String,
    var state: String,
    var temperatures: List<Double>
)
