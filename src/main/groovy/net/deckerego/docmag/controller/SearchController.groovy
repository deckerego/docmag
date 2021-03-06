package net.deckerego.docmag.controller

import net.deckerego.docmag.configuration.DocConfig
import net.deckerego.docmag.configuration.TaggingConfig
import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.repository.ScannedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/search")
class SearchController {
    @Autowired
    ScannedRepository repository

    @Autowired
    DocConfig docConfig

    @Autowired
    TaggingConfig taggingConfig

    @GetMapping
    def search(Model model,
               @RequestParam(name = "query", required = false, defaultValue = "*") String query,
               @RequestParam(name = "tag", required = false, defaultValue = "") Collection<String> tags,
               @RequestParam(name = "startTime", required = false)
                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
               @RequestParam(name = "endTime", required = false)
                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
               @RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber) {

        if(! startTime) startTime = Calendar.getInstance().time - 365
        if(! endTime) endTime = Calendar.getInstance().time

        PageRequest pageable = PageRequest.of(pageNumber, docConfig.pagesize, Sort.Direction.DESC, "_score", "lastModified")
        Page<ScannedDoc> results = repository.findByContent query, tags, startTime, endTime + 1, pageable

        results.content.each { doc ->
            if(doc.body) {
                int idx = doc.body.toLowerCase().indexOf(query.toLowerCase())
                int minlen = idx < 20 ? 0 : idx - 20
                int maxlen = doc.body.size() < idx + 430 ? doc.body.size() : idx + 430
                doc.body = doc.body.substring(minlen, maxlen)

                String matchToken = query.replaceAll("[^A-Za-z0-9]", "")
                if(matchToken?.trim())
                  doc.body = doc.body.replaceAll("(?i)"+matchToken, { "<mark>"+it+"</mark>" })
            }

            if(doc.tags)
                doc.tags.removeAll { it.score < taggingConfig.threshold }
        }

        int totalPages = results.totalElements <= 0 ? 1 : Math.ceil(results.totalElements / pageable.pageSize) as int

        model.addAttribute"results", results
        model.addAttribute"totalDocs", repository.documentCount()
        model.addAttribute"totalPages", totalPages > 20 ? 20 : totalPages
        model.addAttribute"currentPage", pageNumber + 1
        model.addAttribute"query", query
        model.addAttribute"tags", tags
        model.addAttribute"startTime", startTime.format("yyyy-MM-dd")
        model.addAttribute"endTime", endTime.format("yyyy-MM-dd")
        "search"
    }
}
