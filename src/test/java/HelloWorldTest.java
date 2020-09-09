import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HelloWorldTest {

    @Test
    void test() {
        final List<String> strings = new ArrayList<>();
        strings.add("JJICK!!!!0");
        strings.add("JJICK!!!!1");

        assertEquals("JJICK!!!!1", strings.get(1));
    }
}