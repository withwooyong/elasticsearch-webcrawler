package com.elasticsearchWebcrawlerG.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Strings;

public class StringUtil {

	public static final Log log = LogFactory.getLog(StringUtil.class);

	public static final String EMPTY = "";
	public static final char CR = '\n';
	public static final char LF = '\r';

	private static String DEFAULT_SHORTEN_TAIL = "...";
	private static String BR = "<BR>";

	public static final int INDEX_NOT_FOUND = -1;
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	public static final String CHARSET = "UTF-8";
	public static final String CHARSET_EUCKR = "EUC-KR";
	public static final String SECURE_TOKEN_SEPARATOR = ":";
	public static final String HTTP = "http://";
	public static final String HTTPS = "https://";

	public static enum LENGTH {
		STRING, BYTE
	}

	public static enum InValid {
		SUCC, VALUE_NULL, ENCODE_ERR, MIN_LENGTH, MAX_LENGTH, REGEX
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isEmpty(Object obj) {
		return obj == null;
	}

	public static boolean isNotEmpty(String str) {
		return !StringUtil.isEmpty(str);
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static boolean isBlank(String str) {
		int strLen;

		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	public static String trimToEmpty(String str) {
		return str == null ? EMPTY : str.trim();
	}

	public static String stripStart(String str, String stripChars) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		int start = 0;
		if (stripChars == null) {
			while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
				start++;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
				start++;
			}
		}
		return str.substring(start);
	}

	public static String stripEnd(String str, String stripChars) {
		int end;
		if (str == null || (end = str.length()) == 0) {
			return str;
		}

		if (stripChars == null) {
			while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
				end--;
			}
		}
		return str.substring(0, end);
	}

	public static String strip(String str, String stripChars) {
		if (isEmpty(str)) {
			return str;
		}
		str = stripStart(str, stripChars);
		return stripEnd(str, stripChars);
	}

	public static boolean equals(String str, String comp) {
		return str == null ? comp == null : str.equals(comp);
	}

	public static boolean equalsIgnoreCase(String str, String comp) {
		return str == null ? comp == null : str.equalsIgnoreCase(comp);
	}

