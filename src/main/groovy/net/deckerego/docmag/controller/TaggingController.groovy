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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

import javax.imageio.ImageIO
import javax.servlet.http.HttpServletResponse
import java.awt.Image
import java.awt.image.BufferedImage

@Controller
@RequestMapping("/tags")
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
    def tagging(Model model) {
        Iterable<TagTemplate> templates = tagTemplateRepository.findAll()

        templates.each { it.count = scannedRepository.tagCount(it.name) }

        model.addAttribute"results", templates.sort { -it.count }
        "tags"
    }

    @GetMapping("/create")
    def createTag(Model model,
                @RequestParam(value="documentId", required=true) String documentId) {
        model.addAttribute"documentId", documentId
        model.addAttribute"name", ""
        model.addAttribute"xPos", 0
        model.addAttribute"yPos", 0
        model.addAttribute"width", 0
        model.addAttribute"height", 0

        "edittag"
    }

    @GetMapping("/edit")
    def editTag(Model model,
                @RequestParam(value="id", required=true) String id) {
        Optional<TagTemplate> result = tagTemplateRepository.findById id

        if(result) {
            TagTemplate tagTemplate = result.get()

            model.addAttribute "id", tagTemplate.id
            model.addAttribute "documentId", tagTemplate.sourceDocument.id
            model.addAttribute "name", tagTemplate.name
            model.addAttribute "xPos", tagTemplate.sourceDocument.xPos
            model.addAttribute "yPos", tagTemplate.sourceDocument.yPos
            model.addAttribute "width", tagTemplate.template.width
            model.addAttribute "height", tagTemplate.template.height
        }

        "edittag"
    }

    @GetMapping("/cover")
    def coverPage(HttpServletResponse response,
                @RequestParam(value="documentId", required=true) String documentId) {
        ScannedDoc scanDoc = scannedRepository.findById documentId
        File file = localFileService.fetchFile "${scanDoc.parentPath}/${scanDoc.fileName}"
        String contentType = scanDoc.metadata["Content-Type"]

        Image image = imageService.render(file, contentType, 1.0)

        OutputStream os = response.getOutputStream()
        response.setContentType MediaType.IMAGE_PNG_VALUE
        ImageIO.write image, "PNG", os
        response.flushBuffer()
    }

    @GetMapping("/template")
    def template(HttpServletResponse response,
                  @RequestParam(value="id", required=true) String id) {
        Optional<TagTemplate> result = tagTemplateRepository.findById id
        BufferedImage template = result.get().template

        OutputStream os = response.getOutputStream()
        response.setContentType MediaType.IMAGE_PNG_VALUE
        ImageIO.write template, "PNG", os
        response.flushBuffer()
    }

    @PostMapping("/save")
    def saveTagTemplate(Model model,
                        @RequestParam String id,
                        @RequestParam String documentId,
                        @RequestParam Integer xPos,
                        @RequestParam Integer yPos,
                        @RequestParam Integer width,
                        @RequestParam Integer height,
                        @RequestParam String name) {
        if(id.isEmpty()) id = null

        ScannedDoc scanDoc = scannedRepository.findById documentId
        File file = localFileService.fetchFile "${scanDoc.parentPath}/${scanDoc.fileName}"
        String contentType = scanDoc.metadata["Content-Type"]
        Image image = imageService.render(file, contentType, 1.0)

        TagTemplate tagTemplate = new TagTemplate(id: id, name: name, indexUpdated: Calendar.getInstance().getTime())
        tagTemplate.template = image.getSubimage xPos, yPos, width, height
        tagTemplate.sourceDocument = new TagTemplate.SourceDoc(id: documentId, xPos: xPos, yPos: yPos)

        tagTemplate = tagTemplateRepository.save(tagTemplate)

        model.addAttribute"id", tagTemplate.id
        model.addAttribute"documentId", tagTemplate.sourceDocument.id
        model.addAttribute"name", tagTemplate.name
        model.addAttribute"xPos", tagTemplate.sourceDocument.xPos
        model.addAttribute"yPos", tagTemplate.sourceDocument.yPos
        model.addAttribute"width", tagTemplate.template.width
        model.addAttribute"height", tagTemplate.template.height

        "edittag"
    }
}
