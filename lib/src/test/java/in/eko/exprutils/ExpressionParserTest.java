package in.eko.exprutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


public class ExpressionParserTest {
    @Test
    public void testParseExpression() throws Exception {
        // Test basic arithmetic operations
        assertEquals(5.0, (Double)ExpressionParser.parseExpression("['+', 2, 3]"), 1e-6);
        assertEquals(-1.0, (Double)ExpressionParser.parseExpression("['-', 2, 3]"), 1e-6);
        assertEquals(6.0, (Double)ExpressionParser.parseExpression("['*', 2, 3]"), 1e-6);
        assertEquals(2.0, (Double)ExpressionParser.parseExpression("['/', 6, 3]"), 1e-6);

        // Test nested expressions
        assertEquals(14.0, (Double)ExpressionParser.parseExpression("['+', 2, ['*', 3, 4]]"), 1e-6);
        assertEquals(0.416, (Double)ExpressionParser.parseExpression("['/', ['+', 2, 3], ['*', 3, 4]]"), 0.01);

        // Test invalid operator
        try {
            ExpressionParser.parseExpression("['UNKNOWN', 2, 3]");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid operator: UNKNOWN", e.getMessage());
        }

        // Test hashes
        assertEquals("309ecc489c12d6eb4cc40f50c902f2b4d0ed77ee5" +
                "11a7c7a9bcd3ca86d4cd86f989dd35bc5ff499670da34255b45b0cf" +
                "d830e81f605dcf7dc5542e93ae9cd76f",
                ExpressionParser.parseExpression("['SHA512', 'hello world']")
        );
        assertEquals("b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9",
                ExpressionParser.parseExpression("['SHA256', 'hello world']")
        );
        assertEquals("5eb63bbbe01eeed093cb22bb8f5acdc3",
                ExpressionParser.parseExpression("['MD5', 'hello world']")
        );

        // Test deep get...
        assertEquals("3",
                ExpressionParser.parseExpression("['GET', {'a':1, 'b':{'c':2, 'd':{'e': 3}}}, 'b.d.e', '0']").toString()
        );
    }
}