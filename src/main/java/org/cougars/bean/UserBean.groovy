package org.cougars.bean

import javax.validation.constraints.Size

/** A bean for holding "Add User" form data.
 * Created by Dennis Rausch on 10/24/16.
 */

class UserBean {
    @Size(min = 6, max = 255, message = "Username must be 6 - 255 characters!")
    String username

    @Size(min = 8, message = "Password must be at least 8 characters!")
    String password
}
