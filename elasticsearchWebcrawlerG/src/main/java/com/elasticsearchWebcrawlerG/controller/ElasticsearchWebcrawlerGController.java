package com.elasticsearchWebcrawlerG.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.elasticsearchWebcrawlerG.dto.MovieDTO;
import com.elasticsearchWebcrawlerG.service.ElasticsearchWebcrawlerGService;

/**
 * http://bakyeono.net/post/2016-06-03-start-elasticsearch.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/full-text-queries.html#full-text-queries
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html
 * http://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.query-methods.finders
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/array.html
 * http://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#repositories.query-methods.query-creation
 * http://hyeonjae.github.io/elasticsearch/2015/06/29/elasticsearch.html
 * List<String> items = Arrays.asList(str.split("\\s*,\\s*"));
 * 1. elasticsearch 설치
 * 2. 은전한닢(Mecab) 한글 형태소 분석기 설치
 * 3. 인덱스, 타입준비(스키마 설정)
 *    인덱스 설정
 *    curl -XPUT "http://119.149.188.226:9200/seunjeon-idx/?pretty" -d '{"settings" : {"index":{"analysis":{"analyzer":{"korean":{"type":"custom","tokenizer":"seunjeon_default_tokenizer"}},"tokenizer": {"seunjeon_default_tokenizer": {"type": "seunjeon_tokenizer","user_words": ["낄끼빠빠,-100", "버카충", "abc마트"]}}}}}}'
 *    타입매핑
 *    인덱스 생성(인덱스 설정 + 타입매핑) 
 * @author user
 *
 */
@Controller
@RequestMapping("esearch")
public class ElasticsearchWebcrawlerGController extends WebMvcConfigurerAdapter {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ElasticsearchWebcrawlerGService elasticsearchWebcrawlerGService;
	
	/**
	 * 샘플
	 * @param model
	 * @return
	 */
	@RequestMapping(value="template", method = RequestMethod.GET)
	public String template(Model model) {
		return "esearch/template";
	}
	
	/**
     * 리스트
     * @param model
     * @return
     * @throws Exception 
     */
	@RequestMapping(value = "list", method = RequestMethod.GET)
    String list(Model model) throws Exception {
        List<MovieDTO> movieDTOs = new ArrayList<>();
		movieDTOs = elasticsearchWebcrawlerGService.list();
		model.addAttribute("movieDTOs", movieDTOs);
        return "esearch/list";
    }
	
	/**
	 * crawlerDump
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value="crawlerDump", method = RequestMethod.GET)
	public void crawlerDump(Model model) throws Exception {		
		elasticsearchWebcrawlerGService.crawlerDump();
	}
	
	/**
	 * 수집
	 * @param model
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="crawlerMovie", method = RequestMethod.GET)
	public String crawlerMovie(Model model, @RequestParam("code") String code) throws Exception {
		log.info("code={}", code);
		elasticsearchWebcrawlerGService.crawlerMovie(code);		
		return "redirect:/esearch/list";
	}
	
	/**
	 * 검색
	 * @param model
	 * @param kwd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="searchMovie", method = RequestMethod.GET)
	public String searchMovie(Model model, @RequestParam("kwd") String kwd) throws Exception {
		log.info("kwd={}", kwd);
		List<MovieDTO> movieDTOs = new ArrayList<>();
		movieDTOs = elasticsearchWebcrawlerGService.searchMovie(kwd);
		model.addAttribute("movieDTOs", movieDTOs);
		return "esearch/list";
	}
    
	/**
	 * 업데이트
	 * @param form
	 * @param result
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value="update", method = RequestMethod.POST)
	public String updateMovie(@Validated MovieDTO form, BindingResult result, Model model) throws Exception {
		if (result.hasErrors()) {
            return list(model);
        }
		MovieDTO movieDTO = new MovieDTO();
        BeanUtils.copyProperties(form, movieDTO);
        elasticsearchWebcrawlerGService.updateMovie(movieDTO);        
        return "redirect:/esearch/list";
	}
    
    /**
     * 삭제
     * @param model
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    String delete(Model model, @PathVariable String id) throws Exception {
    	elasticsearchWebcrawlerGService.deleteMovie(id);
        return "redirect:/esearch/list";
    }    
}
