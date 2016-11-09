package com.elasticsearchWebcrawlerG.service;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.stereotype.Service;

import com.elasticsearchWebcrawlerG.dto.MovieDTO;
import com.elasticsearchWebcrawlerG.repository.MovieRepository;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.gson.Gson;

@Service
public class ElasticsearchWebcrawlerGService {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate; 
	
	@Autowired
	CrawlerMovieService crawlerMovieService;
	
	
	/**
	 * 메타수집
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public void crawlerDump() throws Exception {
		
		String s = System.getProperty("user.dir");
		try {
			BufferedReader in = new BufferedReader(new FileReader(s + "/bin/dummyData.txt"));
			String line;
			
			while ((line = in.readLine()) != null) {
				
				MovieDTO movieDTO = new MovieDTO();
				Iterable<String> result = Splitter.on('|').split(line);
				int i = 0;
				
				for (String string : result) {
					if (i == 0) {
						movieDTO.setCode(string);
					} else if (i == 1) {
						movieDTO.setNameKo(string);
					} else if (i == 2) {
						movieDTO.setNameEn(string);
					} else if (i == 3) {
						movieDTO.setNtzScore(Double.parseDouble((Strings.isNullOrEmpty(string) ? "0.0" : string)));
					} else if (i == 4) {
						movieDTO.setSpcScore(Double.parseDouble((Strings.isNullOrEmpty(string) ? "0.0" : string)));
					} else if (i == 5) {
						movieDTO.setPoster(string);
					} else if (i == 6) {
						List<String> genreNames = new ArrayList<>();
						Iterable<String> genreName = Splitter.on(',').split(string);
						for (String string2 : genreName) {
							genreNames.add(string2);
						}
						movieDTO.setGenresName(genreNames);
					} else if (i == 7) {
						movieDTO.setCountryName(string);
					} else if (i == 8) {
						movieDTO.setDuration(Integer.parseInt((Strings.isNullOrEmpty(string) ? "0" : string)));
					} else if (i == 9) {
						movieDTO.setReleaseDate(Long.parseLong((Strings.isNullOrEmpty(string) ? "20991231" : string)));
					} else if (i == 10) {
						movieDTO.setStory(string);
					} else if (i == 11) {
						List<String> actorsCode = new ArrayList<>();
						Iterable<String> actorCode = Splitter.on(',').split(string);
						for (String string2 : actorCode) {
							actorsCode.add(string2);
						}
						movieDTO.setActorsCode(actorsCode);
					} else if (i == 12) {
						List<String> actorsKo = new ArrayList<>();
						Iterable<String> actorKo = Splitter.on(',').split(string);
						for (String string2 : actorKo) {
							actorsKo.add(string2);
						}
						movieDTO.setActorsKo(actorsKo);								
					} else if (i == 13) {
						List<String> actorsEn = new ArrayList<>();
						Iterable<String> actorEn = Splitter.on(',').split(string);
						for (String string2 : actorEn) {
							actorsEn.add(string2);
						}
						movieDTO.setActorsEn(actorsEn);		
					} else if (i == 14) {
						List<String> actorsImg = new ArrayList<>();
						Iterable<String> actorImg = Splitter.on(',').split(string);
						for (String string2 : actorImg) {
							actorsImg.add(string2);
						}
						movieDTO.setActorsImg(actorsImg);
					} else if (i == 15) {
						movieDTO.setDirectorCode(string);
					} else if (i == 16) {
						movieDTO.setDirectorKo(string);
					} else if (i == 17) {
						movieDTO.setDirectorEn(string);
					} else if (i == 18) {
						movieDTO.setDirectorImg(string);
					} else if (i == 19) {
						movieDTO.setGrade(string);
					} else if (i == 20) {
						movieDTO.setImporter(string);		
					} else if (i == 21) {						
						movieDTO.setDistributor(string);
					}
					i++;
				}
				// 메타수집	
				IndexQuery indexQuery = new IndexQuery();
				indexQuery.setObject(movieDTO);
				String index = elasticsearchTemplate.index(indexQuery);
				log.info("index={} movieDTO={}", index, movieDTO);
			}		
			in.close();
	    } catch (IOException e) {
	        System.err.println(e); // 에러가 있다면 메시지 출력
	        System.exit(1);
	    }
	}
	
	/**
	 * 메타수집
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public MovieDTO crawlerMovie(String code) throws Exception {
		
		MovieDTO movieDTO = new MovieDTO();		
		if (!Strings.isNullOrEmpty(code)) {
			SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("code", code)).build();			
			List<MovieDTO> movieDTOs = elasticsearchTemplate.queryForList(searchQuery, MovieDTO.class);
			System.out.println("### " + movieDTOs.size());
			// 존재유무 체크
			if (movieDTOs.size() > 0) { // 업데이트 
				movieDTO = movieDTOs.get(0);
				IndexRequest indexRequest = new IndexRequest();
				indexRequest.source(new Gson().toJson(movieDTO));
				UpdateQuery updateQuery = new UpdateQueryBuilder().withId(movieDTO.getId()).withClass(MovieDTO.class).withIndexRequest(indexRequest).build();
				UpdateResponse update = elasticsearchTemplate.update(updateQuery);
				log.info("getId={} str={}", update.getId(), update.toString());
			} else { // 신규
				// 메타수집
				movieDTO = crawlerMovieService.getMeta(code, movieDTO);			
				IndexQuery indexQuery = new IndexQuery();
				indexQuery.setObject(movieDTO);
				String index = elasticsearchTemplate.index(indexQuery);
				log.info(index);
				log.info("index={} movieDTO={}", index, movieDTO);
			}
		}
		return movieDTO;
	}
	
	/**
	 * 전체리스트
	 * @param kwd
	 * @return
	 * @throws Exception
	 */
	public List<MovieDTO> list() throws Exception {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).withIndices("nmovie").withTypes("nmovie").build();
		List<MovieDTO> movieDTOs = elasticsearchTemplate.queryForList(searchQuery, MovieDTO.class);
		log.info("size={}", movieDTOs.size());
		return movieDTOs;
	}
	
	/**
	 * 검색
	 * @param kwd
	 * @return
	 * @throws Exception
	 */
	public List<MovieDTO> searchMovie(String kwd) throws Exception {
		log.info("kwd={}", kwd);
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryStringQuery("*" + kwd + "*")).build();
		List<MovieDTO> movieDTOs = elasticsearchTemplate.queryForList(searchQuery, MovieDTO.class);
		//String str = movieDTOs.stream().map(i -> i.toString()).collect(joining("\n"));
		//log.info("size={} str={}", movieDTOs.size(), str);
		return movieDTOs;
	}
		
	/**
	 * 업데이트
	 * @param movieDTO
	 * @return
	 */
	public UpdateResponse updateMovie(MovieDTO movieDTO) {
		IndexRequest indexRequest = new IndexRequest();
		//indexRequest.source("name", "wowowowo");
		indexRequest.source(movieDTO);
		UpdateQuery updateQuery = new UpdateQueryBuilder().withId(movieDTO.getId()).withClass(MovieDTO.class)
				.withIndexRequest(indexRequest).build();
		UpdateResponse update = elasticsearchTemplate.update(updateQuery);
		log.info("getId={} str={}", update.getId(), update.toString());
		return update;
	}
	
	/**
	 * 삭제
	 * @param id
	 * @return
	 */
	public String deleteMovie(String id) {
		String delete = elasticsearchTemplate.delete(MovieDTO.class, id);
		log.info(delete);
		return delete;
	}
}
