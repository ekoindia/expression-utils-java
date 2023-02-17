package in.eko.exprutils;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class JWT {

    /**
     * Generate JWT (token) with the given configuration.
     * Using java-jwt library (https://github.com/auth0/java-jwt)
     * @param secretKey The secure key used to encrypt the payload (if an encryption algorithm is selected)
     * @param algo The encryption algorithm to use (defaults to NONE). Currently supported: HS256, HS384, HS512, NONE
     * @param data The additional claim (key-value pairs) as serialized JSON object (String).
     * @param issuer The token issuer
     * @param audience The intended audience of the token
     * @param subject An optional subject for the token
     * @param jwtid An optional JWT-ID
     * @param expiresInSeconds Number of seconds after which the token expires
     * @return The generated token
     * @throws IllegalArgumentException if `data` is not a valid JSON Object
     */
    public static String generate(String secretKey, String algo, String data, String issuer, String audience, String subject, String jwtid, long expiresInSeconds) {
        try {
            // Parse JSON Object from String
            JSONObject dataJson = null;

            if (data != null) {
                dataJson = new JSONObject(data);
            }

            // Get JWT
            return generate(secretKey, algo, dataJson, issuer, audience, subject, jwtid, expiresInSeconds);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid JSON: " + e);
        }
    }


    /**
     * Generate JWT (token) with the given configuration.
     * Using java-jwt library (https://github.com/auth0/java-jwt)
     * @param secretKey The secure key used to encrypt the payload (if an encryption algorithm is selected)
     * @param algo The encryption algorithm to use (defaults to NONE). Currently supported: HMAC256, HMAC384, HMAC512, NONE
     * @param data The JSONObject with key-value pairs of the additional claim/data.
     * @param issuer The token issuer
     * @param audience The intended audience of the token
     * @param subject An optional subject for the token
     * @param jwtid An optional JWT-ID
     * @param expiresInSeconds Number of seconds after which the token expires
     * @return The generated token
     */
    public static String generate(String secretKey, String algo, JSONObject data, String issuer, String audience, String subject, String jwtid, long expiresInSeconds) {
        JWTCreator.Builder jwtBuilder = com.auth0.jwt.JWT.create();

        if (issuer != null && !issuer.equals("")) {
            jwtBuilder.withIssuer(issuer);
        }

        if (audience != null && !audience.equals("")) {
            jwtBuilder.withAudience(audience);
        }

        if (subject != null && !subject.equals("")) {
            jwtBuilder.withSubject(subject);
        }

        if (jwtid != null && !jwtid.equals("")) {
            jwtBuilder.withJWTId(jwtid);
        }

        Date expiryDate = null;
        Date now = Calendar.getInstance().getTime();
        if (expiresInSeconds > 0) {
            long nowMillis = now.getTime();
            long expMillis = nowMillis + expiresInSeconds * 1000;
            expiryDate = new Date(nowMillis + expMillis);
            jwtBuilder.withExpiresAt(expiryDate);
        }

        jwtBuilder.withIssuedAt(now);

        if (data != null) {
            Iterator i = data.keys();
            while (i.hasNext()) {
                String key = (String) i.next();
                Object value = data.get(key);
                if (value instanceof Number) {
                    jwtBuilder.withClaim(key, new Double(value.toString()));
                } else {
                    jwtBuilder.withClaim(key, value.toString());
                }
            }
        }

        Algorithm algorithm = null;
        switch (algo.toUpperCase()) {
            case "HS256":
            case "HMAC256":
                algorithm = Algorithm.HMAC256(secretKey);
                break;
            case "HS384":
            case "HMAC384":
                algorithm = Algorithm.HMAC384(secretKey);
                break;
            case "HS512":
            case "HMAC512":
                algorithm = Algorithm.HMAC512(secretKey);
                break;
            // case "NONE":
            default:
                algorithm = Algorithm.none();
        }

        String token = jwtBuilder.sign(algorithm);
        return token;
    }


    /**
     * Validates and parses a JWT (token). If valid, returns a Map of key-value pairs of the claims.
     * @param token The JWT to parse.
     * @param secretKey The secure key that was used to encrypt the token
     * @param algo The encryption algorithm used to generate the token
     * @return  The Map of claim (payload) key-value pairs.
     * @throws JWTVerificationException if the token cannot be verified
     */
    public static Map<String, Claim> parse(String token, String secretKey, String algo) throws JWTVerificationException {
        DecodedJWT jwt;
        Algorithm algorithm = null;
        switch (algo.toUpperCase()) {
            case "HS256":
            case "HMAC256":
                algorithm = Algorithm.HMAC256(secretKey);
                break;
            case "HS384":
            case "HMAC384":
                algorithm = Algorithm.HMAC384(secretKey);
                break;
            case "HS512":
            case "HMAC512":
                algorithm = Algorithm.HMAC512(secretKey);
                break;
            // case "NONE":
            default:
                algorithm = Algorithm.none();
        }
        JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).build();
        jwt = verifier.verify(token);
        return jwt.getClaims();
    }

}
