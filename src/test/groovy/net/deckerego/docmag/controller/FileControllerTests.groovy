package net.deckerego.docmag

import net.deckerego.docmag.configuration.DocConfig
import net.deckerego.docmag.controller.FileController
import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.repository.ScannedRepository
import net.deckerego.docmag.service.LocalFileService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.junit.Test
import org.junit.runner.RunWith
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
@WebMvcTest(FileController.class)
@AutoConfigureMockMvc
class FileControllerTests {
    @Autowired
    private MockMvc mvc

    @MockBean
    private ScannedRepository repository

    @MockBean
    private LocalFileService fileSvc

    @MockBean
    private DocConfig docConfig

    @Test
    void unauthenticated() {
        this.mvc.perform(get("/read")
                .param("id", "feedfacedeadbeef")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
    }

    @Test
    @WithMockUser
    void fetch() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where")
        result.metadata = ["Content-Type": "application/pdf"]

        given(this.fileSvc.fetchFile("/no/where")).willReturn(new File("src/test/docs/test.pdf"))
        given(this.repository.findById("feedfacedeadbeef")).willReturn(result)
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))

        this.mvc.perform(get("/read")
            .param("id", "feedfacedeadbeef")
            .accept(MediaType.APPLICATION_PDF))
            .andExpect(status().isOk())
    }

    @Test
    @WithMockUser
    void thumbnail() {
        def testFile = new File(System.getProperty("user.dir"),"src/test/docs/test.pdf")
        def testImage = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB)

        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where", thumbnail: testImage)
        result.metadata = ["Content-Type": "application/pdf"]

        given(this.fileSvc.fetchFile("/no/where")).willReturn(testFile)
        given(this.repository.findById("feedfacedeadbeef")).willReturn(result)
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))

        this.mvc.perform(get("/read/thumbnail")
                .param("id", "feedfacedeadbeef")
                .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
    }
}

