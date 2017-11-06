package net.deckerego.docmag

import net.deckerego.docmag.configuration.DocConfig
import net.deckerego.docmag.controller.FileController
import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.repository.ScannedRepository
import net.deckerego.docmag.service.LocalFileService
import net.deckerego.docmag.service.ThumbnailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
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
    private ThumbnailService thumbSvc

    @MockBean
    private DocConfig docConfig

    @Test
    void fetch() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", content: "nothing")
        result.file = new ScannedDoc.File(lastModified: Calendar.instance.time)
        result.path = new ScannedDoc.Path(virtual: "/no/where")
        result.meta = new ScannedDoc.MetaData(format: "application/pdf;version=1.0")

        given(this.fileSvc.fetchFile("/no/where")).willReturn(new File("src/test/groovy/test.pdf"))
        given(this.repository.findById("feedfacedeadbeef")).willReturn(result)
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))

        this.mvc.perform(get("/read")
            .param("id", "feedfacedeadbeef")
            .accept(MediaType.APPLICATION_PDF))
            .andExpect(status().isOk())
    }

    @Test
    void thumbnail() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", content: "nothing")
        result.file = new ScannedDoc.File(lastModified: Calendar.instance.time)
        result.path = new ScannedDoc.Path(virtual: "/no/where")
        result.meta = new ScannedDoc.MetaData(format: "application/pdf;version=1.0")

        def testFile = new File(System.getProperty("user.dir"),"src/test/groovy/test.pdf")
        def testImage = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB)

        given(this.fileSvc.fetchFile("/no/where")).willReturn(testFile)
        given(this.thumbSvc.render(testFile, "application/pdf;version=1.0", 0.5)).willReturn(testImage)
        given(this.repository.findById("feedfacedeadbeef")).willReturn(result)
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))

        this.mvc.perform(get("/read/thumbnail")
                .param("id", "feedfacedeadbeef")
                .param("scale", "0.5")
                .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
    }
}
