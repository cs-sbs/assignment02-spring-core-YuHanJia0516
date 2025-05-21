package org.example.personaltaskmanagementsystem.pojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotNull// 非空校验
    private Integer id;//主键ID
    private String username;//用户名
    @JsonIgnore//忽略该字段的序列化和反序列化
    private String password;//密码
    @NotEmpty//非空校验
    @Pattern(regexp = "^\\S{5,16}$")//正则校验
    private String nickname;//昵称
    @NotEmpty//非空校验
    @Email//邮箱校验
    private String email;//邮箱
    private String userPic;//用户头像地址
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
