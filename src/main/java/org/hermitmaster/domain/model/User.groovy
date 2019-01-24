package org.hermitmaster.domain.model

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

/** Represents a user of the system.
 * Created by hermitmaster on 10/4/16.
 */

@Entity
@Table(name = "users")
class User {
    @Id
    @Column(name = "username", unique = true)
    String username

    @Column
    String firstName

    @Column
    String lastName

    @Column(name = "password", nullable = false)
    String password

    @Column(name = "enabled", nullable = false)
    Boolean enabled = true

    @Column(name = "registrationDate", nullable = false, updatable = false)
    Date registrationDate = new Date()

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<Authority> authorities = new HashSet<>()

    String getAuthoritiesList() {
        return authorities*.authority.join(", ")
    }

    boolean isAdmin() {
        getAuthoritiesList().contains("admin")
    }
}
