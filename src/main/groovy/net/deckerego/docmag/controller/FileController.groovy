package net.deckerego.docmag.controller

import net.deckerego.docmag.configuration.DocConfig
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.imageio.ImageIO
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils

import java.awt.image.BufferedImage

@RestController
@RequestMapping("/read")
class FileController {
    @Autowired
    DocConfig docConfig

    @GetMapping
    def fetch(HttpServletResponse response,
              @RequestParam(value="name", required=true) String name,
              @RequestParam(value="type", required=false, defaultValue="application/pdf") String type) {
        InputStream is = new FileInputStream(new File(docConfig.root, name))

        OutputStream os = response.getOutputStream()
        response.setContentType(type)
        IOUtils.copy(is, os)
        response.flushBuffer()
    }

    @GetMapping("/thumbnail")
    def thumbnail(HttpServletResponse response,
              @RequestParam(value="name", required=true) String name,
              @RequestParam(value="type", required=false, defaultValue="application/pdf") String type,
              @RequestParam(value="scale", required=false, defaultValue="0.5") BigDecimal scale) {

        if(type.contains("application/pdf")) {
            File file = new File(docConfig.root, name)
            PDDocument doc = PDDocument.load file
            PDFRenderer renderer = new PDFRenderer(doc)
            BufferedImage image = renderer.renderImage 0, scale
            image = image.getSubimage 0, 0, image.width, image.height / 2 as int
            doc.close()

            OutputStream os = response.getOutputStream()
            response.setContentType "image/png"
            ImageIO.write image, "PNG", os
            response.flushBuffer()
        } else {
            fetch response, name, type
        }
    }
}
