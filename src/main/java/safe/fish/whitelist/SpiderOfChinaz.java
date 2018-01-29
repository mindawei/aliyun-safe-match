package safe.fish.whitelist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 找到国内知名的网站
 */
public class SpiderOfChinaz {
	
	public static void main(String[] args) {
		SpiderOfChinaz.run();
	}
	
	private static void run() {
		PrintWriter printWriter = null;
		int i =0;
		try {
			
		for ( i= 1800; i <= 1877; ++i) {
			if (printWriter == null) {
				printWriter = getPrintWriter(String.format("data\\白名单\\%04d.csv", i));
			} else {
				if (i % 200 == 0) {
					System.out.println(i + " ed");
					printWriter.close();
					printWriter = getPrintWriter(String.format("data\\白名单\\%04d.csv", i));
				}
			}
			if (i == 1)
				crawlData("", printWriter);
			else
				crawlData("_" + i, printWriter);
		}
		printWriter.close();
		}catch(Exception e) {
			System.out.println("error at i:"+i);
			e.printStackTrace();
		}
		
	}

	/* 爬取页面数据 */
	private static void crawlData(String lastPart, PrintWriter printWriter) throws MalformedURLException, IOException {
		
		Document document = Jsoup.parse(new URL("http://top.chinaz.com/all/index_br" + lastPart + ".html"), 3000);

		Elements elements = document.select(".CentTxt");
		for (Element element : elements) {

			String title = element.select(".fz14").text().trim();
			String url = element.select(".col-gray").text().trim();

			if (title.contains("《") || title.contains("》") || title.contains("_") || title.contains(".")
					|| title.contains("-") || title.contains("·") || title.contains(" "))
				continue;
			String str = String.format("%s,%s\n", title.toLowerCase(), url.toLowerCase());
			printWriter.write(str);
		}

	}


	/** 输出覆盖 */
	public static PrintWriter getPrintWriter(String outFilePath) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(outFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(outFilePath+" not found!");
			System.exit(0);
		}
		return out;
	}
}
