package com.bld.http.imageCache;

public class SimpleUrlFileName {

	public static String replaceHostUrl(String url) {

		if (url == null || url.length() <= 0)
			return url;
		url = url.replaceAll("&#xd;", "");
		url = url.replaceAll("&quot;", "\"");
		url = url.replaceAll("&#039;", "'");
		url = url.replaceAll("&lt;", "<");
		url = url.replaceAll("&gt;", ">");
		url = url.replaceAll("&amp;", "&");
		url = url.replaceAll("&ldquo;", "\"");
		url = url.replaceAll("&rdquo;", "\"");
		return url;

	}

	public static String getUrlName(String url) {
		if (url == null || url.length() <= 0)
			return url;
		int lastindex = url.lastIndexOf('.');
		if (lastindex >= url.length() - 4)
			return getUrlName(url, url.substring(lastindex));
		return getUrlName(url, ".png");
	}
	public static String getLongUrlName(String url) {
		if (url == null || url.length() <= 0)
			return url;

			return replace(url);

	}
	
	public static String replace(String url) {
		if (url == null || url.length() <= 0)
			return url;

		String filename = url;
		filename = filename.replace("http://", "");
		filename = filename.replace('/', '_');
		filename = filename.replace('/', '_');
		filename = filename.replace('&', '_');
		filename = filename.replace('?', '_');
		filename = filename.replace('=', '_');
		
		//对此符号不必置换
//		filename = filename.replace('.', '_');
		filename = filename.replace('>', '_');
		filename = filename.replace('<', '_');
		filename = filename.replace('\'', '_');
		filename = filename.replace('\"', '_');
		filename = filename.replace(';', '_');

		return filename;
	}

	public static String getUrlName(String url, String type) {
		if (url == null || url.length() <= 0)
			return url;

		String filename = url;
		if (filename.lastIndexOf("/") != -1) {
			filename = url.substring(url.lastIndexOf("/") + 1);
		}

		filename = replace(filename);

		return filename + type;
	}

}
