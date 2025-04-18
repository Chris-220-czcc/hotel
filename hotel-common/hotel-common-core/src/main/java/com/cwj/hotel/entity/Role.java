package com.cwj.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_role")
public class Role implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name; // 角色名称
    private String code; // 角色编号
    private Integer age;
}
