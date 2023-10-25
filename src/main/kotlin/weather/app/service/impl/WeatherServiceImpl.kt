package weather.app.service.impl

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import weather.app.dto.WeatherDto
import weather.app.entities.Weather
import weather.app.exception.EntityNotFoundException
import weather.app.repo.WeatherRepository
import weather.app.service.WeatherService
import weather.app.utils.FilterRule
import weather.app.utils.FilterSpecification

@Service
class WeatherServiceImpl(
    private val weatherRepository: WeatherRepository
) : WeatherService {

    override fun addWeatherRecord(weatherDto: WeatherDto): WeatherDto {
        return mapToDto(weatherRepository.save(mapToEntity(weatherDto)))
    }

    override fun findAll(filterRules: List<FilterRule>, sort: Sort): List<WeatherDto> {
        val specification = FilterSpecification<Weather>(filterRules)
        return weatherRepository.findAll(specification, sort)
            .map { mapToDto(it) }
    }

    override fun findById(id: String): WeatherDto {
        return mapToDto(weatherRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Can't find weather by id $id") })
    }

    private fun mapToEntity(weatherDto: WeatherDto): Weather {
        return Weather(
            date = weatherDto.date,
            lat = weatherDto.lat,
            lon = weatherDto.lon,
            city = weatherDto.city,
            state = weatherDto.state,
            temperatures = weatherDto.temperatures
        )
    }

    private fun mapToDto(weather: Weather): WeatherDto {
        return WeatherDto(
            id = weather.id,
            date = weather.date,
            lat = weather.lat,
            lon = weather.lon,
            city = weather.city,
            state = weather.state,
            temperatures = weather.temperatures
        )
    }
}
