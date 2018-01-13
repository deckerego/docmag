package net.deckerego.docmag.service

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

import javax.imageio.ImageIO
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage

@Service
class ImageService {
    private final BufferedImage blankImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)

    BufferedImage render(File file, String type, BigDecimal scale) {
        if(! type) {
            blankImage
        } else if(type.contains(MediaType.APPLICATION_PDF_VALUE)) {
            renderPDF(file, scale)
        } else if(type.contains(MediaType.IMAGE_JPEG_VALUE)
                || type.contains(MediaType.IMAGE_PNG_VALUE)
                || type.contains(MediaType.IMAGE_GIF_VALUE)) {
            renderImage(file, scale)
        } else {
            blankImage
        }
    }

    private BufferedImage renderPDF(File file, BigDecimal scale) {
        PDDocument doc = PDDocument.load file
        PDFRenderer renderer = new PDFRenderer(doc)
        BufferedImage image = renderer.renderImage 0, scale
        doc.close()
        image
    }

    private BufferedImage renderImage(File file, BigDecimal scale) {
        BufferedImage image = ImageIO.read(file)
        BufferedImage transformedImage = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)

        AffineTransform transform = new AffineTransform()
        transform.scale(scale, scale)
        AffineTransformOp scaleOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)
        scaleOp.filter(image, transformedImage)
    }
}
