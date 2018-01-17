package net.deckerego.docmag

import net.deckerego.docmag.configuration.DocConfig
import net.deckerego.docmag.controller.SearchController
import net.deckerego.docmag.model.ScannedDoc
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

import java.text.DateFormat
import java.text.SimpleDateFormat

import static org.hamcrest.Matchers.*
import static org.mockito.BDDMockito.isNull
import static org.mockito.BDDMockito.given
import static org.mockito.BDDMockito.eq
import static org.mockito.BDDMockito.any
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
    void  unauthenticated() {
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
        given(this.repository.findByContent(eq("*"), any(Collection.class), any(), any(), any())).willReturn(this.results)
        given(this.repository.documentCount()).willReturn(10L)

        this.mvc.perform(get("/search")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().attribute("query", is("*")))
                .andExpect(model().attribute("tags", empty()))
                .andExpect(model().attribute("results", hasProperty("content")))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("totalDocs", is(10L)))
                .andExpect(model().attribute("currentPage", is(1)))
                .andExpect(model().attribute("startTime", notNullValue()))
                .andExpect(model().attribute("endTime", notNullValue()))
    }

    @Test
    @WithMockUser
    void defaultQuery() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where")

        given(this.docConfig.getPagesize()).willReturn(1)
        given(this.results.getContent()).willReturn([result])
        given(this.results.getTotalElements()).willReturn(1L)
        given(this.repository.findByContent(any(), any(Collection.class), any(), any(), any())).willReturn(this.results)
        given(this.repository.documentCount()).willReturn(10L)

        this.mvc.perform(get("/search")
                .param("query", "inputText")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(model().attribute("query", is("inputText")))
                .andExpect(model().attribute("tags", empty()))
                .andExpect(model().attribute("results", hasProperty("content")))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("totalDocs", is(10L)))
                .andExpect(model().attribute("currentPage", is(1)))
                .andExpect(model().attribute("startTime", notNullValue()))
                .andExpect(model().attribute("endTime", notNullValue()))
    }

    @Test
    @WithMockUser
    void fetchTag() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where")

        given(this.docConfig.getPagesize()).willReturn(1)
        given(this.results.getContent()).willReturn([result])
        given(this.results.getTotalElements()).willReturn(1L)
        given(this.repository.findByContent(any(), any(Collection.class), any(), any(), any())).willReturn(this.results)
        given(this.repository.documentCount()).willReturn(10L)

        this.mvc.perform(get("/search")
                .param("tag", "tagOne")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(model().attribute("query", is("*")))
                .andExpect(model().attribute("tags", contains("tagOne")))
                .andExpect(model().attribute("results", hasProperty("content")))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("totalDocs", is(10L)))
                .andExpect(model().attribute("currentPage", is(1)))
                .andExpect(model().attribute("startTime", notNullValue()))
                .andExpect(model().attribute("endTime", notNullValue()))
    }

    @Test
    @WithMockUser
    void fetchTags() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where")

        given(this.docConfig.getPagesize()).willReturn(1)
        given(this.results.getContent()).willReturn([result])
        given(this.results.getTotalElements()).willReturn(1L)
        given(this.repository.findByContent(any(), any(Collection.class), any(), any(), any())).willReturn(this.results)
        given(this.repository.documentCount()).willReturn(10L)

        this.mvc.perform(get("/search")
                .param("tag", "tagOne", "tagTwo")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(model().attribute("query", is("*")))
                .andExpect(model().attribute("tags", contains("tagOne", "tagTwo")))
                .andExpect(model().attribute("results", hasProperty("content")))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("totalDocs", is(10L)))
                .andExpect(model().attribute("currentPage", is(1)))
                .andExpect(model().attribute("startTime", notNullValue()))
                .andExpect(model().attribute("endTime", notNullValue()))
    }

    @Test
    @WithMockUser
    void paginatedQuery() {
        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where")

        given(this.docConfig.getPagesize()).willReturn(1)
        given(this.results.getContent()).willReturn([result])
        given(this.results.getTotalElements()).willReturn(2L)
        given(this.repository.findByContent(any(), any(Collection.class), any(), any(), any())).willReturn(this.results)
        given(this.repository.documentCount()).willReturn(10L)

        this.mvc.perform(get("/search")
                .param("query", "inputText")
                .param("page", "1")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(model().attribute("query", is("inputText")))
                .andExpect(model().attribute("tags", empty()))
                .andExpect(model().attribute("results", hasProperty("content")))
                .andExpect(model().attribute("totalPages", is(2)))
                .andExpect(model().attribute("totalDocs", is(10L)))
                .andExpect(model().attribute("currentPage", is(2)))
                .andExpect(model().attribute("startTime", notNullValue()))
                .andExpect(model().attribute("endTime", notNullValue()))
    }

    @Test
    @WithMockUser
    void startEndTime() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd")

        def result = new ScannedDoc(id: "feedfacedeadbeef", body: "nothing", lastModified: Calendar.instance.time, parentPath: "/no", fileName: "where")

        given(this.docConfig.getPagesize()).willReturn(1)
        given(this.results.getContent()).willReturn([result])
        given(this.results.getTotalElements()).willReturn(2L)
        given(this.repository.findByContent(eq("inputText"), any(Collection.class), eq(format.parse("2001-01-29")), eq(format.parse("2010-12-12")), any())).willReturn(this.results)
        given(this.repository.documentCount()).willReturn(10L)

        this.mvc.perform(get("/search")
                .param("query", "inputText")
                .param("page", "1")
                .param("startTime", "2001-01-29")
                .param("endTime", "2010-12-11")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(model().attribute("query", is("inputText")))
                .andExpect(model().attribute("tags", empty()))
                .andExpect(model().attribute("results", hasProperty("content")))
                .andExpect(model().attribute("totalPages", is(2)))
                .andExpect(model().attribute("totalDocs", is(10L)))
                .andExpect(model().attribute("currentPage", is(2)))
                .andExpect(model().attribute("startTime", is("2001-01-29")))
                .andExpect(model().attribute("endTime", is("2010-12-11")))
    }
}

