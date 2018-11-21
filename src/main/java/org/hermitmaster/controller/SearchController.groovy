package org.hermitmaster.controller

import groovy.util.logging.Slf4j
import org.hermitmaster.bean.BookmarkBean
import org.hermitmaster.domain.repository.BookmarkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.SortDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * Created by hermitmaster on 12/3/16.
 */

@Slf4j
@Controller
class SearchController {
    @Autowired
    private BookmarkRepository br

    @GetMapping("/search")
    String search(@RequestParam(value = "query") String query,
                  @SortDefault("dateCreated") Pageable pageable,
                  Model model) {
        model.addAttribute("page", br.search(query.trim(), pageable))

        return "table"
    }

    @GetMapping("/advanced-search")
    String advancedSearchPage(Model model) {
        //TODO: Add model attributes for category and subcategory to populate dynamic dropdowns on the view
        model.addAttribute("bookmarkBean", new BookmarkBean())

        return "advancedSearch"
    }

    @PostMapping("/advanced-search")
    String advancedSearch(Model model) {
        //TODO: Create a bookmark repository for searching with multiple parameters
        throw new RuntimeException("Method not implemented!")
    }
}
