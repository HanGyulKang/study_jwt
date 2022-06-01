package com.study.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import io.jsonwebtoken.impl.crypto.JwtSigner;
import org.junit.jupiter.api.Test;
import org.keycloak.common.util.PemUtils;
import org.springframework.util.Base64Utils;
import org.yaml.snakeyaml.util.UriEncoder;

import java.security.*;
import java.util.Base64;
import java.util.Date;

public class JwtRsaTest {

    @Test
    public void testJWTWithRsa() throws NoSuchAlgorithmException {
        String userId = "42914D776E7344C0AFD979BDD710C1F1";


        String lowPrivateKey = """
                MIICXAIBAAKBgQDMFZxa3z6J1xG5g+wOaxc/8QXfLqHjSVtRkbk3uxZha2TKn4ts
                UkpTyeNYGaCK1FD8RjgwgsPS0pdL8dFrRE1NdlIKQGtOJmhYba1CKQU+y5Dlm4Cj
                xwYaJj2qkZMmkVKJLArYnlhmywRSRWdBHZUlhgRN6CRlrxPza8oVeNWsWQIDAQAB
                AoGACykjY9TRlrgsP/j279LrcpO7vnjOYZ+hXtSZTHLuxmUZHfubpFDbv7lKtYax
                QOZbh3BPoleEXgMmQwD2cudJUtoD73Se0b7F4wrC9CT57B0j3/5YcHcmrUsmSxg0
                87TQILpKc2q6S4FgvdE6/nL5spnS39KcVQr0dT7QL4p+2AECQQD9PzeaohLXC3+3
                55oW0apXDMa1wNKxKtA7wk2bsBZ55dr4yEdwlAhiABvXMIgiTrys3qEIrqHS0nzt
                ff4wCPxHAkEAzk2TLejG6RpuYMW8E3uAhajlmJNUiw+K7ad9EU/tG7jbK5q7GDda
                Jdsgk0x1srFwmMGOxE5ce5O7+VZ+hXyCXwJAT6j+Dacjs/9K+odgwgLLyTwcmXmB
                DPfBrrqM/Qcj2s5B1fXx+uilxmXwW/1JJTmNQfd5wihkgNNzNqVmOqfo6wJAOBxv
                WIBEgag3t0hwxBfGOBRdBiekSgJbNhJk+O11PyIDMpPuKUL82OkFriqQpJ6QUmvq
                ycfKba4b5VA6L1JTHwJBAIDlqKJfOPq4qzO979fx52zXIbKLqtx2W8IDEV7tjfBI
                hgL6pSzSfxyt/yyDGzYwPCzxoBgb1t5NCk8QgpliNQ0=
                """;

        String lowPublicKey = """
                MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMFZxa3z6J1xG5g+wOaxc/8QXf
                LqHjSVtRkbk3uxZha2TKn4tsUkpTyeNYGaCK1FD8RjgwgsPS0pdL8dFrRE1NdlIK
                QGtOJmhYba1CKQU+y5Dlm4CjxwYaJj2qkZMmkVKJLArYnlhmywRSRWdBHZUlhgRN
                6CRlrxPza8oVeNWsWQIDAQAB
                """;

        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(1024);

        KeyPair kp2 = keyGenerator.genKeyPair();
        PublicKey publicKey = PemUtils.decodePublicKey(lowPublicKey);
        PrivateKey privateKey = PemUtils.decodePrivateKey(lowPrivateKey);

        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//        System.out.println(encodedPublicKey);
//        System.out.println(convertToPublicKey(encodedPublicKey));

        String token = generateJwtToken(privateKey, userId);
//        System.out.println("TOKEN : " + token);


        printStructure(token, publicKey);

//        System.out.println("TOKEN:");
//        System.out.println(token);
//        printStructure(token, publicKey);

//        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//        System.out.println("Public Key:");
//        System.out.println(convertToPublicKey(encodedPublicKey));
//        String token = generateJwtToken(privateKey);
//        System.out.println("TOKEN:");
//        System.out.println(token);
//        printStructure(token, publicKey);

    }


    @SuppressWarnings("deprecation")
    public String generateJwtToken(PrivateKey privateKey, String userId) {
        String token = Jwts.builder().setSubject("test")
                .setExpiration(new Date(2022, 6, 3))
                .setIssuer("test@test.com")
                .claim("userId", userId)
                .claim("userNumber", "2698501299910")
                // RS256 with privateKey
                .signWith(SignatureAlgorithm.RS256, privateKey).compact();
        return token;
    }

    //Print structure of JWT
    public void printStructure(String token, PublicKey publicKey) {
        // JWT TOKEN DECODING - 1
        Jws parseClaimsJws = Jwts.parser().setSigningKey(publicKey)
                .parseClaimsJws(token);

//        System.out.println("Header     : " + parseClaimsJws.getHeader());
//        System.out.println("Body       : " + parseClaimsJws.getBody());
//        System.out.println("Signature  : " + parseClaimsJws.getSignature());

        // JWT TOKEN DECODING - 2
        DecodedJWT decodedJWT = JWT.decode(token);
        System.out.println(decodedJWT.getClaims());

        JWTVerifier jwtVerifier = new JWTVerifier() {
            @Override
            public DecodedJWT verify(String token) throws JWTVerificationException {
                return JWT.decode(token);
            }

            @Override
            public DecodedJWT verify(DecodedJWT jwt) throws JWTVerificationException {
                return null;
            }
        };


//        System.out.println("Header      : " + decodedJWT.getHeader());
//        System.out.println("Payload     : " + decodedJWT.getPayload());
//        System.out.println("Signature   : " + decodedJWT.getSignature());


        String Header = decodedJWT.getHeader();
        String Payload = decodedJWT.getPayload();
        String Signature = decodedJWT.getSignature();
        System.out.println(jwtVerifier.verify(token));



    }


    // Add BEGIN and END comments
    private String convertToPublicKey(String key){
        StringBuilder result = new StringBuilder();
//        result.append("-----BEGIN PUBLIC KEY-----\n");
        result.append(key);
//        result.append("\n-----END PUBLIC KEY-----");
        return result.toString();
    }
}
