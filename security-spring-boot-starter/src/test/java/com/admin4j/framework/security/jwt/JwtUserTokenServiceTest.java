package com.admin4j.framework.security.jwt;

import com.admin4j.framework.security.properties.JwtProperties;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andanyang
 * @since 2023/6/14 10:35
 */
public class JwtUserTokenServiceTest {

    static final String FILED_USER_ID = "userID";
    static final String FILED_AUTH_TYPE = "authType";
    static final String FILED_SALT = "salt";

    @Test
    public void testCreateToken() {

        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setExpires(-3600000L);
        JwtUserTokenService jwtUserTokenService = new JwtUserTokenService(jwtProperties, null);

        String salt = "12";
        Map<String, String> claims = new HashMap<>();
        claims.put(FILED_USER_ID, String.valueOf(1));
        if (StringUtils.isNotBlank("")) {
            claims.put(FILED_AUTH_TYPE, "");
        }
        claims.put(FILED_SALT, salt);
//        String secret = jwtProperties.getSecret();
//        secret += "" + salt;

        String token = jwtUserTokenService.createToken(claims, salt);
        System.out.println("token = " + token);

//        token += 1;
        UserDetails userDetails = jwtUserTokenService.getUserDetails(token);
        System.out.println("userDetails = " + userDetails);
    }
}