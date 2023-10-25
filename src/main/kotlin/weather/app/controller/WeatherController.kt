package weather.app.controller

import org.springframework.http.HttpStatus.CREATED
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import weather.app.dto.WeatherDto
import weather.app.service.WeatherService
import weather.app.utils.*
import java.time.LocalDate

@RestController
@RequestMapping("/weather")
class WeatherController(
    private val weatherService: WeatherService
) {

    @PreAuthorize("hasAuthority(T(weather.app.entities.Role).EDITOR)")
    @PostMapping
    @ResponseStatus(CREATED)
    fun saveWeather(@RequestBody weatherDto: WeatherDto): WeatherDto {
        return weatherService.addWeatherRecord(weatherDto)
    }

    @GetMapping
    fun findAll(
        @RequestParam(required = false) date: LocalDate?,
        @RequestParam(required = false) city: List<String>?,
        @RequestParam(required = false) sort: String?
    ): List<WeatherDto> {
        val filters = prepareFilters(
            FilterData(fieldName = "date", value = date, type = FilterType.EQ),
            FilterData(fieldName = "city", value = city, type = FilterType.IN, ignoreCase = true)
        )

        val sort = prepareSort(
            SortData(value = sort),
            SortData(value = "id")
        )

        return weatherService.findAll(filters, sort)
    }

    @GetMapping("/{id}")
    fun findWeatherById(@PathVariable id: String): WeatherDto {
        return weatherService.findById(id)
    }
}