	public static int indexOf(String str, String search) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.indexOf(search);
	}

	public static int indexOf(String str, char searchChar) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.indexOf(searchChar);
	}

	public static int lastIndexOf(String str, String search) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.lastIndexOf(search);
	}

	public static int lastIndexOf(String str, char searchChar) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.lastIndexOf(searchChar);
	}

	public static boolean contains(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		return str.indexOf(searchStr) >= 0;
	}

	public static boolean containsIgnoreCase(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		return contains(str.toUpperCase(), searchStr.toUpperCase());
	}

	public static String substring(String str, int start) {
		if (str == null) {
			return null;
		}

		if (start < 0) {
			start = str.length() + start;
		}

		if (start < 0) {
			start = 0;
		}
		if (start > str.length()) {
			return EMPTY;
		}

		return str.substring(start);
	}

	public static String substring(String str, int start, int end) {
		if (str == null) {
			return null;
		}

		if (start < 0) {
			start = 0;
		}

		if (end < 0) {
			end = str.length();
		}

		if (end > str.length()) {
			end = str.length();
		}

		if (start > end) {
			return str;
		}

		return str.substring(start, end);
	}

	private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return EMPTY_STRING_ARRAY;
		}
		List<String> list = new ArrayList<String>();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1) {
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		} else {
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return list.toArray(new String[list.size()]);
	}

	private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return EMPTY_STRING_ARRAY;
		}
		List<String> list = new ArrayList<String>();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match || preserveAllTokens) {
					list.add(str.substring(start, i));
					match = false;
					lastMatch = true;
				}
				start = ++i;
				continue;
			} else {
				lastMatch = false;
			}
			match = true;
			i++;
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return list.toArray(new String[list.size()]);
	}

	public static String[] splitPreserveAllTokens(String str, char separatorChar) {
		return splitWorker(str, separatorChar, true);
	}

	public static String[] split(String str, String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}

	public static String removeStart(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.startsWith(remove)) {
			return str.substring(remove.length());
		}
		return str;
	}

	public static String removeEnd(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.endsWith(remove)) {
			return str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	public static String remove(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		return replace(str, remove, "", -1);
	}

	public static String replace(String text, String repl, String with, int max) {
		if (isEmpty(text) || isEmpty(repl) || with == null || max == 0) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(repl, start);
		if (end == -1) {
			return text;
		}
		int replLength = repl.length();
		int increase = with.length() - replLength;
		increase = (increase < 0 ? 0 : increase);
		increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
		StringBuffer buf = new StringBuffer(text.length() + increase);
		while (end != -1) {
			buf.append(text.substring(start, end)).append(with);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(repl, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	public static String replace(String text, String repl, String with) {
		return replace(text, repl, with, -1);
	}

	public static String replaceOnce(String text, String repl, String with) {
		return replace(text, repl, with, 1);
	}

	public static String upperCase(String str) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase();
	}

	public static String lowerCase(String str) {
		if (str == null) {
			return null;
		}
		return str.toLowerCase();
	}

	public static boolean isAlpha(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (isAsciiAlpha(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAlphaSpace(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if ((isAsciiAlpha(str.charAt(i)) == false) && (str.charAt(i) != ' ')) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAlphanumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (isAsciiAlphanumeric(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAlphanumericSpace(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if ((isAsciiAlphanumeric(str.charAt(i)) == false) && (str.charAt(i) != ' ')) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (isAsciiNumeric(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumericSpace(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if ((isAsciiNumeric(str.charAt(i)) == false) && (str.charAt(i) != ' ')) {
				return false;
			}
		}
		return true;
	}

	public static boolean isWhitespace(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static String defaultString(String str, String defaultStr) {
		return str == null ? defaultStr : str;
	}

	public static String defaultIfEmpty(String str, String defaultStr) {
		return StringUtil.isEmpty(str) ? defaultStr : str;
	}

	public static String reverse(String str) {
		if (str == null) {
			return null;
		}
		return new StringBuffer(str).reverse().toString();
	}

	public static String unicodeEscaped(char ch) {
		if (ch < 0x10) {
			return "\\u000" + Integer.toHexString(ch);
		} else if (ch < 0x100) {
			return "\\u00" + Integer.toHexString(ch);
		} else if (ch < 0x1000) {
			return "\\u0" + Integer.toHexString(ch);
		}
		return "\\u" + Integer.toHexString(ch);
	}

	public static boolean isAscii(char ch) {
		return ch < 128;
	}

	public static boolean isAsciiPrintable(char ch) {
		return ch >= 32 && ch < 127;
	}

	public static boolean isAsciiAlpha(char ch) {
		return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
	}

	public static boolean isAsciiAlphaUpper(char ch) {
		return ch >= 'A' && ch <= 'Z';
	}

	public static boolean isAsciiAlphaLower(char ch) {
		return ch >= 'a' && ch <= 'z';
	}

	public static boolean isAsciiNumeric(char ch) {
		return ch >= '0' && ch <= '9';
	}

	public static boolean isAsciiAlphanumeric(char ch) {
		return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
	}

	public static String hex(char ch) {
		return Integer.toHexString(ch).toUpperCase();
	}

	public static StringTokenizer getToken(String str, String delimeter) {
		if (str != null) {
			if (delimeter != null) {
				delimeter = "";
			}
			StringTokenizer stringToken = new StringTokenizer(str, delimeter);
			return stringToken;
		}
		return null;
	}

	public static StringTokenizer getToken(String str) {
		if (str != null) {
			return new StringTokenizer(str);
		}
		return null;
	}

	public static String append(String... str) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			b.append(str[i]);
		}
		return b.toString();
	}

	public static InValid isValidDetail(String value, int min, int max, LENGTH type, String regex,
			boolean regexBoolean) {
		int len;

		if (value == null) {
			log.error("ERROR, value is null");
			return InValid.VALUE_NULL;
		}

		if (type == LENGTH.BYTE) {
			try {
				len = value.getBytes("utf-8").length;
			} catch (UnsupportedEncodingException e) {
				log.error("ERROR, value=" + value + ", e=" + e);
				return InValid.ENCODE_ERR;
			}
		} else {
			len = value.length();
		}

		if ((min != -1) && (len < min)) {
			log.error("ERROR, len(" + len + ") < min(" + min + "), value=" + value);
			return InValid.MIN_LENGTH;
		} else if ((max != -1) && (len > max)) {
			log.error("ERROR, len(" + len + ") > max(" + max + "), vlaue=" + value);
			return InValid.MAX_LENGTH;
		} else if ((regex != null) && (value.matches(regex) == regexBoolean)) {
			log.error("ERROR, regex=" + regex + ", value=" + value);
			return InValid.REGEX;
		}

		return InValid.SUCC;
	}

	public static InValid isValidDetail(String value, int min, int max, LENGTH type, String regex) {
		return isValidDetail(value, min, max, type, regex, true);
	}

	public static boolean isValid(String value, int min, int max, LENGTH type, String regex) {
		return isValidDetail(value, min, max, type, regex) == InValid.SUCC ? true : false;
	}

	public static boolean isValid(String value, int min, int max, LENGTH type, String regex, boolean regexBoolean) {
		return isValidDetail(value, min, max, type, regex, regexBoolean) == InValid.SUCC ? true : false;
	}

	public static String makeMD5Digest(String id) {
		if (id == null || "".equals(id)) {
			id = Long.toString(System.currentTimeMillis());
		}

		try {
			byte[] defaultBytes = id.getBytes();
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte[] messageDigest = algorithm.digest();
			StringBuilder hexString = new StringBuilder();

			for (int i = 0; i < messageDigest.length; i++) {
				hexString.append(String.format("%02x", (0xFF & (char) messageDigest[i])));
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String urlEncode(String str) {
		if (isEmpty(str) == true) {
			return "";
		}

		try {
			String s = URLEncoder.encode(str, CHARSET);
			return s;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String urlDecode(String str) {
		if (isEmpty(str) == true) {
			return "";
		}

		try {
			String s = URLDecoder.decode(str, CHARSET);
			return s;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String stripTags(String str) {
		if (isEmpty(str) == true) {
			return str;
		}

		StringBuffer w = new StringBuffer();
		boolean tagOpen = false;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if ((!tagOpen && c == '<') || (tagOpen && c == '>')) {
				tagOpen = !tagOpen;
			} else if (!tagOpen) {
				w.append(c);
			}
		}

		return w.toString();
	}

	public static String stripTagsAndNickname(String str) {
		if (isEmpty(str) == true) {
			return str;
		}

		StringBuffer w = new StringBuffer();
		boolean tagOpen = false;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if ((!tagOpen && c == '<') || (tagOpen && c == '>')) {
				tagOpen = !tagOpen;
			} else if (!tagOpen) {
				w.append(c);
			}
		}

		String org = w.toString();
		int idx = -1;
		if ((idx = org.indexOf("]")) != -1) {
			org = org.substring(idx + 1).trim();
		}

		return org;
	}

	public static String stripHeadBodyDivTag(String org) {
		if (isEmpty(org) == true) {
			return org;
		}
		String str = org.replaceAll("<\\s*[Hh][Ee][Aa][Dd].*\\/[Hh][Ee][Aa][Dd]\\s*>", "");
		// System.out.println("TRACE, StringUtil.stripHeadBodyTag()"
		// + ", strip(<HEAD></HEAD>), org=" + org + ", str=" + str);

		str = str.replaceAll("<\\s*[Bb][Oo][Dd][Yy]\\s*[^>]*>", "");
		// System.out.println("TRACE, StringUtil.stripHeadBodyTag()"
		// + ", strip(<BODY>), str=" + str);

		str = str.replaceAll("<\\/[Bb][Oo][Dd][Yy]\\s*>", "");
		// System.out.println("TRACE, StringUtil.stripHeadBodyTag()"
		// + ", strip(</BODY>), str=" + str);

		str = str.replaceAll("<\\s*[Dd][Ii][Vv]\\s*[^>]*>", BR);
		// System.out.println("TRACE, StringUtil.stripHeadBodyTag()"
		// + ", strip(<Div>), str=" + str);

		str = str.replaceAll("<\\/[Dd][Ii][Vv]\\s*>", "");
		// System.out.println("TRACE, StringUtil.stripHeadBodyTag()"
		// + ", strip(</Div>), str=" + str);

		str = str.replaceFirst(BR, "");
		// System.out.println("TRACE, StringUtil.stripHeadBodyTag()"
		// + ", first strip(<BR>), str=" + str);

		return str;
	}

	public static String shorten(String str, float len, boolean isSkipTag, String shortenTail, double notAsciiCnt,
			double asciiCnt) {
		if (isEmpty(str) == true) {
			return str;
		}

		double l = 0.0;
		boolean tagOpen = false;

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if ((isSkipTag == true) && ((!tagOpen && c == '<') || (tagOpen && c == '>'))) {
				tagOpen = !tagOpen;
			} else if (!tagOpen) {
				l += (c > 255) ? notAsciiCnt : asciiCnt;
			}

			if (l > len) {
				buf.append(shortenTail);
				return buf.toString();
			}

			buf.append(c);
		}

		return buf.toString();
	}

	public static String shorten(String str, int len) {
		return shorten(str, len, false, DEFAULT_SHORTEN_TAIL, 2, 1);
	}

	public static String shorten2(String str, int len) {
		return shorten(str, len, false, DEFAULT_SHORTEN_TAIL, 2, 2);
	}

	public static String getHtmlToText(final String strHtml) {
		return getHtmlToText(strHtml, 0, "");
	}

	public static String getHtmlToText2(final String strHtml) {
		return getHtmlToText(getConvertAmpCharToChar(strHtml), 0, "");
	}

	public static String getHtmlToTextBr(final String strHtml) {
		return getHtmlToText(strHtml, 0, BR);
	}

	public static String getHtmlToTextBr2(final String strHtml) {
		return getHtmlToText(getConvertAmpCharToChar(strHtml), 0, BR);
	}

	public static String getHtmlToText(final String strHtml, int dispFlag, String br) {
		String turnStr = strHtml;
		String[] defHtmlParam = new String[] { "&", "&amp;", // bit-1
				"<", "&lt;", // bit-2
				">", "&gt;", // bit-3
				"\"", "&quot;", // bit-4
				"\\n", br // bit-5
		};

		if (strHtml == null) {
			turnStr = "";
		} else {
			turnStr = strHtml.trim();
		}

		for (int i = 0; i < defHtmlParam.length; i += 2) {
			if ((dispFlag & (1 << (i / 2))) == 0) {
				turnStr = turnStr.replaceAll(defHtmlParam[i], defHtmlParam[i + 1]);
			}
		}

		return turnStr;
	}

	public static String getConvertAmpCharToChar(final String org) {
		return org.replaceAll("&nbsp;", " ");
	}

	public static boolean isHttpUrl(String url) {
		if (url == null) {
			return false;
		} else if ((url.toLowerCase().startsWith(HTTP) == true) || (url.toLowerCase().startsWith(HTTPS) == true)) {
			return true;
		}

		return false;
	}

	public static String getPoiKey(String poiKey, int retLen) {
		if (StringUtil.isEmpty(poiKey) == true) {
			return null;
		}

		StringBuffer buff = new StringBuffer(poiKey);
		for (int i = buff.length(); i < retLen; i++) {
			buff.insert(0, "0");
		}

		return buff.toString();
	}

	public static String getPoiKey(String poiKey) {
		return getPoiKey(poiKey, 12);
	}

	public static String toStringMulitLine(Object o) {
		StringBuffer buff = new StringBuffer("INLINE_START, ");
		if (o instanceof String) {
			return buff.append("value = {".concat(o.toString()).concat("}")).append(", INLINE_END").toString();
		} else {
			return buff.append(ToStringBuilder.reflectionToString(o, ToStringStyle.MULTI_LINE_STYLE))
					.append(", INLINE_END").toString();
		}
	}

	public static Map<String, Object> getMap(String key, Object val) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(key, val);

		return map;
	}

	public static Map<String, Object> getMap(Map<String, Object> map, String key, Object val) {
		if (map == null) {
			map = new HashMap<String, Object>();
		}

		map.put(key, val);

		return map;
	}

	public static int countToken(String pm_sSrc, String pm_sDelim) {

		if (pm_sSrc == null || pm_sSrc.length() == 0) {
			return 0;
		}

		int lm_iCnt = 0; // 토큰 수
		String lm_sSrc = pm_sSrc;
		int lm_iIndex = 0;

		while (true) {
			lm_iIndex = lm_sSrc.indexOf(pm_sDelim);

			if (lm_iIndex == -1) {
				lm_iCnt++;
				break;
			} else {
				lm_iCnt++;
				lm_sSrc = lm_sSrc.substring(lm_iIndex + pm_sDelim.length());
			}
		}

		return lm_iCnt;

	}

	public static String replaceAll(String pm_sString, String pm_sOld, String pm_sNew) {

		if (pm_sString == null || pm_sString.length() == 0) {
			return "";
		}

		if (pm_sOld == null || pm_sOld.length() == 0) {
			return pm_sString;
		}

		int lm_iIndex = pm_sString.indexOf(pm_sOld);
		StringBuilder lm_oResult = new StringBuilder();

		if (lm_iIndex == -1) {
			return pm_sString;
		}

		lm_oResult.append(pm_sString.substring(0, lm_iIndex) + pm_sNew);

		if (lm_iIndex + pm_sOld.length() < pm_sString.length()) {
			lm_oResult.append(replaceAll(pm_sString.substring(lm_iIndex + pm_sOld.length(), pm_sString.length()),
					pm_sOld, pm_sNew));
		}

		return lm_oResult.toString();

	}

	public static int length(String str) {
		if (isEmpty(str) == true) {
			return 0;
		}
		return str.length();
	}

	public static void errLog(Exception e) {
		StringBuffer buff = new StringBuffer();
		buff.append(e);

		for (int i = 0; i < 5; i++) {
			StackTraceElement ste = e.getStackTrace()[i];
			buff.append("\n").append(ste);
		}

		StringUtil.log.error("ERR, " + buff);
	}

	public static String utf8TOutf8(String str) {
		if (isNotEmpty(str)) {
			Pattern p = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
			Matcher m = p.matcher(str);
			while (m.find()) {
				str = str.replaceAll("\\".concat(m.group(0)),
						Character.toString((char) Integer.parseInt(m.group(1), 16)));
			}
			return str;
		} else {
			return "";
		}
	}

	public static boolean IntHexEqual(int a, String hex) {
		int b = 0;
		try {
			hex = hex.replaceAll("0[Xx]", "");
			b = Integer.parseInt(hex, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return a == b;
	}

	public static boolean IntHexEqual2(int a, String mask, String hex) {
		int b = 0;
		int m = 0;
		try {
			hex = hex.replaceAll("0[Xx]", "");
			String maskHex = mask.replaceAll("0[Xx]", "");
			b = Integer.parseInt(hex, 16);
			m = Integer.parseInt(maskHex, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (a & m) == b;
	}

	public static boolean IntHexEqualF0(int a, String hex) {
		int b = 0;
		try {
			hex = hex.replaceAll("0[Xx]", "");
			b = Integer.parseInt(hex, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (a & 0x1FFFFFF0) == b;
	}

	public static boolean bitOperAnd(int a, String hex) {
		int b = 0;
		try {
			hex = hex.replaceAll("0[Xx]", "");
			b = Integer.parseInt(hex, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (a & b) > 0;
	}

	public static boolean bitOperAnd2(int a, String mask, String hex) {
		int b = 0;
		try {
			hex = hex.replaceAll("0[Xx]", "");
			b = Integer.parseInt(hex, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (bitAndMask(a, mask) & b) > 0;
	}

	public static boolean bitOperOr(int a, String hex) {
		int b = 0;
		try {
			hex = hex.replaceAll("0[Xx]", "");
			b = Integer.parseInt(hex, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (a | b) > 0;
	}

	public static int bitAndMask(int a, String mask) {
		int m = 0;

		try {
			String maskHex = mask.replaceAll("0[Xx]", "");
			m = Integer.parseInt(maskHex, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return a & m;
	}

	public static int bitOrMask(int a, String mask) {
		int m = 0;

		try {
			String maskHex = mask.replaceAll("0[Xx]", "");
			m = Integer.parseInt(maskHex, 16);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return a | m;
	}

	public static String toHexString(int org, int padding) {
		StringBuffer buff = new StringBuffer();
		buff.append(Integer.toHexString(org).toUpperCase());
		for (int i = buff.length(); i < padding; i++) {
			buff.insert(0, "0");
		}
		buff.insert(0, "0x");
		return buff.toString();
	}

	public static String toHexString(int org) {
		return toHexString(org, 8);
	}
	
	public static <T> List<T> castCollection(List<?> srcList, Class<T> clas) {
		List<T> list = new ArrayList<T>();
		for (Object obj : srcList) {
			if (obj != null && clas.isAssignableFrom(obj.getClass()))
				list.add(clas.cast(obj));
		}
		return list;
	}
	
	public static String getBody(HttpServletRequest request) throws IOException {
		 
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
 
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
 
        body = stringBuilder.toString();
        return body;
    }
	
	public static String createRandomString() {
		Random random = new Random(System.currentTimeMillis());
		long randomLong = random.nextLong();
		return Long.toHexString(randomLong);
	}
	
	public static String getDateTime() {
		Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        String today = (new SimpleDateFormat("yyyyMMddHHmmss").format(date));
        return today;
	}
	
	public static List<String> subDirList(String source) {
		File dir = new File(source);
		File[] fileList = dir.listFiles();
		List<String> strList = new ArrayList<>(); 
		
		if (fileList == null) {
			return strList;
		} else {
			try {
				for (int i = 0; i < fileList.length; i++) {
					File file = fileList[i];
					if (file.isFile()) {
						// 파일이 있다면 파일 이름 출력
						// System.out.println("\t 파일 이름 = " + file.getName());
						strList.add(file.getPath());					
					} else if (file.isDirectory()) {
						// System.out.println("디렉토리 이름 = " + file.getName());
						// 서브디렉토리가 존재하면 재귀적 방법으로 다시 탐색
						subDirList(file.getCanonicalPath().toString());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return strList;
	}
	
//	public static void main(String[] args) {
//		
//		String key = "ffmpeg-" + UUID.randomUUID().toString();
//		System.out.println(key);
//		System.out.println(System.currentTimeMillis());
//		System.out.println(System.currentTimeMillis());
//		System.out.println(System.nanoTime());
//		System.out.println(System.nanoTime());
//		System.out.println("ffmpeg-" + createRandomString());
//	}
}
