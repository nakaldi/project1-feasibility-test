import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Example program to list links from a URL.
 */
public class ListLinks {
    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 0, "usage: delete url to fetch");

        Scanner sc = new Scanner(System.in);
        System.out.print(">>");
        int input = sc.nextInt();

        long startTime = System.currentTimeMillis();

        switch (input) {
            case 1:
                Document doc1 = Jsoup.connect("https://search.naver.com/search.naver?where=news&query=%EB%B6%81%ED%95%9C&sm=tab_srt&sort=1&photo=0&field=0&reporter_article=&pd=0&ds=&de=&docid=&nso=so%3Add%2Cp%3Aall%2Ca%3Aall&mynews=0&refresh_start=0&related=0")
                        .get();
                parseNaver(doc1);
                break;
            case 2:
                Document doc2 = Jsoup.connect("https://www.youtube.com/user/spotv/search?query=%ED%86%A0%ED%8A%B8%EB%84%98")
                        .get();
                Elements contents = doc2.select("div ytd-item-section-renderer");
                System.out.println(contents);
                print("%s", doc2.html());
                break;
            case 3:
                Document doc3 = Jsoup.connect("https://www.instagram.com/explore/tags/%ED%86%A0%ED%8A%B8%EB%84%98/")
                        .get();
                Elements instagrams = doc3.select("div");
                System.out.println(instagrams);
                print("%s", doc3.html());
                break;
            case 4:
                Document doc4 = Jsoup.connect("https://twitter.com/search?q=%EC%86%90%ED%9D%A5%EB%AF%BC&src=typed_query&f=live")
                    .get();
                Elements twitters = doc4.select("section.css-1dbjc4n div div");
                System.out.println(twitters);
                print("%s", doc4.html());
                break;
            case 5:
                Document doc5 = Jsoup.connect("https://search.daum.net/search?w=news&sort=recency&q=%EB%B6%81%ED%95%9C&cluster=n&DA=STC&dc=STC&pg=1&r=1&p=1&rc=1&at=more&sd=&ed=&period=")
                        .get();
                parseDaum(doc5);
                break;
            case 6:
                Connection.Response seoulSearchResponse = Jsoup.connect("https://www.seoul.go.kr/realmnews/in/list.do")
                        .timeout(10 * 1000)
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
                break;
            case 7:
                try {
                    String URL = "https://twitter.com/hashtag/손흥민";
                    runSelenium(URL);
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
        long finishTime = System.currentTimeMillis();
        System.out.println(finishTime - startTime + "ms");




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


    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }

    private static void parseNaver(Document doc) {
        Elements articles = doc.select("ul.type01 li");

        print("\nArticles: (%d)", articles.size());
        for (Element article : articles) {
            print(" * a: <%s>  (%s)", article.select("a[href]").attr("href"), trim(article.text(), 35));
        }
    }

    private static void parseDaum(Document doc) {
        Elements articles = doc.select("#newsResultUL li");

        print("\nArticles: (%d)", articles.size());
        for (Element article : articles) {
            print(" * a: <%s>  (%s)", article.select("a[href]").attr("href"), trim(article.text(), 35));
        }
    }

    public static void runSelenium(String URL) throws Exception {
        // 1. WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", "D:/programming/WebDriver/bin/chromedriver.exe");

        // 2. WebDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");          // 최대크기로
        options.addArguments("--headless");                 // Browser를 띄우지 않음
        options.addArguments("--disable-gpu");              // GPU를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
        options.addArguments("--no-sandbox");               // Sandbox 프로세스를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.

        // 3. WebDriver 객체 생성
        ChromeDriver driver = new ChromeDriver(options);

        // 4. 웹페이지 요청
        driver.get(URL);

        try {
            // 6. 트윗 목록 Block 조회, 로드될 때까지 최대 30초간 대기
            WebDriverWait wait = new WebDriverWait(driver, 30);
            WebElement parent = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("section[aria-labelledby*=\"accessible-list\"]")));

            // 7. 트윗 콘텐츠 조회
            List<WebElement> contents = parent.findElements(By.cssSelector("div.css-1dbjc4n.r-my5ep6.r-qklmqi.r-1adg3ll"));
            System.out.println("조회된 콘텐츠 수 : " + contents.size());

            if (contents.size() > 0) {
                // 8. 트윗 상세 내용 탐색
                for (WebElement content : contents) {
                    try {
                        String username = content.findElement(By.cssSelector("span > span.css-901oao.css-16my406.r-1qd0xha.r-ad9z0x.r-bcqeeo.r-qvutc0")).getText();
                        String id = content.findElement(By.cssSelector("span.css-901oao.css-16my406.r-1qd0xha.r-ad9z0x.r-bcqeeo.r-qvutc0")).getText();
                        String text = content.findElement(By.cssSelector("div.css-901oao.r-hkyrab.r-1qd0xha.r-a023e6.r-16dba41.r-ad9z0x.r-bcqeeo.r-bnwqim.r-qvutc0")).getText();

                        System.out.println(username + " " + id);
                        System.out.println(text);
                        System.out.println("========================");
                    } catch (NoSuchElementException e) {
                        // pass
                    }
                }
            }
        } catch (TimeoutException e) {
            System.out.println("목록을 찾을 수 없습니다.");
        } finally {
            //소스 출력
            //System.out.println(driver.getPageSource());
        }
        // WebDriver 종료
        driver.quit();
    }

}