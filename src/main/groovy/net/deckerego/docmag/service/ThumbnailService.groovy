package net.deckerego.docmag.service

import net.deckerego.docmag.configuration.DocConfig
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

import javax.imageio.ImageIO
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
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
        } else if(type.contains(MediaType.IMAGE_JPEG_VALUE)
               || type.contains(MediaType.IMAGE_PNG_VALUE)
               || type.contains(MediaType.IMAGE_GIF_VALUE)) {
            image = renderImage(file, scale)
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

    private BufferedImage renderImage(File file, BigDecimal scale) {
        BigDecimal finalScale = scale / 2 // Double the reduction in scale. Because I said so.

        BufferedImage image = ImageIO.read(file)
        BufferedImage transformedImage = new BufferedImage(image.width * finalScale as int, image.height * finalScale as int, BufferedImage.TYPE_INT_ARGB)

        AffineTransform transform = new AffineTransform()
        transform.scale(finalScale, finalScale)
        AffineTransformOp scaleOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)
        scaleOp.filter(image, transformedImage)
    }
}
