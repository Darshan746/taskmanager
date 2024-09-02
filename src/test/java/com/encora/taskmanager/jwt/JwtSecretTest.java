package com.encora.taskmanager.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtSecretTest {

    @Test
    public void generateSecretKey(){
       SecretKey secretKey = Jwts.SIG.HS256.key().build();
        String encodingKey = DatatypeConverter.printHexBinary(secretKey.getEncoded());
        System.out.println(encodingKey);
    }
}
