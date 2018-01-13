package net.deckerego.docmag.controller

import net.deckerego.docmag.configuration.DocConfig
import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.repository.ScannedRepository
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
    LocalFileService localFileService

    @MockBean
    DocConfig docConfig

    @Test
    void unauthenticated() {
        this.mvc.perform(get("/tagging")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
    }

    @Test
    @WithMockUser
    void thumbnail() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where")
        result.metadata = ["Content-Type": "application/pdf"]

        def testFile = new File(System.getProperty("user.dir"),"src/test/docs/test.pdf")
        def testImage = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB)

        given(this.localFileService.fetchFile("/no/where")).willReturn(testFile)
        given(this.imageService.render(testFile, "application/pdf", 1.0)).willReturn(testImage)
        given(this.scannedRepository.findById("feedfacedeadbeef")).willReturn(result)
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))

        this.mvc.perform(get("/tagging/cover")
                .param("id", "feedfacedeadbeef")
                .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
    }
}

