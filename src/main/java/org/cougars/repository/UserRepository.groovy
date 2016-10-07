package org.cougars.repository

import org.cougars.domain.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Dennis Rausch on 10/7/16.
 */
interface UserRepository extends JpaRepository<User, Long> {
}
