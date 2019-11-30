package org.hermitmaster.validation.constraintvalidator

import org.hermitmaster.domain.repository.BookmarkRepository
import org.hermitmaster.validation.constraint.UniqueURL
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class UniqueURLValidator implements ConstraintValidator<UniqueURL, String> {
    @Autowired
    BookmarkRepository br

    String url

    @Override
    void initialize(UniqueURL uniqueURL) {
    }

    @Override
    boolean isValid(String value, ConstraintValidatorContext context) {
        String url = value.trim().replaceAll(/\/$/, "")

        return !br.findByUrl(url)
    }
}
