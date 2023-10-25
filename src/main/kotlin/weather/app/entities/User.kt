package weather.app.entities

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    private val id: String = UUID.randomUUID().toString(),
    private var email: String,
    private var password: String,
    @Enumerated(EnumType.STRING)
    var role: Role
) : UserDetails {
    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority(role.name))

    override fun getPassword() = password

    override fun getUsername() = email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}

enum class Role {
    READER,
    EDITOR,
}