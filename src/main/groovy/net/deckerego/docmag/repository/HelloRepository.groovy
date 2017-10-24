package net.deckerego.docmag.repository

import net.deckerego.docmag.model.ScannedDoc
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
interface HelloRepository extends ElasticsearchRepository<ScannedDoc, String> {
    @Query("{\"match\" : {\"_all\" : \"?0\"}}")
    Page<ScannedDoc> findByName(String name, Pageable pageable)
}