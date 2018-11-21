package org.hermitmaster.domain.repository

import org.hermitmaster.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

/** A JPA repository for accessing User data
 * Created by hermitmaster on 10/7/16.
 */

interface UserRepository extends JpaRepository<User, String> {
    /** Find a user by its id (primary key).
     *
     * @param id Id of the user being searched for.
     * @return User with the referenced id.
     */
    User findByUsername(String username)

    /**
     *
     * @return
     */
    Set<User> findByEnabledTrue()
}
