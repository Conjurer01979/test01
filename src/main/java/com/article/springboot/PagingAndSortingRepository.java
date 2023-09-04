package com.article.springboot;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagingAndSortingRepository extends CrudRepository<Article, Long> {
	Page<Article> findAll(Pageable pageable);
}
