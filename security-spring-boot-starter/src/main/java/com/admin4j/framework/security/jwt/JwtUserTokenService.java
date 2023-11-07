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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 用户令牌服务
 *
 * @author andanyang
 * @since 2023/5/30 13:27
 */
@RequiredArgsConstructor
public class JwtUserTokenService implements UserTokenService {


    static final String FILED_USER_ID = "userID";
    static final String FILED_SALT = "salt";
    static final String FILED_AUTH_TYPE = "authType";
    final JwtProperties jwtProperties;
    final JwtUserDetailsService jwtUserDetailsService;

    /**
     * 创建令牌
     *
     * @param userDetails 用户信息
     * @return 令牌
     */
    @Override
    public String createToken(JwtUserDetails userDetails) {

        long expiration = System.currentTimeMillis() + jwtProperties.getExpires();
        Algorithm algorithm = Algorithm.HMAC256(generateSecret((userDetails).getJwtSalt()));
        JWTCreator.Builder builder = JWT.create();

        builder.withClaim(FILED_USER_ID, userDetails.getUserId());
        builder.withClaim(FILED_SALT, userDetails.getJwtSalt());

        if (StringUtils.isNotBlank(userDetails.getAuthType())) {
            builder.withClaim(FILED_AUTH_TYPE, userDetails.getAuthType());
        }

        return builder.withExpiresAt(new Date(expiration))
                .sign(algorithm);
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
    public JwtUserDetails getUserDetails(String token) {

        DecodedJWT jwt = JWT.decode(token);
        Long userId = jwt.getClaim(FILED_USER_ID).asLong();
        String secret = jwt.getClaim(FILED_SALT).asString();
        secret = generateSecret(secret);

        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        verifier.verify(token);

        // 可以验证下 salt 是否有效，用来验证令牌是否已注销
        JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUserId(userId);
        if (userDetails != null) {
            if (!jwtUserDetailsService.verifySalt(userDetails, jwt.getClaim(FILED_SALT).asString())) {
                return null;
            }
        }
        return userDetails;
    }

    protected String generateSecret(String salt) {
        return jwtProperties.getSecret() + "#" + salt;
    }
}
