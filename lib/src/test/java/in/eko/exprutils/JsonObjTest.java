package in.eko.exprutils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonObjTest {

    String obj = "{'a':1, 'b':{'c':2, 'd':{'e': 3}}}";

    @Test
    void get() {
        // Test simple (non-nested) value retrieve
        assertEquals("1",
                JsonObj.get(obj, "a", "0").toString()
        );

        // Test nested (1-level deep) value retrieve
        assertEquals("2",
                JsonObj.get(obj, "b.c", "0").toString()
        );

        // Test nested (2-level deep) value retrieve
        assertEquals("3",
                JsonObj.get(obj, "b.d.e", "0").toString()
        );

        // Test default value return when key does not exist
        assertEquals("-1",
                JsonObj.get(obj, "b.Z.e", "-1").toString()
        );
    }

    @Test
    void set() {
        // Test simple (non-nested) value set
        assertEquals("{\"a\":0}",
                JsonObj.set("{}", "a", 0).toString()
        );

        // Test nested (1-level deep) value set
        assertEquals("{\"a\":{\"hello\":\"world\"}}",
                JsonObj.set("{}", "a.hello", "world").toString()
        );

        // Test nested (2-level deep) value set
        assertEquals("{\"a\":{\"hello\":{\"world\":\"again\"}},\"b\":5}",
                JsonObj.set("{\"b\":5}", "a.hello.world", "again").toString()
        );
    }
}