package com.elasticsearchWebcrawlerG.dto;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document(indexName = "movie", type = "movie", shards = 1, replicas = 0, refreshInterval = "-1")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
	
	@Id
	private String id;	
	private List<String> actor;	
	private String adultYn;	
	private String category1Code;	
	private String category1Name;
	private String category2Code;	
	private String category2Name;
	private String code;
	private List<String> director;
	private List<String> displayCategory1;
	private List<String> displayCategory2;
	private String mcpCode;
	private String scpCode;
	private String downloadYn;
	private int duration;
	private String freeYn;  
	private String gradeCode;
	private String guestWatchYn;
	private String hdYn;
	private List<String> imageCodes;
	private List<String> imageUrls;
	private String initialConsonant;
	private List<String> keywords1;
	private List<String> keywords2;
	private String nameKo;
	private String nameEn;
	private String originalCp;
	private int price;
	private String productCountry;
	private int productYear;
	private String production;
	private String quality;
	private Double rating;
	private Long releaseDate;
	private String  sameCode;
	private String seriesCode;
	private String story;
	private String type;
}
