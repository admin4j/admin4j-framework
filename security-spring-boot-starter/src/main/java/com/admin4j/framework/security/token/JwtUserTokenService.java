package com.admin4j.framework.security.token;

import com.admin4j.framework.security.JwtUserDetails;
import com.admin4j.framework.security.JwtUserDetailsService;
import com.admin4j.framework.security.UserTokenService;
import com.admin4j.framework.security.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 创建令牌
     *
     * @param claims 用户信息
     * @return 令牌
     */
    //@Override
    protected String createToken(Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret()).compact();

    }

    @Override
    public String createToken(JwtUserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(FILED_USER_ID, userDetails.getUserId());
        String secret = jwtProperties.getSecret();
        secret += "&" + (userDetails).getJwtSalt();

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    protected Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

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

    @Override
    public UserDetails getUserDetails(String token) {
        Claims claims = parseToken(token);
        Long userId = (Long) claims.get(FILED_USER_ID);
        return jwtUserDetailsService.loadUserByUserId(userId);
    }
}
