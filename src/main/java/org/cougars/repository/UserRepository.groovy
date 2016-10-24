package org.cougars.repository

import org.cougars.domain.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Dennis Rausch on 10/7/16.
 */
interface UserRepository extends JpaRepository<User, String> {
    /** Find a user by its id (primary key).
     *
     * @param id    Id of the user being searched for.
     * @return      User with the referenced id.
     */
    User findByUsername(String username)
}
