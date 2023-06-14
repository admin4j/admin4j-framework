package com.admin4j.framework.security.jwt;

import com.admin4j.framework.security.properties.JwtProperties;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;

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


        JwtUserDetails jwtUserDetails = new TestJwtUserDetails();

        JwtProperties jwtProperties = new JwtProperties();
//        jwtProperties.setExpires(-3600000L);
        JwtUserTokenService jwtUserTokenService = new JwtUserTokenService(jwtProperties, null);


        String token = jwtUserTokenService.createToken(jwtUserDetails);
        System.out.println("token = " + token);

//        token += 1;
        UserDetails userDetails = jwtUserTokenService.getUserDetails(token);
        System.out.println("userDetails = " + userDetails);
    }
}