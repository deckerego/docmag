package net.deckerego.docmag.controller

import net.deckerego.docmag.configuration.DocConfig
import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.model.TagTemplate
import net.deckerego.docmag.repository.ScannedRepository
import net.deckerego.docmag.repository.TagTemplateRepository
import net.deckerego.docmag.service.ImageService
import net.deckerego.docmag.service.LocalFileService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

import java.awt.image.BufferedImage

import static org.mockito.BDDMockito.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*

@RunWith(SpringRunner)
@WebMvcTest(TaggingController.class)
@AutoConfigureMockMvc
class TaggingControllerTests {
    @Autowired
    private MockMvc mvc

    @MockBean
    ImageService imageService

    @MockBean
    ScannedRepository scannedRepository

    @MockBean
    TagTemplateRepository tagTemplateRepository

    @MockBean
    LocalFileService localFileService

    @MockBean
    DocConfig docConfig

    @Test
    void unauthenticated() {
        this.mvc.perform(get("/tags")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
    }

    @Test
    @WithMockUser
    void coverPage() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where")
        result.metadata = ["Content-Type": "application/pdf"]

        def testFile = new File(System.getProperty("user.dir"),"src/test/docs/test.pdf")
        def testImage = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB)

        given(this.localFileService.fetchFile("/no/where")).willReturn(testFile)
        given(this.imageService.render(testFile, "application/pdf", 1.0)).willReturn(testImage)
        given(this.scannedRepository.findById("feedfacedeadbeef")).willReturn(result)
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))

        this.mvc.perform(get("/tags/cover")
                .param("documentId", "feedfacedeadbeef")
                .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
    }

    @Test
    @WithMockUser
    void template() {
        def testImage = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB)

        TagTemplate savedTemplate = new TagTemplate(name: "test")
        savedTemplate.template = testImage
        savedTemplate.sourceDocument = new TagTemplate.SourceDoc(id: "feedfacedeadbeef", xPos: 10, yPos: 10)

        given(this.tagTemplateRepository.findById("feedfacedeadbeef")).willReturn(new Optional<>(savedTemplate))

        this.mvc.perform(get("/tags/template")
                .param("id", "feedfacedeadbeef")
                .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
    }

    @Test
    @WithMockUser
    void edit() {
        def testImage = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB)

        TagTemplate savedTemplate = new TagTemplate(name: "test")
        savedTemplate.template = testImage
        savedTemplate.sourceDocument = new TagTemplate.SourceDoc(id: "feedfacedeadbeef", xPos: 10, yPos: 10)

        given(this.tagTemplateRepository.findById("feedfacedeadbeef")).willReturn(new Optional<>(savedTemplate))

        this.mvc.perform(get("/tags/edit")
                .with(csrf())
                .param("id", "feedfacedeadbeef")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
    }

    @Test
    @WithMockUser
    void create() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where")
        result.metadata = ["Content-Type": "application/pdf"]

        def testFile = new File(System.getProperty("user.dir"),"src/test/docs/test.pdf")
        def testImage = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB)

        TagTemplate savedTemplate = new TagTemplate(name: "test")
        savedTemplate.template = testImage
        savedTemplate.sourceDocument = new TagTemplate.SourceDoc(id: "feedfacedeadbeef", xPos: 10, yPos: 10)

        given(this.localFileService.fetchFile("/no/where")).willReturn(testFile)
        given(this.imageService.render(testFile, "application/pdf", 1.0)).willReturn(testImage)
        given(this.scannedRepository.findById("feedfacedeadbeef")).willReturn(result)
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))

        this.mvc.perform(get("/tags/create")
                .with(csrf())
                .param("documentId", "feedfacedeadbeef")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
    }

    @Test
    @WithMockUser
    void save() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where")
        result.metadata = ["Content-Type": "application/pdf"]

        def testFile = new File(System.getProperty("user.dir"),"src/test/docs/test.pdf")
        def testImage = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB)

        TagTemplate savedTemplate = new TagTemplate(name: "test")
        savedTemplate.template = testImage
        savedTemplate.sourceDocument = new TagTemplate.SourceDoc(id: "feedfacedeadbeef", xPos: 10, yPos: 10)

        given(this.localFileService.fetchFile("/no/where")).willReturn(testFile)
        given(this.imageService.render(testFile, "application/pdf", 1.0)).willReturn(testImage)
        given(this.scannedRepository.findById("feedfacedeadbeef")).willReturn(result)
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))
        given(this.tagTemplateRepository.save(any(TagTemplate.class))).willReturn(savedTemplate)

        this.mvc.perform(post("/tags/save")
                .with(csrf())
                .param("id", "feedfacedeadbeef")
                .param("documentId", "feedfacedeadbeef")
                .param("name", "test")
                .param("xPos", "10")
                .param("yPos", "10")
                .param("width", "100")
                .param("height", "20")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
    }
}

