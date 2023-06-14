package com.admin4j.framework.security.jwt;

import com.admin4j.framework.security.UserTokenService;
import com.admin4j.framework.security.properties.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户令牌服务
 *
 * @author andanyang
 * @since 2023/5/30 13:27
 */
@RequiredArgsConstructor
public class JwtUserTokenService implements UserTokenService {


    final JwtProperties jwtProperties;
    final JwtUserDetailsService jwtUserDetailsService;
    static final String FILED_USER_ID = "userID";
    static final String FILED_SALT = "salt";
    static final String FILED_AUTH_TYPE = "authType";

    /**
     * 创建令牌
     *
     * @param claims 用户信息
     * @param salt   jwt salt
     * @return 令牌
     */
    //@Override
    protected String createToken(Map<String, String> claims, String salt) {

        long expiration = System.currentTimeMillis() + jwtProperties.getExpires();
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret() + salt);

        JWTCreator.Builder builder = JWT.create();
        for (Map.Entry<String, String> entry : claims.entrySet()) {
            builder.withClaim(entry.getKey(), entry.getValue());
        }
        return builder.withExpiresAt(new Date(expiration))
                .sign(algorithm);
    }

    @Override
    public String createToken(JwtUserDetails userDetails) {

        Map<String, String> claims = new HashMap<>();
        claims.put(FILED_USER_ID, String.valueOf(userDetails.getUserId()));
        claims.put(FILED_SALT, userDetails.getJwtSalt());
        if (StringUtils.isNotBlank(userDetails.getAuthType())) {
            claims.put(FILED_AUTH_TYPE, userDetails.getAuthType());
        }
        String secret = jwtProperties.getSecret();
        secret += "&" + (userDetails).getJwtSalt();

        return createToken(claims, secret);
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
//    protected Claims parseToken(String token, String secret) {
//        return Jwts.parser()
//                .setSigningKey(secret.getBytes())
//                .parseClaimsJws(token)
//                .getBody();
//    }

    /**
     * 获取请求token(令牌)
     *
     * @param request
     * @return token
     */
    @Override
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getHeader());
        if (StringUtils.isNotEmpty(token) && token.startsWith(jwtProperties.getTokenPrefix())) {
            token = token.replace(jwtProperties.getTokenPrefix(), "");
        }
        return token;
    }

    /**
     * SignatureVerificationException
     *
     * @param token token
     * @return SignatureVerificationException
     */
    @Override
    public UserDetails getUserDetails(String token) {

        DecodedJWT jwt = JWT.decode(token);
        String userId = jwt.getClaim(FILED_USER_ID).asString();
        String secret = jwt.getClaim(FILED_SALT).asString();
        secret = jwtProperties.getSecret() + secret;

        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        verifier.verify(token);

        return jwtUserDetailsService.loadUserByUserId(Long.valueOf(userId));
    }
}
