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
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("hotel_checkin")
public class HotelCheckin implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("order_number")
    private String orderNumber; // 订单号
    @TableField("room_id")
    private Long roomId; // 房间编号
    @TableField("member_name")
    private String memberName; // 会员姓名
    @TableField("id_card")
    private String idCard; // 身份证
    @TableField("member_number")
    private int memberNumber; // 入住人数
    private String birthday; // 出生日期
    private String gender; // 性别
    private String province; // 省份
    private String city; // 城市
    private String address; // 家庭住址
    private String phone; // 手机号
    private int status; // 入住状态：0已入住未退房，1已退房
    @TableField("checkin_date")
    private LocalDate checkinDate; // 入住时间
    @TableField("checkout_date")
    private LocalDate checkoutDate; // 退房时间
    @TableField("amount_money")
    private Integer amountMoney; // 金额
}
