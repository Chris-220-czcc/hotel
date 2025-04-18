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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("hotel_recharge_record")
public class HotelRechargeRecord implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("member_id")
    private Long memberId; // 会员编号
    private Integer money; // 充值金额
}
