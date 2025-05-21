package org.example.personaltaskmanagementsystem.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

/**
 * JwtUtil 类用于生成和解析 JSON Web Token (JWT)。
 * 提供了生成 JWT 令牌和验证并解析 JWT 令牌的静态方法。
 */
public class JwtUtil {

    /**
     * 用于签名和验证 JWT 的密钥。
     * 实际生产环境中应使用更安全且复杂的密钥，并妥善保管。
     */
    private static final String KEY = "jiayuhan";
	
    /**
     * 接收业务数据，生成 JWT 令牌并返回。
     *
     * @param claims 包含业务数据的 Map，这些数据将被封装在 JWT 的 payload 中。
     * @return 生成的 JWT 令牌字符串。
     */
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                // 将业务数据以 "claims" 为键添加到 JWT 的 payload 中
                .withClaim("claims", claims)
                // 将 claim 转换为 Map 类型
                // 获取名为 "claims" 的 claim
                // 验证传入的 JWT 令牌
                // 构建 JWT 验证器
    /**
     * 接收 JWT 令牌，验证令牌的有效性，并返回封装在其中的业务数据。
     *
     * @param token 待验证和解析的 JWT 令牌字符串。
     * @return 解析出的包含业务数据的 Map，如果令牌无效则会抛出异常。
     */
                // 使用 HMAC256 算法对 JWT 进行签名
                // 设置 JWT 的过期时间为当前时间往后 12 小时
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .sign(Algorithm.HMAC256(KEY));
    }

	//接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

}
