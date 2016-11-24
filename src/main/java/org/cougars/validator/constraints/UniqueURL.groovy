package org.cougars.validator.constraints

import org.cougars.validator.constraintvalidators.UniqueURLValidator

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.ANNOTATION_TYPE
import static java.lang.annotation.ElementType.CONSTRUCTOR
import static java.lang.annotation.ElementType.FIELD
import static java.lang.annotation.ElementType.METHOD
import static java.lang.annotation.ElementType.PARAMETER
import static java.lang.annotation.RetentionPolicy.RUNTIME

/**
 * Created by Dennis Rausch on 11/24/16.
 */

@Documented
@Constraint(validatedBy = [ UniqueURLValidator ])
@Target([ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER ])
@Retention(RUNTIME)
public @interface UniqueURL {
    String message() default "{org.cougars.validator.constraints.UniqueURL.message}"

    Class<?>[] groups() default [ ]

    Class<? extends Payload>[] payload() default [ ]


    /*@Target([ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER ])
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        UniqueURL[] value();
    }*/
}