package com.article.springboot;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

	@Autowired
	PagingAndSortingRepository articleRepository;
	
	@GetMapping("/articlecreate")
	public ResponseEntity<String> addArticle(@RequestParam(name = "title", required = true) String title,
			@RequestParam(name = "author", required = true) String author,
			@RequestParam(name = "content", required = true) String content,
			@RequestParam(name = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		try {
			articleRepository.save(new Article(title, author, content, date));
			return ResponseEntity.ok().body("Article added: " + title);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@GetMapping("/articlelist")
	public ResponseEntity<String> listArticle(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
		try {
			StringBuilder list = new StringBuilder();
			Pageable p = PageRequest.of(page != null ? page : 0, 10);
			articleRepository.findAll(p).forEach(a -> {
				list.append(String.format("%s\r\n", a.getTitle()));
			});
			return ResponseEntity.ok().body(list.toString());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@GetMapping("/articlecount")
	public ResponseEntity<String> countArticle() {
		try {
			int page = 0;
			long total = 0;
			boolean done = false;
			LocalDate d = LocalDate.now().minusWeeks(1);
			
			while (!done) {
				Pageable p = PageRequest.of(page, 10, Sort.by("date").descending());
				Page<Article> pa = articleRepository.findAll(p);
				long count = pa.stream().filter(a -> a.getDate().isAfter(d)).count();
				total += count;
				done = pa.isLast() || (count == 0);
				page++;
			}
			return ResponseEntity.ok().body(Long.toString(total));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
