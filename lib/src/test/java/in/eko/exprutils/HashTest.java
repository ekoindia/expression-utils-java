package in.eko.exprutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class HashTest {
    @Test
    public void testHashes() throws Exception {
        // Test SHA-512 hash
        assertEquals("309ecc489c12d6eb4cc40f50c902f2b4d0ed77ee5" +
                        "11a7c7a9bcd3ca86d4cd86f989dd35bc5ff499670da34255b45b0cf" +
                        "d830e81f605dcf7dc5542e93ae9cd76f",
                Hash.sha512("hello world")
        );

        // Test SHA-256 hash
        assertEquals("b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9",
                Hash.sha256("hello world")
        );

        // Test MD5 hash
        assertEquals("5eb63bbbe01eeed093cb22bb8f5acdc3",
                Hash.md5("hello world")
        );
    }
}
