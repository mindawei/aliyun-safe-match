package alibaba.safe.fish.find.white;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import alibaba.safe.utils.FileUtil;

/**
 * 找到国内知名的网站
 */
public class WellKnownWebSite {

	private void run(String lastPart, PrintWriter printWriter) throws MalformedURLException, IOException {

		// Document document = Jsoup.parse(new URL("http://site.baidu.com/"), 3000);
		Document document = Jsoup.parse(new URL("http://top.chinaz.com/all/index_br" + lastPart + ".html"), 3000);

		Elements elements = document.select(".CentTxt");
		for (Element element : elements) {

			String title = element.select(".fz14").text().trim();
			String url = element.select(".col-gray").text().trim();

			if (!title.contains("银行"))
				continue;

			// if(title.contains("《")||title.contains("》")||title.contains("_")||
			// title.contains(".")||title.contains("-")||title.contains("·")||title.contains("
			// "))
			// continue;
			// String str = String.format("m.put(\"%s\",
			// \"%s\");\n",title.toLowerCase(),url.toLowerCase());

			try {

				System.out.println("http://" + url);

				// String realTitle = Jsoup.parse(new URL("http://"+url), 3000).title().trim();
				// if(realTitle.length()==0||realTitle.equals("正在登录"))
				// continue;
				//
				// String str = String.format("m.put(\"%s\",
				// \"%s\");\n",realTitle,url.toLowerCase());

				String htmlContent = Jsoup.parse(new URL("http://" + url), 3000).text().trim().toLowerCase();

				int index = htmlContent.indexOf("icp备");

				if (index != -1) {
					String icp = htmlContent.substring(index, index + 12);

					String str = String.format("m.put(\"%s\", \"%s\");\n", icp, url.toLowerCase());

					System.out.println(str);

					printWriter.write(str);
				}

				System.out.println("ok");
			} catch (Exception e) {
				System.out.println(" error");
			}

		}

	}

	private void run_bank() throws MalformedURLException, IOException {

		Document document = Jsoup.parse(new URL("http://www.hao123.com/bank"), 3000);
		Elements elements = document.select("a[href]");
		int num = 0;
		for (Element element : elements) {
			try {
				String urlStr = element.attr("href");
				URL url = new URL(urlStr);
				if (url.getPath().length() > 1 || url.getQuery() != null)
					continue;
				if (url.getHost().contains("baidu.com") || url.getHost().contains("hao123.com"))
					continue;

				String text = element.text().replace(" ", "");
				if (text.length() == 0 || text.contains("更多") || text.contains("95"))
					continue;

				String host = url.getHost();
				int index = host.indexOf(".");
				String domain = host.substring(index + 1);

				String str = String.format("name2domain.put(\"%s\", \"%s\");", element.text(), domain);
				// System.out.println(element.text()+","+url.getHost());
				System.out.println(str);

				// name2url.put("", "");

				num++;

			} catch (Exception e) {

			}

		}
		System.out.println(num);

	}

	private static Map<String, Set<String>> url2names = new HashMap<>();

	void run(String filePath, String charset, String... fromHosts) throws MalformedURLException, IOException {

		String content = getContent(filePath, charset);

		Document document = Jsoup.parse(content, charset);

		Elements elements = document.select("a[href]");

		for (Element element : elements) {
			String urlStr = element.attr("href");
			try {
				URL url = new URL(urlStr);
				if (url.getPath().length() > 1 || url.getQuery() != null)
					continue;
			} catch (Exception e) {
				continue;
			}

			String text = element.text().trim().replaceAll("\\s", "");
			if (text.length() == 0 || text.contains("更多") || text.contains("直播"))
				continue;

			String href = element.attr("href").trim();
			boolean isContinue = false;
			for (String fromHost : fromHosts) {
				if (href.contains(fromHost)) {
					isContinue = true;
					break;
				}
			}
			if (isContinue)
				continue;

			if (!url2names.containsKey(href)) {
				url2names.put(href, new HashSet<String>());
			}
			url2names.get(href).add(text);

		}

	}

