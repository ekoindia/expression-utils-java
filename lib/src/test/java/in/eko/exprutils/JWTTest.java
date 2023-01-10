package in.eko.exprutils;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JWTTest {

    @Test
    void generate() {
        // Generate JWT for testing...
        String secret = "abc123";
        String token = JWT.generate(secret, "HMAC256", "{'devkey':'1234'}", "issuer", "aud", null, null, 0);

        DecodedJWT jwt = null;

        // Test with correct secret...
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).build();
            jwt = verifier.verify(token);
        } catch (JWTVerificationException e) {
            System.out.println("ERROR: " + e);
            assertTrue(false);
        }
        assertNotNull(jwt);
        assertEquals("issuer", jwt.getIssuer());
        assertEquals("[aud]", jwt.getAudience().toString());
        assertEquals("1234", jwt.getClaim("devkey").asString());

        // Wrong secret: should not validate
        try {
            Algorithm algorithm = Algorithm.HMAC256("wrong-secret");
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).build();
            jwt = verifier.verify(token);
            assertNull(jwt);
        } catch (JWTVerificationException e) {
            assertTrue(true);
        }
    }

    @Test
    void parse() {
        // Generate JWT for testing...
        String secret = "abc123";
        String token = JWT.generate(secret, "HS256", "{'devkey':'1234'}", "issuer", "aud", null, null, 0);

        Map<String, Claim> claims = JWT.parse(token, secret, "HS256");
        assertEquals("1234", claims.get("devkey").asString());
    }
}
