package net.deckerego.docmag.service

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import java.awt.image.BufferedImage

import static org.assertj.core.api.Assertions.assertThat

@RunWith(SpringRunner)
@SpringBootTest
class ThumbnailServiceTest {
    @Autowired
    private ThumbnailService thumbSvc

    @Test
    void pdf() {
        File file = new File(System.getProperty("user.dir"),"src/test/docs/test.pdf")
        BufferedImage image = thumbSvc.render(file, "application/pdf", 0.5)
        assertThat(image).isNotNull()
        assertThat(image.height).isGreaterThan(1)
        assertThat(image.width).isGreaterThan(1)
    }

    @Test
    void jpeg() {
        File file = new File(System.getProperty("user.dir"),"src/test/docs/test.jpg")
        BufferedImage image = thumbSvc.render(file, "image/jpeg", 0.5)
        assertThat(image).isNotNull()
        assertThat(image.height).isGreaterThan(1)
        assertThat(image.width).isGreaterThan(1)
    }

    @Test
    void gif() {
        File file = new File(System.getProperty("user.dir"),"src/test/docs/test.gif")
        BufferedImage image = thumbSvc.render(file, "image/gif", 0.5)
        assertThat(image).isNotNull()
        assertThat(image.height).isGreaterThan(1)
        assertThat(image.width).isGreaterThan(1)
    }

    @Test
    void png() {
        File file = new File(System.getProperty("user.dir"),"src/test/docs/test.png")
        BufferedImage image = thumbSvc.render(file, "image/png", 0.5)
        assertThat(image).isNotNull()
        assertThat(image.height).isGreaterThan(1)
        assertThat(image.width).isGreaterThan(1)
    }

    @Test
    void nothing() {
        File file = new File(System.getProperty("user.dir"),"src/test/docs/test.pdf")
        BufferedImage image = thumbSvc.render(file, "application/binary", 0.5)
        assertThat(image).isNotNull()
        assertThat(image.height).isEqualTo(1)
        assertThat(image.width).isEqualTo(1)
    }
}
