package weather.app.service

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import weather.app.dto.WeatherDto
import weather.app.entities.Weather
import weather.app.utils.FilterRule

@Service
interface WeatherService {
    fun addWeatherRecord(weatherDto: WeatherDto): WeatherDto
    fun findAll(filterRules: List<FilterRule>, sort: Sort): List<WeatherDto>
    fun findById(id: String): WeatherDto
}