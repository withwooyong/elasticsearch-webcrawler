package com.elasticsearchWebcrawlerG.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.elasticsearchWebcrawlerG.dto.MovieDTO;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class CrawlerMovie {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${URL.BASIC}")
	private String URL_BASIC;
	
	@Value("${URL.DETAIL}")
	private String URL_DETAIL;
	
	@Value("${IMAGE.PATH}")
	private String IMAGE_PATH;
	
//	private static String URL_BASIC = "http://movie.naver.com/movie/bi/mi/basic.nhn?code=";
//	private static String URL_DETAIL = "http://movie.naver.com/movie/bi/mi/detail.nhn?code=";
	// http://movie.naver.com/movie/bi/pi/filmo.nhn?code=1326 인물상세
//	private static String IMAGE_PATH = "D:\\sts-3.7.3.RELEASE\\workspace\\crawler\\src\\main\\java\\org\\test\\data\\img";
	
	// Poster, Actor, Director
	enum image_gubun {
		POSTER, ACTOR, DIRECTOR
	};
	
	public String getImagePath() {
		return this.IMAGE_PATH;
	}
	
	public MovieDTO getMeta(String code, MovieDTO movieDTO) throws Exception {		
		if (!Strings.isNullOrEmpty(code)) {
			movieDTO = getBasic(URL_BASIC, code, movieDTO);	// 주요정보
			movieDTO = getDetail(URL_DETAIL, code, movieDTO); // 배우/제작진
		}	
		return movieDTO; 
	}
	
	// Basic
	public MovieDTO getBasic(String url, String code, MovieDTO dto) throws Exception {

		log.info("{}{}", url, code);
		StringBuffer buf = new StringBuffer();
		Document doc = Jsoup.connect(url + code).get();
		dto.setCode(code);

		// 한글명
		Element h_movie = doc.select(".mv_info_area .mv_info .h_movie a").first();
		log.info("h_movie.text()={}", h_movie.text());
		dto.setNameKo(h_movie.text());

		// 영문명
		Element h_movie2 = doc.select(".mv_info_area .mv_info .h_movie2").first();
		log.info("h_movie2.text()={}", h_movie2.text());
		dto.setNameEn(Optional.fromNullable(h_movie2.text()).or(""));

		// 관람객
		Elements ntz_score = doc.select(".mv_info_area .ntz_score .star_score em");
		for (Element element : ntz_score) {
			buf.append(element.text());
		}

		String ntzScore = Strings.isNullOrEmpty(buf.toString()) ? "0.0" : buf.toString();
		// buf.toString().isEmpty() ? 0 : Double.parseDouble(buf.toString())
		// String ntzScore = Optional.fromNullable(buf.toString()).or("0.0");
		log.info("setNtzScore={}", ntzScore);
		dto.setNtzScore(Double.parseDouble(ntzScore));

		// 기자/평론가
		Elements spc = doc.select(".mv_info_area .spc .star_score em");
		buf = new StringBuffer();
		for (Element element : spc) {
			buf.append(element.text());
		}
		String spcScore = Strings.isNullOrEmpty(buf.toString()) ? "0.0" : buf.toString();
		// String spcScore = Optional.fromNullable(buf.toString()).or("0.0");
		log.info("setSpcScore={}", spcScore);
		dto.setSpcScore(Double.parseDouble(spcScore));

		// 개요 [장르, 국가명, 상영시간, 개봉일]
		Elements info_spec_span = doc.select(".mv_info_area .mv_info .info_spec span");

		List<String> genre = new ArrayList<String>();
		int index = 0;
		for (Element element : info_spec_span) {
			if (index == 0) { // 장르
				String[] temps = element.text().split(",");
				genre = new ArrayList<String>(Arrays.asList(temps));
				log.info("genre={}", genre);
				dto.setGenresName(genre);
			} else if (index == 1) { // 국가명
				log.info("setCountryName={}", element.text());
				dto.setCountryName(Strings.nullToEmpty(element.text()));
			} else if (index == 2) { // 상영시간
				log.info("setDuration={}", element.text());
				dto.setDuration(Integer.valueOf(Strings.nullToEmpty(element.text()).replaceAll("[^0-9]", "")));
			} else if (index == 3) { // 개봉일
				log.info("setReleaseDate={}", element.text());
				dto.setReleaseDate(Long.valueOf(Strings.nullToEmpty(element.text()).replaceAll("[^0-9]", "")));
			}
			index++;
		}

		// 등급
		index = 0;
		Elements info_spec_dd = doc.select(".mv_info_area .mv_info .info_spec dd");
		for (Element element : info_spec_dd) { // [국내] 12세 관람가
			if (index == 3) {
				log.info("setGrade={}", Strings.nullToEmpty(element.text()));
				dto.setGrade(Strings.nullToEmpty(element.text()));
			}
			index++;
		}

		// 줄거리
		Element con_tx = doc.select(".section_group .story_area .con_tx").first();
		log.info("setStory={}", Strings.nullToEmpty(con_tx.text()));
		dto.setStory(Strings.nullToEmpty(con_tx.text()));

		// 포스터
		Element poster = doc.select(".mv_info_area .poster img").first();
		log.info("setPoster={}", image_gubun.POSTER.name() + "_" + code);
		dto.setPoster(image_gubun.POSTER.name() + "_" + code);
		imageDownload(poster.attr("src").toString(), image_gubun.POSTER.name(), code);

		log.info("dto={}", dto.toString());
		return dto;
	}

	// detail
	public MovieDTO getDetail(String url, String code, MovieDTO dto) throws Exception {

		log.debug("{}{}", url, code);
		Document doc = Jsoup.connect(url + code).get();

		Elements lst_people = doc.select(".lst_people li");

		List<String> actors_code = new ArrayList<String>();
		List<String> actors_ko = new ArrayList<String>();
		List<String> actors_en = new ArrayList<String>();
		List<String> actors_image = new ArrayList<String>();
		int index = 0;
		for (Element element : lst_people) {
			if (StringUtils.isNotEmpty(element.select(".k_name").text())
					&& StringUtils.isNotEmpty(element.select(".e_name").text())) {

				String href = element.select(".k_name").attr("href"); // /movie/bi/pi/basic.nhn?code=1326
				String actor_code = href.substring(href.lastIndexOf("=") + 1); // 1326
				actors_code.add(actor_code); // 코드

				actors_ko.add(element.select(".k_name").text()); // 한글명
				actors_en.add(element.select(".e_name").text()); // 영문명

				String img_url = element.select("img").attr("src").toString(); // http://tv03.search.naver.net/nhnsvc?size=111x139&quality=9&q=http://sstatic.naver.net/people/37/201504171519202401.jpg
				actors_image.add(image_gubun.ACTOR.name() + "_" + actor_code); // 이미지
				imageDownload(img_url, image_gubun.ACTOR.name(), actor_code);
			}
		}
		log.info("setActorsCode={}", actors_code);
		log.info("setActorsKo={}", actors_ko);
		log.info("setActorsEn={}", actors_en);
		log.info("setActorsImg={}", actors_image);
		dto.setActorsCode(actors_code);
		dto.setActorsKo(actors_ko);
		dto.setActorsEn(actors_en);
		dto.setActorsImg(actors_image);

		// 감독
		Element dir_product = doc.select(".dir_product").first();
		dto.setDirectorKo(Strings.nullToEmpty(dir_product.select(".k_name").text()));
		dto.setDirectorEn(Strings.nullToEmpty(dir_product.select(".e_name").text()));

		log.info("getDirectorKo={}", dto.getDirectorKo());
		log.info("getDirectorEn={}", dto.getDirectorEn());

		String href = dir_product.select(".k_name").attr("href"); // /movie/bi/pi/basic.nhn?code=1326
		String director_code = href.substring(href.lastIndexOf("=") + 1); // 1326
		dto.setDirectorCode(director_code);

		log.info(dir_product.select("img").attr("src").toString());
		String img_url = dir_product.select("img").attr("src").toString(); // http://tv03.search.naver.net/nhnsvc?size=111x139&quality=9&q=http://sstatic.naver.net/people/37/201504171519202401.jpg
		dto.setDirectorImg(image_gubun.DIRECTOR.name() + "_" + director_code);
		imageDownload(img_url, image_gubun.DIRECTOR.name(), director_code);

		Element h_movie2 = doc.select(".mv_info_area .mv_info .h_movie2").first();
		dto.setNameEn(Strings.nullToEmpty(h_movie2.text()));

		// 수입/배급
		Elements agencys_name = doc.select(".agency_name dd");

		index = 0;
		for (Element element : agencys_name) {
			if (index == 0) { // 수입
				dto.setImporter(Strings.nullToEmpty(element.text()));
			} else if (index == 1) { // 배급
				dto.setDistributor(Strings.nullToEmpty(element.text()));
			}
			index++;
		}
		log.debug("{}", dto.toString());
		return dto;
	}
	
	// P poster A actor D director
	private void imageDownload(String imgUrl, String gubun, String code) throws Exception {
		log.info("imgUrl={}, gubun={}, code={}", imgUrl, gubun, code);
		// imgUrl = "http://movie2.phinf.naver.net/20150324_33/1427159150181p9uQM_JPEG/movie_image.jpg?type=m203_290_2"; Poster
		// imgUrl = "http://tv03.search.naver.net/nhnsvc?size=111x139&quality=9&q=http://sstatic.naver.net/people/37/201504171519202401.jpg"; Actor
		URL url = new URL(imgUrl);
		String ext = "";
		if (image_gubun.POSTER.name().equals(gubun)) {
			ext = imgUrl.substring( imgUrl.lastIndexOf('.')+1, imgUrl.lastIndexOf('?'));  // 이미지 확장자 추출
		} else if (image_gubun.ACTOR.name().equals(gubun)) {
			ext = imgUrl.substring( imgUrl.lastIndexOf('.')+1);
		}		
		log.info("{}", ext);
		BufferedImage img = ImageIO.read(url);
		System.out.println(System.getProperty("user.dir"));
		System.out.println(IMAGE_PATH);
		ImageIO.write(img, ext, new File(IMAGE_PATH + "/" + gubun + "_" + code + "."+ext));
	}
}
