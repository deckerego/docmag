package net.deckerego.docmag.model

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonSetter
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Mapping
import org.springframework.data.elasticsearch.annotations.Setting

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@Document(indexName = "docidx", type = "tagtemplate")
@Mapping(mappingPath = "tagtemplate-mapping.json")
@Setting(settingPath = "docidx-settings.json")
class TagTemplate {
    @Id
    String id
    String name
    BufferedImage template
    Date indexUpdated
    SourceDoc sourceDocument

    @JsonGetter("template")
    byte[] getThumbnailBytes() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
            ImageIO.write this.template, "PNG", outputStream
            return outputStream.toByteArray()
        } catch(IOException e) {
            return new byte[0]
        }
    }

    @JsonSetter("template")
    void setThumbnailBytes(byte[] image) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(image)
            this.template = ImageIO.read inputStream
        } catch(IOException e) {
            this.template = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
        }
    }

    static class SourceDoc {
        String id
        Integer yPos
        Integer xPos
    }
}
