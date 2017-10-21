package net.deckerego.docmag

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/welcome")
class HelloController {

    @RequestMapping("/")
    def welcome() {
        "hello"
    }

}