package net.deckerego.docmag.model

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Mapping
import org.springframework.data.elasticsearch.annotations.Setting

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@Document(indexName = "docidx", type = "fileentry")
@Mapping(mappingPath = "fileentry-mapping.json")
@Setting(settingPath = "docidx-settings.json")
class ScannedDoc {
    @Id
    String id
    Map metadata
    String parentPath
    String fileName
    Date lastModified
    BufferedImage thumbnail
    String body

    @JsonGetter("thumbnail")
    byte[] getThumbnailBytes() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
            ImageIO.write this.thumbnail, "PNG", outputStream
            return outputStream.toByteArray()
        } catch(IOException e) {
            return new byte[0]
        }
    }

    @JsonSetter("thumbnail")
    void setThumbnailBytes(byte[] image) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(image)
            this.thumbnail = ImageIO.read inputStream
        } catch(IOException e) {
            this.thumbnail = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
        }
    }
}
