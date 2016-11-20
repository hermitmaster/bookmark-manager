package org.cougars.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

/** Represents Spring Security Authority objects.
 * Created by Dennis Rausch on 10/23/16.
 */
@Entity
@Table(name = "authorities", uniqueConstraints = @UniqueConstraint(columnNames = ["authority", "username"]))
public class Authority {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    User user

    @Column(name = "authority", nullable = false)
    String authority = "user"

    Authority() {
        super()
    }

    Authority(User user, String authority) {
        this.user = user
        this.authority = authority
    }
}
