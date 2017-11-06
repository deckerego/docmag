package net.deckerego.docmag.controller

import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.repository.ScannedRepository
import net.deckerego.docmag.service.LocalFileService
import net.deckerego.docmag.service.ThumbnailService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
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
    ScannedRepository repository

    @Autowired
    LocalFileService fileSvc

    @Autowired
    ThumbnailService thumbSvc

    @GetMapping
    def fetch(HttpServletResponse response,
              @RequestParam(value="id", required=true) String id) {
        ScannedDoc scanDoc = repository.findById id

        InputStream is = new FileInputStream(fileSvc.fetchFile(scanDoc.path.virtual))

        OutputStream os = response.getOutputStream()
        response.setContentType scanDoc.meta.format
        IOUtils.copy is, os
        response.flushBuffer()
    }

    @GetMapping("/thumbnail")
    def thumbnail(HttpServletResponse response,
              @RequestParam(value="id", required=true) String id,
              @RequestParam(value="scale", required=false, defaultValue="0.5") BigDecimal scale) {
        ScannedDoc scanDoc = repository.findById id
        File file = fileSvc.fetchFile scanDoc.path.virtual

        BufferedImage image = thumbSvc.render(file, scanDoc.meta.format, scale)
        image = image.getSubimage 0, 0, image.width, image.height / 2 as int

        OutputStream os = response.getOutputStream()
        response.setContentType MediaType.IMAGE_PNG_VALUE
        ImageIO.write image, "PNG", os
        response.flushBuffer()
    }
}