package org.cougars.validator.constraintvalidators

import org.cougars.repository.BookmarkRepository
import org.cougars.validator.constraints.UniqueURL
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Created by Dennis Rausch on 11/24/16.
 */
class UniqueURLValidator implements ConstraintValidator<UniqueURL, String> {
    @Autowired BookmarkRepository br

    String url

    @Override
    void initialize(UniqueURL uniqueURL) {
    }

    @Override
    boolean isValid(String value, ConstraintValidatorContext context) {
        return !br.findByUrl(value)
    }
}
