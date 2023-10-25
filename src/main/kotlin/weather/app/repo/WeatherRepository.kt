package weather.app.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import weather.app.entities.Weather
import java.time.LocalDate

@Repository
interface WeatherRepository : JpaRepository<Weather, String>, JpaSpecificationExecutor<Weather>