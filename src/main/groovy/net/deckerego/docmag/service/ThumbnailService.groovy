package net.deckerego.docmag.service

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

import java.awt.image.BufferedImage

// Generating thumbnails until Tika generates their own & can be woven into fscrawler
// See https://issues.apache.org/jira/browse/TIKA-90
@Service
class ThumbnailService {
    private final BufferedImage blankImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)

    @Autowired
    CacheManager cacheManager

    @Cacheable("renderedThumbnails")
    BufferedImage render(File file, String type, BigDecimal scale) {
        BufferedImage image

        if(! type) {
            image = blankImage
        } else if(type.contains(MediaType.APPLICATION_PDF_VALUE)) {
            image = renderPDF(file, scale)
        } else {
            image = blankImage
        }

        if(image != null && image.height >= 32)
            image.getSubimage 0, 0, image.width, image.height / 2 as int
        else
            image
    }

    private BufferedImage renderPDF(File file, BigDecimal scale) {
        PDDocument doc = PDDocument.load file
        PDFRenderer renderer = new PDFRenderer(doc)
        BufferedImage image = renderer.renderImage 0, scale
        doc.close()
        image
    }
}
