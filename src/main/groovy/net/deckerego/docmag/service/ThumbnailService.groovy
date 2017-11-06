package net.deckerego.docmag.service

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

import java.awt.image.BufferedImage

// Generating thumbnails until Tika generates their own & can be woven into fscrawler
// See https://issues.apache.org/jira/browse/TIKA-90
@Service
class ThumbnailService {

    BufferedImage render(File file, String type, BigDecimal scale) {
        BufferedImage image = null;

        if(type.contains(MediaType.APPLICATION_PDF_VALUE)) {
            PDDocument doc = PDDocument.load file
            PDFRenderer renderer = new PDFRenderer(doc)
            image = renderer.renderImage 0, scale
            doc.close()
        }

        image
    }
}
