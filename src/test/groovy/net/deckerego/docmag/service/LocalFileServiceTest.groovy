package net.deckerego.docmag.service

import net.deckerego.docmag.configuration.DocConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner

import static org.mockito.BDDMockito.given
import static org.assertj.core.api.Assertions.*

@RunWith(SpringRunner)
@SpringBootTest
class LocalFileServiceTest {

    @MockBean
    private DocConfig docConfig

    @Autowired
    private LocalFileService fileSvc

    @Test
    void fetch() {
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))
        File file = fileSvc.fetchFile("src/test/docs/test.pdf")
        assertThat(file).isNotNull()
    }

    @Test(expected = SecurityException.class)
    void backwardTraversal() {
        given(this.docConfig.getRoot()).willReturn(System.getProperty("user.dir"))
        fileSvc.fetchFile("../../etc/passwd")
    }
}
