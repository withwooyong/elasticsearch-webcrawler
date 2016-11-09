package com.elasticsearchWebcrawlerG.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * http://www.namooz.com/2015/11/10/spring-boot-thymeleaf-4-reload-contents-without-restarting-tomcat-server/
 * 
 * @author user
 *
 */
@Configuration
public class ThymeleafConfig {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public ThymeleafProperties thymeleafProperties;

	@Bean
	public TemplateResolver templateResolver() {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setPrefix(thymeleafProperties.getPrefix());
		templateResolver.setSuffix(thymeleafProperties.getSuffix());
		templateResolver.setTemplateMode(thymeleafProperties.getMode());
		templateResolver.setCacheable(thymeleafProperties.isCache());
		templateResolver.setCharacterEncoding(thymeleafProperties.getEncoding().toString());

		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		return templateEngine;
	}

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setOrder(1);
		viewResolver.setContentType(thymeleafProperties.getContentType().toString());
		viewResolver.setCharacterEncoding(thymeleafProperties.getEncoding().toString());
		return viewResolver;
	}
}