package weather.app.testutil

import weather.app.dto.WeatherDto
import weather.app.entities.Weather
import java.time.LocalDate
import java.util.*

object Factory {
    fun weatherDto() = WeatherDto(
        id = UUID.randomUUID().toString(),
        date = LocalDate.now(),
        lat = 1f,
        lon = 1f,
        city = "Lviv",
        state = "U",
        temperatures = listOf()
    )

    fun weather(city: String = "Lviv", id: String = UUID.randomUUID().toString(), date: LocalDate = LocalDate.now()) =
        Weather(
            id = id,
            date = date,
            lat = 1f,
            lon = 1f,
            city = city,
            state = "U",
            temperatures = listOf()
        )

    fun weatherFromDto(weatherDto: WeatherDto): Weather {
        return Weather(
            id = weatherDto.id!!,
            date = weatherDto.date,
            lat = weatherDto.lat,
            lon = weatherDto.lon,
            city = weatherDto.city,
            state = weatherDto.state,
            temperatures = weatherDto.temperatures
        )
    }

    fun weatherDtoFromEntity(weather: Weather) = WeatherDto(
        id = weather.id,
        date = weather.date,
        lat = weather.lat,
        lon = weather.lon,
        city = weather.city,
        state = weather.state,
        temperatures = weather.temperatures
    )
}