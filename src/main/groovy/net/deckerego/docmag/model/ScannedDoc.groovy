package net.deckerego.docmag.model

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import org.apache.commons.imaging.ImageFormat
import org.apache.commons.imaging.ImageReadException
import org.apache.commons.imaging.ImageWriteException
import org.apache.commons.imaging.Imaging
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

import java.awt.image.BufferedImage

@Document(indexName = "docidx", type = "fileentry")
class ScannedDoc {
    @Id
    String id
    Metadata metadata
    String parentPath
    String fileName
    Date lastModified
    BufferedImage thumbnail
    String body

    static class Metadata {
        @JsonProperty("Content-Type")
        String contentType
    }

    @JsonGetter("thumbnail")
    byte[] getThumbnailBytes() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
            Imaging.writeImage this.thumbnail, outputStream, ImageFormat.IMAGE_FORMAT_PNG, new HashMap<>()
            return outputStream.toByteArray()
        } catch(IOException e) {
            return new byte[0]
        } catch(ImageWriteException e) {
            return new byte[0]
        }
    }

    @JsonSetter("thumbnail")
    void setThumbnailBytes(byte[] image) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(image)
            this.thumbnail = Imaging.getBufferedImage inputStream
        } catch(IOException e) {
            this.thumbnail = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
        } catch(ImageReadException e) {
            this.thumbnail = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
        }
    }
}