	private String getContent(String filePath, String charset) {
		try {

			File file = new File(filePath);

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), charset));

			StringBuilder builder = new StringBuilder();

			String htmlLine = null;
			while ((htmlLine = bufferedReader.readLine()) != null) {
				builder.append(htmlLine);
				builder.append("\n");
			}
			String htmlContent = builder.toString();
			bufferedReader.close();

			return htmlContent;

		} catch (IOException e) {
			return "";
		}
	}

	static void run(String baseUrl, int num) throws MalformedURLException, IOException {

		// Document document = Jsoup.parse(new URL("http://site.baidu.com/"), 3000);

		Collection<String> titles = WhiteList.m.keySet();
		Collection<String> urls = WhiteList.m.values();

		for (int i = 1;; i++) {

			String lastPart = "";
			if (i > 1)
				lastPart += "_" + i;

			Document document = Jsoup.parse(new URL(baseUrl + lastPart + ".html"), 3000);

			Elements elements = document.select(".CentTxt");
			for (Element element : elements) {

				String title = element.select(".fz14").text().trim();
				String url = element.select(".col-gray").text().trim();

				if (title.contains("《") || title.contains("》") || title.contains("_") || title.contains(".")
						|| title.contains("-") || title.contains("·") || title.contains(" "))
					continue;

				String[] parts = url.split("\\.");
				int partsNum = parts.length;
				if (url.endsWith(".com.cn")) {
					url = parts[partsNum - 3] + ".com.cn";
				} else if (url.endsWith(".com")) {
					url = parts[partsNum - 2] + ".com";
				} else if (url.endsWith(".cn")) {
					url = parts[partsNum - 2] + ".cn";
				} else {
					continue;
				}

				if (urls.contains(url)) {
					continue;
				}

				if (titles.contains(title.toLowerCase())) {
					continue;
				}

				String str = String.format("m.put(\"%s\", \"%s\");", title.toLowerCase(), url.toLowerCase());
				System.out.println(str);

				num--;
				if (num == 0)
					return;
			}
		}
	}

	public static void main(String[] args) throws MalformedURLException, IOException {

		WellKnownWebSite webSite = new WellKnownWebSite();
		PrintWriter printWriter = null;
		// 1727
		for (int i = 1; i <= 1877; ++i) {
			if (printWriter == null) {
				printWriter = FileUtil.getPrintWriter(String.format("data\\白名单\\%04d.txt", i));
			} else {
				if (i % 30 == 0) {
					System.out.println(i + " ed");
					printWriter.close();
					printWriter = FileUtil.getPrintWriter(String.format("data\\白名单\\%04d.txt", i));
				}
			}
			if (i == 1)
				webSite.run("", printWriter);
			else
				webSite.run("_" + i, printWriter);

		}
		printWriter.close();

		// webSite.run("导航\\114la.html","utf-8","114la","beian");
		// webSite.run("导航\\2345.html","gb2312","2345","beian");
		// webSite.run("导航\\360.html","gb2312","360","beian");
		// webSite.run("导航\\baidu.html","gb2312","baidu","hao123","beian");
		// webSite.run("导航\\qq.html","gb2312","qq","sogou");
		// webSite.run("导航\\sogou.html","gb2312","sogou");
		// for(Map.Entry<String, Set<String>> entry : url2names.entrySet()){
		// String url = entry.getKey();
		// Set<String> names = entry.getValue();
		// System.out.println(url+":"+names);
		// }
		// System.out.println(url2names.entrySet().size());

		// 购物网站
		// run("http://top.chinaz.com/hangye/index_shopping_dianshang",100);
		// run("http://top.chinaz.com/hangye/index_shopping_gouwu",50);
		// run("http://top.chinaz.com/hangye/index_shopping_bijia",50);
		// run("http://top.chinaz.com/hangye/index_shopping_tuangou",50);

		// 银行
		// run("http://top.chinaz.com/hangye/index_shenghuo_yinhang",100);
		// 金融财经
		// run("http://top.chinaz.com/hangye/index_shenghuo_caijing",100);
		// 网贷
		// run("http://top.chinaz.com/hangye/index_shenghuo_p2p",100);
		// 证券
		// run("http://top.chinaz.com/hangye/index_shenghuo_zhengquan",100);

		// 企业
		// run("http://top.chinaz.com/hangye/index_qiye",500);

		// 广播电视
		// run("http://top.chinaz.com/hangye/index_news_dianshi",50);

		// 聊天交友
		// run("http://top.chinaz.com/hangye/index_shenghuo_jiaoyou",50);

		// 旅行社
		// run("http://top.chinaz.com/hangye/index_jiaotonglvyou_lvxingshe",20);

		// 旅游网站
		// run("http://top.chinaz.com/hangye/index_jiaotonglvyou_lvyou",10);

		// 票务预订
		// run("http://top.chinaz.com/hangye/index_jiaotonglvyou_piaowu",10);

		// 酒店
		// run("http://top.chinaz.com/hangye/index_jiaotonglvyou_jiudian",10);

		// 支付
		// run("http://top.chinaz.com/hangye/index_wangluo_zhifu",50);
	}
}
