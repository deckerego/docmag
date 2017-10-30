package net.deckerego.docmag.repository

import net.deckerego.docmag.model.ScannedDoc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository

interface ScannedRepository extends Repository<ScannedDoc, String> {
    @Autowired
    ElasticsearchOperations elasticsearchTemplate

    Page<ScannedDoc> findByContentContainingOrderByLastModifiedDesc(String name, Pageable pageable);
}