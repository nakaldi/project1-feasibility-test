import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HelloWorldTest {

    @Test
    void test() {
        final List<String> strings = new ArrayList<>();
        strings.add("JJICK!!!!0");
        strings.add("JJICK!!!!1");

        assertEquals("JJICK!!!!1", strings.get(1));
        try {
            URL url = new URL("https://www.youtube.com/watch?v=MiWz9bF4WQY");
            Map<String, String> queryMap = UrlUtils.getQueryMap(url.getQuery());
            System.out.println(queryMap.get("v"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}