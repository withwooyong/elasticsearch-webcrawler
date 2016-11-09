package com.elasticsearchWebcrawlerG;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import java.util.List;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import com.elasticsearchWebcrawlerG.dto.MovieDTO;

/**
 * http://aoruqjfu.fun25.co.kr/index.php/post/792 
 * @author user
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchTemplateTest {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Test
	public void getMovie() {
		GetQuery getQuery = new GetQuery();
		getQuery.setId("AVgo9SVsG6cGmxKyLcG-");
		MovieDTO movieDTO = elasticsearchTemplate.queryForObject(getQuery, MovieDTO.class);
		log.info("### {}", movieDTO.toString());
	}

	@Test
	public void getMovieTemplateIndexAndTypeTest() {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).withIndices("nmovie").withTypes("nmovie").build();
		List<MovieDTO> nmovies = elasticsearchTemplate.queryForList(searchQuery, MovieDTO.class);
		for (MovieDTO movieDTO : nmovies) {
			log.info("### {}", movieDTO.toString());
		}
	}

	@Test
	public void getMovieTemplateCountTest() {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).withIndices("nmovie").withTypes("nmovie").build();
		long count = elasticsearchTemplate.count(searchQuery, MovieDTO.class);
		log.info("### count={}", count);
	}

	@Test
	public void getMovieTemplateIndexAndTypeFieldsTest() {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).withIndices("nmovie").withTypes("nmovie").withFields("nameKo").build();
		List<MovieDTO> movieDTOs = elasticsearchTemplate.queryForList(searchQuery, MovieDTO.class);
		for (MovieDTO movieDTO : movieDTOs) {
			log.info("### {}", movieDTO.toString());
		}
	}

	@Test
	public void getMovieTemplatePage() {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("nmovie").withTypes("nmovie")
				.withQuery(matchAllQuery()).withSort(new FieldSortBuilder("nameKo").order(SortOrder.DESC))
				.withPageable(new PageRequest(0, 2)).build();
		Page<MovieDTO> nmovies = elasticsearchTemplate.queryForPage(searchQuery, MovieDTO.class);

		for (MovieDTO movieDTO : nmovies) {
			log.info("### {}", movieDTO.toString());
		}
		//String str = nmovies.getContent().stream().map(i -> i.toString()).collect(joining("\n"));
		//log.info(str);
		//log.info("### {}", nmovies.getContent().size());
	}

	@Test
	public void getMovieTemplateSearchTest() {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("code", "134842")).build();
		// SearchQuery searchQuery = new
		// NativeSearchQueryBuilder().withQuery(matchAllQuery())
		// .withFilter(boolQuery().filter(termQuery("name", "wonwoo"))).build();
		List<MovieDTO> nmovies = elasticsearchTemplate.queryForList(searchQuery, MovieDTO.class);
		for (MovieDTO movieDTO : nmovies) {
			log.info("### {}", movieDTO.toString());
		}
//		String str = nmovies.stream().map(i -> i.toString()).collect(joining("\n"));
//		log.info(str);
//		log.info("### {}", nmovies.size());
	}

	@Test
	public void getMovieTemplateSearchWildcardQueryTest() {
		//SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(wildcardQuery("nameEn", "*Avengers*")).build();
		//SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(wildcardQuery("nameEn", "*The Book Of*")).build();
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(wildcardQuery("nameKo", "*어벤져스*")).build();
		
		List<MovieDTO> nmovies = elasticsearchTemplate.queryForList(searchQuery, MovieDTO.class);
		for (MovieDTO movieDTO : nmovies) {
			log.info("### {}", movieDTO.toString());
		}
//		String str = nmovies.stream().map(i -> i.toString()).collect(joining("\n"));
//		log.info(str);
//		log.info("### {}", nmovies.size());
	}

	@Test
	public void getMovieTemplateSearchQueryStringQueryTest() {

		//SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryStringQuery("*Avengers*")).build();
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryStringQuery("*일라이*")).build();

		List<MovieDTO> nmovies = elasticsearchTemplate.queryForList(searchQuery, MovieDTO.class);
		for (MovieDTO movieDTO : nmovies) {
			log.info("### {}", movieDTO.toString());
		}
//		String str = movieDTOs.stream().map(i -> i.toString()).collect(joining("\n"));
//		log.info(str);
//		log.info("### {}", movieDTOs.size());
	}

	@Test
	public void saveTemplateTest() {
		// Account account = new Account("kk", "kk@test.com");
//		MovieDTO movieDTO = new MovieDTO();
//		IndexQuery indexQuery = new IndexQuery();
//		indexQuery.setObject(movieDTO);
//		String index = elasticsearchTemplate.index(indexQuery);
//		log.info(index);
	}

	@Test
	public void updateTemplateTest() {
//		IndexRequest indexRequest = new IndexRequest();
//		indexRequest.source("name", "wowowowo");
//		UpdateQuery updateQuery = new UpdateQueryBuilder().withId("AVSAOJvzW3yAedZaYx2w").withClass(MovieDTO.class)
//				.withIndexRequest(indexRequest).build();
//		UpdateResponse update = elasticsearchTemplate.update(updateQuery);
//		log.info(update);
	}

	@Test
	public void deleteTemplateTest() {
		// AVg9vxQe7mx7HmWsR7LP, code=137971, nameKo=로스트 인 더스트
		// AVg97LVR7mx7HmWsR7LR, code=134842, nameKo=무한대를 본 남자
		String delete = elasticsearchTemplate.delete(MovieDTO.class, "AVg-AV4j7mx7HmWsR7LT");
		
		log.info(delete);
	}
}
