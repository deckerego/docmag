package net.deckerego.docmag

import net.deckerego.docmag.controller.SearchController
import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.model.ScannedDoc.File
import net.deckerego.docmag.model.ScannedDoc.Path
import net.deckerego.docmag.model.ScannedDoc.MetaData
import net.deckerego.docmag.repository.ScannedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

import static org.hamcrest.Matchers.hasItem
import static org.hamcrest.Matchers.hasProperty
import static org.hamcrest.Matchers.is
import static org.mockito.BDDMockito.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner)
@WebMvcTest(SearchController.class)
@AutoConfigureMockMvc
class SearchControllerTests {
    @Autowired
    private MockMvc mvc

    @MockBean
    private ScannedRepository repository

    @MockBean
    private Page<ScannedDoc> results

    @Test
    void welcome() {
        this.mvc.perform(get("/search")
            .accept(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk())
            .andExpect(model().attribute("query", hasProperty("content", is("Search for..."))))
    }

    @Test
    void search() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", content: "nothing")
        result.file = new File(lastModified: Calendar.instance.time)
        result.path = new Path(virtual: "/no/where")
        result.meta = new MetaData(format: "application/pdf")

        given(this.results.getContent()).willReturn([result])
        given(this.repository.findByContent(any(), any())).willReturn(this.results)

        this.mvc.perform(post("/search")
            .param("content", "inputText")
            .accept(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk())
            .andExpect(model().attribute("query", hasProperty("content", is("inputText"))))
            .andExpect(model().attribute("documents", hasItem(result)))
    }
}

