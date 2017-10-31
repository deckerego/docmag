package net.deckerego.docmag.controller

import net.deckerego.docmag.configuration.DocConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils

@RestController
@RequestMapping("/read")
class FileController {
    @Autowired
    DocConfig docConfig

    @GetMapping
    def fetch(@RequestParam(value="name", required=true) String name,
              @RequestParam(value="type", required=false, defaultValue="application/pdf") String type,
              HttpServletResponse response) {
        InputStream is = new FileInputStream(new File(docConfig.root, name))
        OutputStream os = response.getOutputStream()

        response.setContentType(type)
        IOUtils.copy(is, os)
        response.flushBuffer()
    }
}
