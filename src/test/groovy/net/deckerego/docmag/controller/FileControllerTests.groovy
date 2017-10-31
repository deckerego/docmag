package net.deckerego.docmag

import net.deckerego.docmag.configuration.DocConfig
import net.deckerego.docmag.controller.FileController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

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
    private DocConfig docConfig

    @Test
    void fetch() {
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))

        this.mvc.perform(get("/read")
            .param("name", "README.md")
            .param("type", "text/plain")
            .accept(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk())
    }
}

