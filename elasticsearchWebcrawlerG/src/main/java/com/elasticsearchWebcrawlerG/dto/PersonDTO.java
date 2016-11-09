package com.elasticsearchWebcrawlerG.dto;

import lombok.Data;

@Data
public class PersonDTO {	

	private String ko; // 한글이름
	private String en; // 영문이름	
	private String prefix_ko;	
	private String leading_role;			// 주연
	private String supporting_role;			// 조연
}
