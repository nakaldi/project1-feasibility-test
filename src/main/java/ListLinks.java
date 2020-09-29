import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Example program to list links from a URL.
 */
public class ListLinks {
    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 0, "usage: delete url to fetch");


        Document doc1 = Jsoup.connect("https://search.naver.com/search.naver?where=news&query=%EB%B6%81%ED%95%9C&sm=tab_srt&sort=1&photo=0&field=0&reporter_article=&pd=0&ds=&de=&docid=&nso=so%3Add%2Cp%3Aall%2Ca%3Aall&mynews=0&refresh_start=0&related=0")
                .get();
        ParsNaver(doc1);
/*
        Document doc2 = Jsoup.connect("https://www.youtube.com/user/spotv/search?query=%ED%86%A0%ED%8A%B8%EB%84%98")
                .get();
        Elements contents = doc2.select("div ytd-item-section-renderer");
        System.out.println(contents);
        print("%s", doc2.html());
*/
/*
        Document doc3 = Jsoup.connect("https://www.instagram.com/explore/tags/%ED%86%A0%ED%8A%B8%EB%84%98/")
                .get();
        Elements instagrams = doc3.select("div");
        System.out.println(insta);
        print("%s", doc3.html());
*/
/*
        Document doc4 = Jsoup.connect("https://twitter.com/search?q=%EC%86%90%ED%9D%A5%EB%AF%BC&src=typed_query&f=live")
                .get();
        Elements twitters = doc4.select("section.css-1dbjc4n div div");
        System.out.println(twitters);
        print("%s", doc4.html());
*/
        Document doc5 = Jsoup.connect("https://search.daum.net/search?w=news&sort=recency&q=%EB%B6%81%ED%95%9C&cluster=n&DA=STC&dc=STC&pg=1&r=1&p=1&rc=1&at=more&sd=&ed=&period=")
                .get();
        ParsDaum(doc5);

        //로그인 할때 쓸것. 서울시 사이트는 이거 안쓰고 그냥 검색 data 하나만 넣어주니까 되네
//        Connection.Response seoulSearch = Jsoup.connect("https://www.seoul.go.kr/realmnews/in/list.do")
//                .timeout(10 * 1000)
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .header("Accept-Encoding", "gzip, deflate, br")
//                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
//                .header("Cache-Control", "no-cache")
//                .header("Connection", "keep-alive")
//                .header("Content-Length", "64")
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .header("Host", "www.seoul.go.kr")
//                .header("Origin", "https://www.seoul.go.kr")
//                .header("Pragma", "no-cache")
//                .header("Referer", "https://www.seoul.go.kr/realmnews/in/list.do")
//                .header(" Sec-Fetch-Dest", "document")
//                .header("Sec-Fetch-Mode", "navigate")
//                .header("Sec-Fetch-Site", "same-origin")
//                .header("Sec-Fetch-User", "?1")
//                .header("Upgrade-Insecure-Requests", "1")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
//                .method(Connection.Method.GET)
//                .execute();
//
//        Map<String, String> seoulSearchCookies = seoulSearch.cookies();
//
//        Map<String, String> data = new HashMap<>();
//        data.put("searchWord", "청년");


        Connection.Response seoulSearchResponse = Jsoup.connect("https://www.seoul.go.kr/realmnews/in/list.do")
                .timeout(10*1000)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
                .data("searchWord", "청년")
                .method(Connection.Method.POST)
                .execute();

        Document seoulSearchDoc = seoulSearchResponse.parse();
        Elements seoulNews = seoulSearchDoc.select("div.news-lst div");
        print("\nseoulNews: (%d)", seoulNews.size());
        for (Element article : seoulNews) {
            print(" * a: <%s>  (%s)", article.select("a[href]").attr("href"), trim(article.text(), 35));
        }

/**
        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.normalName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }*/
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }

    private static void ParsNaver(Document doc){
        Elements articles = doc.select("ul.type01 li");

        print("\nArticles: (%d)", articles.size());
        for (Element article : articles) {
            print(" * a: <%s>  (%s)", article.select("a[href]").attr("href"), trim(article.text(), 35));
        }
    }

    private static void ParsDaum(Document doc){
        Elements articles = doc.select("#newsResultUL li");

        print("\nArticles: (%d)", articles.size());
        for (Element article : articles) {
            print(" * a: <%s>  (%s)", article.select("a[href]").attr("href"), trim(article.text(), 35));
        }
    }

}