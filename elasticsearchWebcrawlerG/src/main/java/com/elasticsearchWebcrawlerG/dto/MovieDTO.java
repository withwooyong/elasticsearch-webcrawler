package com.elasticsearchWebcrawlerG.dto;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * http://hyeonjae.github.io/elasticsearch/2015/06/29/elasticsearch.html
 * 데이터 구조
 * RDBMS	ElasticSearch
 * Database	Index
 * Table	Type
 * Row	Document
 * Column	Field
 * Scheme	Mapping
 * 
 * Method
 * HTTP	CRUD
 * Post	Create
 * Get	Read
 * Put	Update
 * Delete	Delete
 * 
 * Rest API
 * $ curl -XPUT http://localhost:9200/{index}/{type}/{document id}/ -d '{data}'
 * 
 * PUT을 이용하여 데이터 추가
 * $ curl -XPUT http://localhost:9200/books/book/1 -d '{  "title": "Elasticsearch Guide",  "author": "Kim",  "date": "2015-06-25",  "pages": 250}'
 * 
 * GET을 이용하여 데이터 조회
 * $ curl -XGET http://localhost:9200/books/book/1 
 * {"_index":"books","_type":"book","_id":"1","_version":1,"found":true,
 *   "_source":  {
 *   "title":"Elasticsearch Guide",
 *   "author":"Kim",
 *   "date":"2015-06-25",
 *   "pages":250,
 *   }
 * }
 * 
 * POST를 이용하여 데이터 입력 (document id를 생략하고 입력)
 * $ curl -XPOST http://localhost:9200/books/book/ -d '{  "title": "Elasticsearch Guide",  "author": "Kim",  "date": "2015-06-25",  "pages": 250}'
 * 
 * _source만 출력하기
 * $ curl -XGET http://localhost:9200/books/book/AU4eDMKk5_2R8Icukymj/_source?pretty=true
 * 
 * DELETE로 데이터 삭제하기
 * $ curl -XDELETE http://localhost:9200/books/book/AU4eDMKk5_2R8Icukymj
 */
@Document(indexName = "nmovie", type = "nmovie", shards = 1, replicas = 0, refreshInterval = "-1")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {	
	
	@Id
	private String id;
	
	// basic
	private String code;    				// 영화코드 code=98438
	
	//@Field(type=FieldType.String, index=FieldIndex.analyzed, indexAnalyzer="korean", searchAnalyzer="korean")
	private String nameKo;				    // 한글명 어벤져스: 에이지 오브 울트론
	
	private String nameEn; 					// 영문명 The Avengers: Age of Ultron, 2015
	private Double ntzScore;				// 관람객 평점
	private Double spcScore;				// 기자,평론가 평점
	private String poster;				    // 포스터
	
	// basic info_spec
	private List<String> genresName;		// 액션, 모험, 판타지, SF
	private String countryName;			// 미국
	private int duration;					// 상영시간 141분
	private Long releaseDate;				// 개봉일 2015.04.23 개봉
	private String story;					// 줄거리
	
	// detail
	private List<String> actorsCode;		// 출연 조스 웨던 code=1326
	private List<String> actorsKo;			// 출연 조스 웨던 code=1326	
	private List<String> actorsEn;			// 출연 조스 웨던 code=1326
	private List<String> actorsImg;		// 배우 이미지
	
	private String directorCode;			// 감독 조스 웨던 code=8222
	private String directorKo;				// 감독 조스 웨던 code=8222
	private String directorEn;				// 감독 조스 웨던 code=8222
	private String directorImg;			// 감독 이미지
	
	private String grade;					// 12세 관람가 grade=1001002
		
	private String importer; 				// 수입사
	private String distributor;				// 배급사
	
}
