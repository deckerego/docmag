package net.deckerego.docmag

import net.deckerego.docmag.configuration.DocConfig
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
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

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
    private DocConfig docConfig

    @MockBean
    private Page<ScannedDoc> results

    @Test
    void unauthenticated() {
        this.mvc.perform(get("/search")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
    }

    @Test
    @WithMockUser
    void welcome() {
        given(this.docConfig.getPagesize()).willReturn(1)
        given(this.results.getContent()).willReturn([])
        given(this.results.getTotalElements()).willReturn(0L)
        given(this.repository.findByContent(any(), any())).willReturn(this.results)

        this.mvc.perform(get("/search")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().attribute("query", is("")))
                .andExpect(model().attribute("results", hasProperty("content")))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("currentPage", is(1)))
    }

    @Test
    @WithMockUser
    void defaultSearch() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", content: "nothing")
        result.file = new File(lastModified: Calendar.instance.time)
        result.path = new Path(virtual: "/no/where")
        result.meta = new MetaData(format: "application/pdf")

        given(this.docConfig.getPagesize()).willReturn(1)
        given(this.results.getContent()).willReturn([result])
        given(this.results.getTotalElements()).willReturn(1L)
        given(this.repository.findByContent(any(), any())).willReturn(this.results)

        this.mvc.perform(get("/search")
                .param("query", "inputText")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(model().attribute("query", is("inputText")))
                .andExpect(model().attribute("results", hasProperty("content")))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("currentPage", is(1)))
    }

    @Test
    @WithMockUser
    void paginatedSearch() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", content: "nothing")
        result.file = new File(lastModified: Calendar.instance.time)
        result.path = new Path(virtual: "/no/where")
        result.meta = new MetaData(format: "application/pdf")

        given(this.docConfig.getPagesize()).willReturn(1)
        given(this.results.getContent()).willReturn([result])
        given(this.results.getTotalElements()).willReturn(2L)
        given(this.repository.findByContent(any(), any())).willReturn(this.results)

        this.mvc.perform(get("/search")
                .param("query", "inputText")
                .param("page", "1")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(model().attribute("query", is("inputText")))
                .andExpect(model().attribute("results", hasProperty("content")))
                .andExpect(model().attribute("totalPages", is(2)))
                .andExpect(model().attribute("currentPage", is(2)))
    }
}

