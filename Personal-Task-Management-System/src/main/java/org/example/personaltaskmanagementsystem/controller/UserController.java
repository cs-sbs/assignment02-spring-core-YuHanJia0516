package org.example.personaltaskmanagementsystem.controller;
import jakarta.validation.constraints.Pattern;
import org.example.personaltaskmanagementsystem.anno.Log;
import org.example.personaltaskmanagementsystem.service.UserService;
import org.example.personaltaskmanagementsystem.utils.JwtUtil;
import org.example.personaltaskmanagementsystem.utils.Md5Util;
import org.example.personaltaskmanagementsystem.pojo.Result;
import org.example.personaltaskmanagementsystem.pojo.User;
import org.example.personaltaskmanagementsystem.utils.ThreadLocalUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Validated// 开启数据校验
@RestController// 返回json数据
@RequestMapping("/user")// 统一请求前缀
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 注册
    @PostMapping("/register")
    public Result register(
            @Pattern(regexp = "^\\S{5,16}$", message = "用户名格式不正确") String username,
            @Pattern(regexp = "^\\S{5,16}$", message = "密码格式不正确") String password) {
        User user = userService.findByUserName(username);
        if (user == null) {
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已存在");
        }
    }

    // 登录
    @PostMapping("/login")
    public Result<String> login(
            @Pattern(regexp = "^\\S{5,16}$", message = "用户名格式不正确") String username,
            @Pattern(regexp = "^\\S{5,16}$", message = "密码格式不正确") String password) {
        //根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        //判断用户是否存在
        if (loginUser == null) {
            return Result.error("用户名错误");
        }
        //判断密码是否正确    加密后再比较
        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            //登录成功
            //生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 30 , TimeUnit.DAYS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }


    //  获取当前用户信息
    @Log
    @GetMapping("/userInfo")
    public Result<User> userInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    //  更新用户信息吗
    @Log
    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }
    //更新用户头像  更新用户头像
    @Log
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    // 更新用户密码
    @Log
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        //校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd)||!StringUtils.hasLength(newPwd)||!StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要参数");
        }
       //原密码是否正确
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUserName(username);
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码错误");
        }
       //新密码和确认密码是否一致
        if(!newPwd.equals(rePwd)){
            return Result.error("两次密码不一致");
        }
        userService.updatePwd(newPwd);
        //删除redis中的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        //返回结果
        return Result.success();
    }

}

