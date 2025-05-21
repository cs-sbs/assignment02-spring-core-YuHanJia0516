package org.example.personaltaskmanagementsystem.service;

import org.example.personaltaskmanagementsystem.pojo.User;

public interface UserService {


    //  根据用户名和密码查询用户
    User findByUserName(String username);


    //  注册用户
    void register(String username, String password);

    //  更新用户信息
    void update(User user);

    // 更新用户头像
    void updateAvatar(String avatarUrl);
    // 更新用户密码
    void updatePwd(String newPwd);
}
