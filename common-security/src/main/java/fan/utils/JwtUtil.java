package fan.utils;

import cn.hutool.crypto.asymmetric.RSA;
import fan.consts.AuthConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

/**
 * JWT工具类
 *
 * @author Fan
 * @since 2022/11/26 21:28
 */
public class JwtUtil {

    private final static RSA rsa = new RSA();

    /**
     * 生成JWT
     *
     * @param userId 用户ID
     * @return {@link String}
     * @author Fan
     * @since 2022/11/26 21:30
     */
    public static String generateJwt(String userId) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) rsa.getPrivateKey();

        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId)
                .setIssuedAt(new Date())
                // 设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + AuthConst.TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.RS256, rsaPrivateKey)
                .compact();

        LogUtil.info("生成JWT: " + jwt);
        return jwt;
    }

    /**
     * 解析JWT
     *
     * @param jwt JWT
     * @return {@link Jws<Claims>}
     * @author Fan
     * @since 2022/11/26 21:50
     */
    public static Jws<Claims> parseJwt(String jwt) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) rsa.getPublicKey();

        try {
            return Jwts.parser().setSigningKey(rsaPublicKey).parseClaimsJws(jwt);
        } catch (Exception e) {
            LogUtil.error("解析 JWT 失败");
            return null;
        }
    }
}
