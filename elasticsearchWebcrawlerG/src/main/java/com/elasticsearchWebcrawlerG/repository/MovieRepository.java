package com.elasticsearchWebcrawlerG.repository;


import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.elasticsearchWebcrawlerG.dto.MovieDTO;

/**
 * http://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.query-methods.finders
 * @author user
 *
 */
public interface MovieRepository extends ElasticsearchRepository<MovieDTO, Long> {

	// basic
	public List<MovieDTO> findByCode(String kwd); 
	public List<MovieDTO> findByNameKoLike(String kwd); // 한글명 어벤져스: 에이지 오브 울트론
	public List<MovieDTO> findByNameKo(String kwd); // 한글명 어벤져스: 에이지 오브 울트론
	public List<MovieDTO> findByNameEn(String kwd); // 영문명 The Avengers: Age of Ultron, 2015
	public List<MovieDTO> findByNameEnLike(String kwd); // 영문명 The Avengers: Age of Ultron, 2015
	
	// basic info_spec
	@SuppressWarnings("rawtypes")
	public List<MovieDTO> findByGenresNameIn(List kwd); // 액션, 모험, 판타지, SF	
	public List<MovieDTO> findByCountryName(String kwd); // 미국	
	//public List<MovieDTO> findByStory(String kwd); // 줄거리
	@SuppressWarnings("rawtypes")
	public List<MovieDTO> findByActorsKoIn(List kwd);
	@SuppressWarnings("rawtypes")
	public List<MovieDTO> findByActorsEnIn(List kwd);
	@SuppressWarnings("rawtypes")
	public List<MovieDTO> findByDirectorKoIn(List kwd);
	@SuppressWarnings("rawtypes")
	public List<MovieDTO> findByDirectorEnIn(List kwd);
	public List<MovieDTO> findByImporter(String kwd);
	public List<MovieDTO> findByDistributor(String kwd);
	
}
