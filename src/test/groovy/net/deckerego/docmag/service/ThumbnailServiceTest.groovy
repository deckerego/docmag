package net.deckerego.docmag.service

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import java.awt.image.BufferedImage

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.BDDMockito.given

@RunWith(SpringRunner)
@SpringBootTest
class ThumbnailServiceTest {
    @Autowired
    private ThumbnailService thumbSvc

    @Test
    void pdf() {
        File file = new File(System.getProperty("user.dir"),"src/test/groovy/test.pdf")
        BufferedImage image = thumbSvc.render(file, "application/pdf;version=1.0", 0.5)
        assertThat(image).isNotNull()
    }

    @Test
    void nothing() {
        File file = new File(System.getProperty("user.dir"),"src/test/groovy/test.pdf")
        BufferedImage image = thumbSvc.render(file, "application/binary", 0.5)
        assertThat(image).isNull()
    }
}
