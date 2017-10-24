package net.deckerego.docmag.controller

import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.repository.HelloRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/welcome")
class HelloController {
    @Autowired
    HelloRepository helloRepository

    @RequestMapping("/")
    def welcome(Model model) {
        Page<ScannedDoc> results = helloRepository.findByName("electric", new PageRequest(0, 5))
        model.addAttribute("documents", results.content)
        "hello"
    }
}