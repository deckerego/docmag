package net.deckerego.docmag.controller

import net.deckerego.docmag.configuration.DocConfig
import net.deckerego.docmag.model.ScannedDoc
import net.deckerego.docmag.repository.ScannedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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

    @GetMapping
    def search(Model model,
               @RequestParam(name = "query", required = false, defaultValue="") String query,
               @RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber) {
        PageRequest pageable = PageRequest.of(pageNumber, docConfig.pagesize, Sort.Direction.DESC, "_score", "file.last_modified")
        Page<ScannedDoc> results = repository.findByContent query, pageable

        results.content.each { doc ->
            int idx = doc.content.indexOf(query)
            int minlen = idx < 50 ? 0 : idx - 50
            int maxlen = doc.content.size() < idx + 400 ? doc.content.size() : idx + 400
            doc.content = doc.content.substring(minlen, maxlen)
        }

        int totalPages = results.totalElements <= 0 ? 1 : Math.ceil(results.totalElements / pageable.pageSize) as int

        model.addAttribute"results", results
        model.addAttribute"totalDocs", repository.documentCount()
        model.addAttribute"totalPages", totalPages > 20 ? 20 : totalPages
        model.addAttribute"currentPage", pageNumber + 1
        model.addAttribute"query", query
        "search"
    }
}