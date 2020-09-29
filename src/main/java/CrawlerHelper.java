import Modules.Database;
import Modules.IOLib;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CrawlerHelper {
    public static Set<String> uniqueURL = new HashSet<>();
    public CrawlerHelper(){uniqueURL = Database.returnURL();}
    public static ArrayList<String> urls = new ArrayList<>();


    public static void main(String[] args) {
        Database.search("tbeatty");
//        getLinks("https://www.neumont.edu", 2);
//        getLinks("https://www.simplesite.com/", 0);
//        getLinks("http://www.tbeatty.com", 0);
//        int counter = 0;
//        while (counter < 3) {
//            String input = IOLib.promptForString("Please enter a URL to scrape: ");
//            urls.add(input);
//            counter++;
//        }
//
//        for (String url : urls) {
//  //          if (!url.contains("facebook")) {
//                getLinks(url, 3);
//                System.out.println("Scraping " + url);
//  //          }
//        }

    }

public static void getLinks(String URL, int hops) {
        if((!uniqueURL.contains(URL)) && hops < 2) {
            try {
                    uniqueURL.add(URL);
                    Document doc = Jsoup.connect(URL).get();
                    scrapePage(doc, URL);
                    Elements pageLinks = doc.select("a[href]");
                    hops++;
                    for (Element link : pageLinks) {
                        getLinks(link.attr("abs:href"), hops);
                    }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}


    public static void scrapePage(Document doc, String url) {
        Element body = doc.select("body").first();
        Database.writeToDatabase(url, body.text());
    }

}