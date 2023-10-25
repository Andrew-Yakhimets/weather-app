package weather.app.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.junit.jupiter.Testcontainers
import weather.app.dto.WeatherDto
import weather.app.repo.WeatherRepository
import weather.app.testutil.Factory
import weather.app.testutil.objectMapper
import java.time.LocalDate

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WeatherControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val weatherRepository: WeatherRepository,
) : AbstractTestcontainersIntegrationTest() {

    @AfterEach
    fun cleanUp() {
        weatherRepository.deleteAll()
    }

    @WithMockUser(authorities = ["EDITOR"])
    @Test
    fun saveWeatherTest() {
        val weatherDto = Factory.weatherDto()

        val response = mockMvc.perform(
            post("/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weatherDto))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .response
            .contentAsString

        val actualDto = objectMapper.readValue(response, WeatherDto::class.java)

        assertEquals(weatherDto.city, actualDto.city)
        assertEquals(weatherDto.state, actualDto.state)
        assertEquals(weatherDto.date, actualDto.date)
        assertEquals(weatherDto.lon, actualDto.lon)
        assertEquals(weatherDto.lat, actualDto.lat)
        assertEquals(weatherDto.temperatures, actualDto.temperatures)

        assertEquals(actualDto.id, weatherRepository.findAll().first().id)
    }

    @WithMockUser(authorities = ["READER"])
    @Test
    fun saveWeatherTestForbidden() {
        val weatherDto = Factory.weatherDto()

        mockMvc.perform(
            post("/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weatherDto))
        )
            .andExpect(status().isForbidden())
    }

    @WithMockUser(authorities = ["EDITOR"])
    @Test
    fun findById() {
        val weather = Factory.weather()
        weatherRepository.save(weather)

        val response = mockMvc.perform(get("/weather/${weather.id}"))
            .andExpect(status().isOk())
            .andReturn()
            .response
            .contentAsString

        val weatherDto = objectMapper.readValue(response, WeatherDto::class.java)

        assertEquals(weather.id, weatherDto.id)
        assertEquals(weather.city, weatherDto.city)
        assertEquals(weather.state, weatherDto.state)
        assertEquals(weather.date, weatherDto.date)
        assertEquals(weather.lon, weatherDto.lon)
        assertEquals(weather.lat, weatherDto.lat)
        assertEquals(weather.temperatures, weatherDto.temperatures)
    }

    @WithMockUser(authorities = ["EDITOR"])
    @Test
    fun findByIdNotFound() {
        mockMvc.perform(get("/weather/wrong-id"))
            .andExpect(status().isNotFound())
    }

    @WithMockUser(authorities = ["EDITOR"])
    @Test
    fun findAllFilteredByCity() {
        val weather1 = Factory.weather(city = "Lviv", id = "4")
        val weather2 = Factory.weather(city = "Kyiv", id = "3")
        val weather3 = Factory.weather(city = "LVIV", id = "1")
        val weather4 = Factory.weather(city = "Odesa", id = "2")
        weatherRepository.save(weather1)
        weatherRepository.save(weather2)
        weatherRepository.save(weather3)
        weatherRepository.save(weather4)

        val response = mockMvc.perform(
            get("/weather")
                .param("city", "Lviv,Odesa")
        )
            .andExpect(status().isOk())
            .andReturn()
            .response
            .contentAsString

        val weatherDtos = objectMapper.readValue(response, Array<WeatherDto>::class.java)

        assertEquals(3, weatherDtos.size)
        assertEquals("1", weatherDtos[0].id)
        assertEquals("2", weatherDtos[1].id)
        assertEquals("4", weatherDtos[2].id)
    }

    @WithMockUser(authorities = ["EDITOR"])
    @Test
    fun findAllFilteredByDate() {
        val weather1 = Factory.weather(date = LocalDate.now().minusDays(1))
        val weather2 = Factory.weather(date = LocalDate.now())
        val weather3 = Factory.weather(date = LocalDate.now().minusDays(1))
        val weather4 = Factory.weather(date = LocalDate.now())
        weatherRepository.save(weather1)
        weatherRepository.save(weather2)
        weatherRepository.save(weather3)
        weatherRepository.save(weather4)

        val response = mockMvc.perform(
            get("/weather")
                .param("date", LocalDate.now().toString())
        )
            .andExpect(status().isOk())
            .andReturn()
            .response
            .contentAsString

        val weatherDtos = objectMapper.readValue(response, Array<WeatherDto>::class.java)

        assertEquals(2, weatherDtos.size)
        assertEquals(weather2.id, weatherDtos[0].id)
        assertEquals(weather4.id, weatherDtos[1].id)
    }

    @WithMockUser(authorities = ["EDITOR"])
    @Test
    fun findAllSortedByDateAsc() {
        val weather1 = Factory.weather(date = LocalDate.now().minusDays(1), id = "2")
        val weather2 = Factory.weather(date = LocalDate.now().plusDays(1))
        val weather3 = Factory.weather(date = LocalDate.now().minusDays(1), id = "1")
        val weather4 = Factory.weather(date = LocalDate.now().minusDays(4))
        weatherRepository.save(weather1)
        weatherRepository.save(weather2)
        weatherRepository.save(weather3)
        weatherRepository.save(weather4)

        val response = mockMvc.perform(
            get("/weather")
                .param("sort", "date")
        )
            .andExpect(status().isOk())
            .andReturn()
            .response
            .contentAsString

        val weatherDtos = objectMapper.readValue(response, Array<WeatherDto>::class.java)

        assertEquals(4, weatherDtos.size)
        assertEquals(weather4.id, weatherDtos[0].id)
        assertEquals(weather3.id, weatherDtos[1].id)
        assertEquals(weather1.id, weatherDtos[2].id)
        assertEquals(weather2.id, weatherDtos[3].id)
    }

    @WithMockUser(authorities = ["EDITOR"])
    @Test
    fun findAllSortedByDateDesc() {
        val weather1 = Factory.weather(date = LocalDate.now().minusDays(1), id = "2")
        val weather2 = Factory.weather(date = LocalDate.now().plusDays(1))
        val weather3 = Factory.weather(date = LocalDate.now().minusDays(1), id = "1")
        val weather4 = Factory.weather(date = LocalDate.now().minusDays(4))
        weatherRepository.save(weather1)
        weatherRepository.save(weather2)
        weatherRepository.save(weather3)
        weatherRepository.save(weather4)

        val response = mockMvc.perform(
            get("/weather")
                .param("sort", "-date")
        )
            .andExpect(status().isOk())
            .andReturn()
            .response
            .contentAsString

        val weatherDtos = objectMapper.readValue(response, Array<WeatherDto>::class.java)

        assertEquals(4, weatherDtos.size)
        assertEquals(weather2.id, weatherDtos[0].id)
        assertEquals(weather3.id, weatherDtos[1].id)
        assertEquals(weather1.id, weatherDtos[2].id)
        assertEquals(weather4.id, weatherDtos[3].id)
    }
}
