package net.deckerego.docmag.controller

import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.repository.ScannedRepository
import net.deckerego.docmag.service.LocalFileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.imageio.ImageIO
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils

import java.awt.Image

@RestController
@RequestMapping("/read")
class FileController {

    @Autowired
    ScannedRepository repository

    @Autowired
    LocalFileService fileSvc

    @GetMapping
    def fetch(HttpServletResponse response,
              @RequestParam(value="id", required=true) String id) {
        ScannedDoc scanDoc = repository.findById id

        InputStream is = new FileInputStream(fileSvc.fetchFile("${scanDoc.parentPath}/${scanDoc.fileName}"))

        OutputStream os = response.getOutputStream()
        response.setContentType scanDoc.metadata["Content-Type"]
        IOUtils.copy is, os
        response.flushBuffer()
    }

    @GetMapping("/thumbnail")
    def thumbnail(HttpServletResponse response,
              @RequestParam(value="id", required=true) String id) {
        ScannedDoc scanDoc = repository.findById id

        OutputStream os = response.getOutputStream()
        response.setContentType MediaType.IMAGE_PNG_VALUE
        ImageIO.write scanDoc.thumbnail, "PNG", os
        response.flushBuffer()
    }
}
