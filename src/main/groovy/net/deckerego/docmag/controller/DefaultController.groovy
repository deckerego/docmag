package net.deckerego.docmag.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class DefaultController {

    @GetMapping
    def index() {
        "redirect:/search"
    }
}