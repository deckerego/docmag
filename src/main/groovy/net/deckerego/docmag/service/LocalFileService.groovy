package net.deckerego.docmag.service

import net.deckerego.docmag.configuration.DocConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LocalFileService {
    @Autowired
    DocConfig docConfig

    File fetchFile(String relativeName) {
        //TODO Add more security constraints to file fetching
        if(relativeName.contains(".."))
            throw new SecurityException("Cannot load paths with backward traversals")
        new File(docConfig.root, relativeName)
    }
}
