package net.deckerego.docmag.controller

import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.model.TagTemplate
import net.deckerego.docmag.repository.ScannedRepository
import net.deckerego.docmag.repository.TagTemplateRepository
import net.deckerego.docmag.service.ImageService
import net.deckerego.docmag.service.LocalFileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

import javax.imageio.ImageIO
import javax.servlet.http.HttpServletResponse
import java.awt.Image

@Controller
@RequestMapping("/tagging")
class TaggingController {

    @Autowired
    ImageService imageService

    @Autowired
    ScannedRepository scannedRepository

    @Autowired
    TagTemplateRepository tagTemplateRepository

    @Autowired
    LocalFileService localFileService

    @GetMapping
    def tagging(Model model,
        @RequestParam(value="id", required=true) String id) {
        model.addAttribute"id", id
        model.addAttribute"name", ""
        model.addAttribute"xPos", 0
        model.addAttribute"yPos", 0
        model.addAttribute"width", 0
        model.addAttribute"height", 0

        "tagging"
    }

    @GetMapping("/cover")
    def coverPage(HttpServletResponse response,
                @RequestParam(value="id", required=true) String id) {
        ScannedDoc scanDoc = scannedRepository.findById id
        File file = localFileService.fetchFile "${scanDoc.parentPath}/${scanDoc.fileName}"
        String contentType = scanDoc.metadata["Content-Type"]

        Image image = imageService.render(file, contentType, 1.0)

        OutputStream os = response.getOutputStream()
        response.setContentType MediaType.IMAGE_PNG_VALUE
        ImageIO.write image, "PNG", os
        response.flushBuffer()
    }

    @PostMapping("/save")
    def saveTagTemplate(Model model,
                        @RequestParam String id,
                        @RequestParam Integer xPos,
                        @RequestParam Integer yPos,
                        @RequestParam Integer width,
                        @RequestParam Integer height,
                        @RequestParam String name) {
        ScannedDoc scanDoc = scannedRepository.findById id
        File file = localFileService.fetchFile "${scanDoc.parentPath}/${scanDoc.fileName}"
        String contentType = scanDoc.metadata["Content-Type"]
        Image image = imageService.render(file, contentType, 1.0)

        TagTemplate tagTemplate = new TagTemplate(name: name)
        tagTemplate.template = image.getSubimage xPos, yPos, width, height
        tagTemplate.sourceDocument = new TagTemplate.SourceDoc(id: id, xPos: xPos, yPos: yPos)

        tagTemplate = tagTemplateRepository.save(tagTemplate)

        model.addAttribute"id", tagTemplate.sourceDocument.id
        model.addAttribute"name", tagTemplate.name
        model.addAttribute"xPos", tagTemplate.sourceDocument.xPos
        model.addAttribute"yPos", tagTemplate.sourceDocument.yPos
        model.addAttribute"width", width
        model.addAttribute"height", height

        "tagging"
    }
}
