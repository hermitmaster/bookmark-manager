package org.hermitmaster.configuration

import org.hermitmaster.domain.model.Authority
import org.hermitmaster.domain.model.BookmarkCategory
import org.hermitmaster.domain.model.User
import org.hermitmaster.domain.repository.BookmarkCategoryRepository
import org.hermitmaster.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class InitDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private UserRepository userRepository
    @Autowired
    private BookmarkCategoryRepository bookmarkCategoryRepository
    @Autowired
    private PasswordEncoder passwordEncoder

    boolean alreadySetup = false

    @Override
    @Transactional
    void onApplicationEvent(ContextRefreshedEvent event) {
        if (!alreadySetup) {
            User user = new User([
                username   : "admin",
                password   : passwordEncoder.encode("password")
            ])

            user = userRepository.save(user)
            user.authorities = Arrays.asList(new Authority([authority: "ROLE_ADMIN", user: user]))
            user = userRepository.save(user)
            createDefaultBookmarkCategoryIfNotFound(user)
        }

        alreadySetup = true
    }

    @Transactional
    BookmarkCategory createDefaultBookmarkCategoryIfNotFound(User user) {
        return bookmarkCategoryRepository.save(new BookmarkCategory([name: "None", createdBy: user, parent: null]))
    }
}
