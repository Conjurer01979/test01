package com.article.springboot;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class ArticleRepositoryTests {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PagingAndSortingRepository customers;
	
	private final String TITLE = "title";

	@Test
	public void testFindByLastName() {
		Article article = new Article(TITLE, "Author", "Content", LocalDate.now());
		entityManager.persist(article);
		Optional<Article> findArticle = customers.findById(1l);
		assertThat(findArticle.isPresent()).isEqualTo(true);
		assertThat(findArticle.get().getTitle()).isEqualTo(TITLE);
	}
}
