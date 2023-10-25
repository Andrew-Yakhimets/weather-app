package weather.app.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import weather.app.entities.User
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findByEmail(email: String): User?
}