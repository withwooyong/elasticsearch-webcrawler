package com.elasticsearchWebcrawlerG;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.elasticsearchWebcrawlerG.dto.MovieDTO;
import com.elasticsearchWebcrawlerG.repository.MovieRepository;
import com.elasticsearchWebcrawlerG.service.CrawlerMovieService;
import com.elasticsearchWebcrawlerG.service.ElasticsearchWebcrawlerGService;

/**
 * http://aoruqjfu.fun25.co.kr/index.php/post/792
 * @author user
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchWebcrawlerGApplicationTests {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	ElasticsearchWebcrawlerGService elasticsearchWebcrawlerGService;
	
	@Autowired
	CrawlerMovieService crawlerMovieService;
	
	// Poster, Actor, Director
	enum image_gubun {
		POSTER, ACTOR, DIRECTOR
	};
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void saveTest() throws Exception {
//		log.info("URL_BASIC={}", URL_BASIC);
//		log.info("URL_DETAIL={}", URL_DETAIL);
		
		MovieDTO movieDTO = new MovieDTO();
		String code = "70000"; // 70000 98438
//		movieDTO = crawlerMovie.getBasic(URL_BASIC, code, movieDTO);	// 주요정보
//		log.info("주요정보={}", movieDTO.toString());
//		movieDTO = crawlerMovie.getDetail(URL_DETAIL, code, movieDTO); // 배우/제작진
		crawlerMovieService.getMeta(code, movieDTO);
		log.info("배우/제작진={}", movieDTO.toString());
		
		try {
			movieDTO = movieRepository.save(movieDTO);
			log.info("movieDTO={}", movieDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//movieRepository.findAll().forEach(System.out::println);
		
		//movieRepository.findByNameEnLike("The Avengers*").forEach(System.out::println);
		//movieRepository.findByNameEnLike("The Book Of*").forEach(System.out::println);
	}
	
	@Test
	public void findByTest() throws Exception {
		
		try {
			String documentId = "123456";
			MovieDTO movieDTO = new MovieDTO();
			//movieDTO.setId(documentId);
			movieDTO = movieRepository.save(movieDTO);
			log.info("movieDTO={}", movieDTO);
			//movieRepository.findByNameEnLike("The Avengers*").forEach(System.out::println);
			//movieRepository.findByNameEnLike("The Book Of*").forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/*
	Method
	 * HTTP	CRUD
	 * Post	Create
	 * Get	Read
	 * Put	Update
	 * Delete	Delete
	*/
	
	@Test
	public void deleteTest() throws Exception {
		//### MovieDTO(id=AVg9vxQe7mx7HmWsR7LP, code=137971, nameKo=로스트 인 더스트, nameEn=Hell or High Water, 2016, ntzScore=8.54, spcScore=8.1, poster=POSTER_137971.jpg, genresName=[범죄,  드라마], countryName=미국, duration=103, releaseDate=20161103, story=빚더미에 시달리던 두 형제, 토비(크리스 파인)과 태너(벤 포스터).  가족의 유일한 재산이자, 어머니의 유산인 농장의 소유권 마저 은행 차압위기에 놓이게 된다.    절망적인 현실에서 가족을 지키기 위해 연쇄 은행 강도 계획을 꾸미는 형제.  동생을 위해서라면 어떠한 일도 서슴지 않는 전과자 출신의 형 태너와 차분하고 이성적인 성격의 동생 토비는 범죄에 성공한다.    한편, 연달아 발생한 은행강도 사건을 수사하던 베테랑 형사 해밀턴(제프 브리지스)은 치밀한 범죄 수법을 본능적으로 직감하고 수사망을 좁혀 그들을 추격하기 시작하는데..", actorsCode=[48203, 7314, 687, 111714, 60483, 137776, 131371, 186927, 172591, 349333, 51683, 215465, 29172, 380376, 337071], actorsKo=[크리스 파인, 벤 포스터, 제프 브리지스, 케이티 믹슨, 데일 딕키, 케빈 랜킨, 멜라니 페퍼리아, 엠버 미드썬더, 길 버밍햄, 로라 마르티네즈-커닝햄, 벅 테일러, 딜런 케닌, 그레고리 크루즈, 마틴 팔머, 테리 데일 파크스], actorsEn=[Chris Pine, Ben Foster, Jeff Bridges, Katy Mixon, Dale Dickey, Kevin Rankin, Melanie Papalia, Amber Midthunder, Gil Birmingham, Lora Martinez-Cunningham, Buck Taylor, Dylan Kenin, Gregory Cruz, Martin Palmer, Terry Dale Parks], actorsImg=[], directorCode=9600, directorKo=데이빗 맥킨지, directorEn=David MacKenzie, directorImg=null, grade=[국내] 15세 관람가 [해외] R 도움말, importer=(주)메인타이틀 픽쳐스, distributor=메가박스(주)플러스엠)
		// INFO  17:54:41.201 [main] com.elasticsearchWebcrawlerG.ElasticsearchWebcrawlerGApplicationTests[findAllTest:108] - ### MovieDTO(id=AVg97LVR7mx7HmWsR7LR, code=134842, nameKo=무한대를 본 남자, nameEn=The Man Who Knew Infinity, 2015, ntzScore=8.72, spcScore=6.45, poster=POSTER_134842.jpg, genresName=[드라마], countryName=영국, duration=108, releaseDate=20161103, story=머릿속에 그려지는 수많은 공식들을 세상 밖으로 펼치고 싶었던 인도 빈민가의 수학 천재 ‘라마누잔’.  그의 천재성을 알아본 영국 왕립학회의 괴짜 수학자 ‘하디 교수’는  엄격한 학교의 반대를 무릅쓰고 케임브리지 대학으로 ‘라마누잔’을 불러들인다.  성격도 가치관도 신앙심도 다르지만 수학에 대한 뜨거운 열정으로 함께한 두 사람은  모두가 불가능이라 여긴 위대한 공식을 세상에 증명하기 위해 무한대로의 여정을 떠나는데…    역사상 가장 지적인 브로맨스가 시작된다!, actorsCode=[159494, 703, 37333, 19770, 705, 8938, 62148, 384980, 15490, 366367, 352742, 101778, 347216, 255831, 366331, 393885], actorsKo=[데브 파텔, 제레미 아이언스, 토비 존스, 스티븐 프라이, 제레미 노덤, 케빈 맥널리, 엔조 실렌티, 데비카 비스, 리차드 존슨, 알렉산더 쿠퍼, 샤자드 라티프, 패드레익 들러니, 리처드 커닝햄, 로저 나라얀, 니콜라스 애그뉴, 산 쉘라], actorsEn=[Dev Patel, Jeremy Irons, Toby Jones, Stephen Fry, Jeremy Northam, Kevin McNally, Enzo Cilenti, Devika Bhise, Richard Johnson, Alexander Cooper, Shazad Latif, Padraic Delaney, Richard Cunningham, Roger Narayan, Nicholas Agnew, San Shella], actorsImg=[], directorCode=363879, directorKo=맷 브라운, directorEn=Matt Brown, directorImg=null, grade=[국내] 12세 관람가 [해외] PG-13 도움말, importer=판씨네마(주), distributor=판씨네마(주))
		//movieRepository.delete("AVg97LVR7mx7HmWsR7LR");
		//movieRepository.deleteAll();
		//movieRepository.findAll().forEach(System.out::println);
	}
	
	@Test
	public void findAllTest() throws Exception {
		Iterable<MovieDTO> movieDTOs = movieRepository.findAll();
		for (Iterator<MovieDTO> iterator = movieDTOs.iterator(); iterator.hasNext();) {
			MovieDTO movieDTO = (MovieDTO) iterator.next();
			log.info("### {}", movieDTO.toString());
		}
	}
	
	@Test
	public void findByNameKoLikeTest() throws Exception {
		List<MovieDTO> movieDTOs = movieRepository.findByNameKoLike("무한대를");
		
		for (MovieDTO movieDTO : movieDTOs) {
			log.info("### {}", movieDTO.toString());
		}
		
		// accountRepository.save(new Account("tving", "tving.com"));
		// accountRepository.findByname("tving").forEach(System.out::println);
		// accountRepository.findAll().forEach(System.out::println);
	}
	
	@Test
	public void findByNameKoTest() throws Exception {
		List<MovieDTO> movieDTOs = movieRepository.findByNameKo("어벤져스: 에이지 오브 울트론");
		
		for (MovieDTO movieDTO : movieDTOs) {
			log.info("### {}", movieDTO.toString());
		}
	}
	
	@Test
	public void findByCodeTest() throws Exception {
		List<MovieDTO> movieDTOs = movieRepository.findByCode("98438");
		
		for (MovieDTO movieDTO : movieDTOs) {
			log.info("### {}", movieDTO.toString());
		}
	}
	
	@Test
	public void deleteAll() throws Exception {
		movieRepository.deleteAll();
	}
	
	
	
	
}
