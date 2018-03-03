package net.deckerego.docmag.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class PropertyControllerAdvice {

    @Value("\${app.version}")
    private String applicationVersion

    @ModelAttribute("applicationVersion")
    String getApplicationVersion() {
        return applicationVersion
    }
}