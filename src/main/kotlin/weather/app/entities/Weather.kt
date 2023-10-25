package weather.app.entities

import com.fasterxml.jackson.annotation.JsonFormat
import com.vladmihalcea.hibernate.type.array.DoubleArrayType
import com.vladmihalcea.hibernate.type.array.ListArrayType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.util.*


@Entity
@Table(name = "weathers")
data class Weather(
    @Id
    val id: String = UUID.randomUUID().toString(),
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var date: LocalDate,
    var lat: Float,
    var lon: Float,
    var city: String,
    var state: String,
    @Type(ListArrayType::class)
    @Column(columnDefinition = "double precision[]")
    var temperatures: List<Double>
)