package com.cwj.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 会员表(HotelMember)实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("hotel_member")
public class HotelMember implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String nickname; // 昵称
    @TableField("real_name")
    private String realName; // 真实姓名
    private String phone; // 手机号
    private String email; // 邮箱
    private String password; // 登录密码
    @TableField("login_times")
    private int loginTimes; // 登录次数
    private int status; // 会员状态
    @TableField("member_icon")
    private String memberIcon; // 会员头像
    private Integer balance; // 余额
}
