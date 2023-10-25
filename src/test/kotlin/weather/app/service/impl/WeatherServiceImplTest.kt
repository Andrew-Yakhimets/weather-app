package weather.app.service.impl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Sort
import weather.app.entities.Weather
import weather.app.exception.EntityNotFoundException
import weather.app.repo.WeatherRepository
import weather.app.testutil.Factory
import weather.app.utils.FilterRule
import weather.app.utils.FilterSpecification
import weather.app.utils.FilterType
import java.util.*


@ExtendWith(MockitoExtension::class)
class WeatherServiceImplTest {
    private val weatherRepository = mock(WeatherRepository::class.java)
    private val weatherServiceImpl = WeatherServiceImpl(weatherRepository)

    @Test
    fun addWeatherRecordTest() {
        val weatherDto = Factory.weatherDto()
        val weather = Factory.weatherFromDto(weatherDto)

        `when`(weatherRepository.save(any())).thenReturn(weather)

        val result = weatherServiceImpl.addWeatherRecord(weatherDto)

        verify(weatherRepository).save(any())
        assertEquals(weatherDto, result)
    }

    @Test
    fun findAllTest() {
        val specificationClass: Class<FilterSpecification<Weather>> =
            FilterSpecification::class.java as Class<FilterSpecification<Weather>>
        val captor: ArgumentCaptor<FilterSpecification<Weather>> = ArgumentCaptor.forClass(specificationClass)

        val filterRules = listOf(FilterRule("city", "Lviv", FilterType.EQ))
        val sort = Sort.by(Sort.Order.asc("id"))

        val weathers = listOf(Factory.weather(), Factory.weather())
        val weatherDtos = weathers.map { Factory.weatherDtoFromEntity(it) }

        `when`(weatherRepository.findAll(any(), eq(sort))).thenReturn(weathers)

        val result = weatherServiceImpl.findAll(filterRules, sort)

        verify(weatherRepository).findAll(captor.capture(), eq(sort))

        assertEquals(weatherDtos, result)
        assertEquals(filterRules, captor.value.filters)
    }

    @Test
    fun findByIdTest() {
        val id = UUID.randomUUID().toString()
        val weather = Factory.weather()
        val weatherDto = Factory.weatherDtoFromEntity(weather)

        `when`(weatherRepository.findById(id)).thenReturn(Optional.of(weather))

        val result = weatherServiceImpl.findById(id)

        verify(weatherRepository).findById(id)
        assertEquals(weatherDto, result)
    }

    @Test
    fun findByIdEntityNotFoundTest() {
        val id = UUID.randomUUID().toString()

        `when`(weatherRepository.findById(id)).thenReturn(Optional.empty())

        Assertions.assertThrows(EntityNotFoundException::class.java) { weatherServiceImpl.findById(id) }
    }
}

