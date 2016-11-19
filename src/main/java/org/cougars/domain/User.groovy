/**
 MIT License

 Copyright (c) 2016 MetroState-Cougars

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package org.cougars.domain

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

/** Represents a user of the system.
 * Created by Dennis Rausch on 10/4/16.
 */

@Entity
@Table(name = "users")
class User {
    @Id
    @Column(unique = true)
    String username

    @Column(nullable = false)
    String password

    @Column(nullable = false)
    Boolean enabled = true

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<Authority> authorities = new HashSet<>()

    @Column(nullable = false, updatable = false)
    Date registrationDate = new Date()

    User() {
        super()
    }

    User(String username, String password) {
        this.username = username
        this.password = new BCryptPasswordEncoder().encode(password)
        this.registrationDate = new Date()
        this.authorities.add(new Authority(this, "admin"))
    }

    String getAuthoritiesList() {
        return authorities*.authority.join(", ")
    }
}
