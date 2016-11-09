package com.elasticsearchWebcrawlerG.util;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp3|zip|gz))$");

	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, git, ...
	 * extensions and to only accept urls that start with
	 * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
	 * parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		System.out.println(new Exception().getStackTrace()[0].getMethodName() + ":" + (new Throwable()).getStackTrace()[0].getLineNumber() + " href=" + href);
		return !FILTERS.matcher(href).matches() && href.startsWith("http://www.ics.uci.edu/");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println(new Exception().getStackTrace()[0].getMethodName() + ":" + (new Throwable()).getStackTrace()[0].getLineNumber() + " URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			Map<String, String > metaTagsMap = htmlParseData.getMetaTags();
			System.out.println(new Exception().getStackTrace()[0].getMethodName() + ":" + (new Throwable()).getStackTrace()[0].getLineNumber() + " metaTagsMap: " + metaTagsMap.toString());
			
			System.out.println(new Exception().getStackTrace()[0].getMethodName() + ":" + (new Throwable()).getStackTrace()[0].getLineNumber() + " Text length: " + text.length());
			System.out.println(new Exception().getStackTrace()[0].getMethodName() + ":" + (new Throwable()).getStackTrace()[0].getLineNumber() + " Html length: " + html.length());
			System.out.println(new Exception().getStackTrace()[0].getMethodName() + ":" + (new Throwable()).getStackTrace()[0].getLineNumber() + " Number of outgoing links: " + links.size());
			//System.out.println(new Exception().getStackTrace()[0].getMethodName() + ":" + (new Throwable()).getStackTrace()[0].getLineNumber() + " text: " + text);
			System.out.println(new Exception().getStackTrace()[0].getMethodName() + ":" + (new Throwable()).getStackTrace()[0].getLineNumber() + " html: " + html);
		}
	}
}
