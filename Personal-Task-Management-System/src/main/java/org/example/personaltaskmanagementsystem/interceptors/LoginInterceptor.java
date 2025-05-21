package org.example.personaltaskmanagementsystem.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.personaltaskmanagementsystem.utils.JwtUtil;
import org.example.personaltaskmanagementsystem.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

// 登录拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌校验验证
        String token = request.getHeader("Authorization");
        //验证
        try{
            //从redis中获取令牌信息
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if (redisToken == null){
                //令牌过期
                throw new RuntimeException();
            }
            Map<String,Object> claims = JwtUtil.parseToken(token);

            //将解析出来的令牌信息存储到ThreadLocal中将用户信息存入ThreadLocal
            ThreadLocalUtil.set(claims);

            return true;
        }catch (Exception e){
            //验证失败，响应401状态码,拦截
            response.setStatus(401);
            return false;
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户信息，防止内存泄漏
        ThreadLocalUtil.remove();
    }
}
