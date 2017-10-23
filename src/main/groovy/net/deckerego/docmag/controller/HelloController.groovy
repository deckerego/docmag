package net.deckerego.docmag.controller

import net.deckerego.docmag.repository.HelloRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/welcome")
class HelloController {

    @RequestMapping("/")
    def welcome() {
        def results = HelloRepository.findByName("Dakota")
        "hello"
    }

}