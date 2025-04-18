package com.cwj.hotel.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;

    @NotNull(message = "登录用户名不能为空！")
    private String username;

    @NotNull(message = "登录密码不能为空！")
    private String password;

    @NotNull(message = "真实姓名不能为空！")
    private String realName;

    @NotNull(message = "用户性别用户性别不能为空！")
    private String sex;

    private Integer status; // 用户状态

    private String email; // 邮箱',
    private String userIcon; // 用户头像
    private Long roleId; // 角色编号
}
