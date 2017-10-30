package net.deckerego.docmag.controller

import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.repository.ScannedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/search")
class SearchController {
    @Autowired
    ScannedRepository repository

    @GetMapping
    def welcome(Model model) {
        def searchDoc = new ScannedDoc(content: "Search for...")
        model.addAttribute("query", searchDoc)
        "hello"
    }

    @PostMapping
    def search(Model model, @ModelAttribute ScannedDoc searchDoc) {
        Page<ScannedDoc> results =
                repository.findByContent(searchDoc.content, new PageRequest(0, 5))
        model.addAttribute("documents", results.content)
        model.addAttribute("query", searchDoc)
        "hello"
    }
}