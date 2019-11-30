package org.hermitmaster.validation.constraint

import org.hermitmaster.validation.constraintvalidator.UniqueURLValidator

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.FIELD
import static java.lang.annotation.RetentionPolicy.RUNTIME

/**
 * Created by hermitmaster on 11/24/16.
 */

@Documented
@Constraint(validatedBy = [UniqueURLValidator])
@Target([FIELD])
@Retention(RUNTIME)
@interface UniqueURL {
    String message() default "{UniqueURL.message}"

    Class<?>[] groups() default []

    Class<? extends Payload>[] payload() default []
}
