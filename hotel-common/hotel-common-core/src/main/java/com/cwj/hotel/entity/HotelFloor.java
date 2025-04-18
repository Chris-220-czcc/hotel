package com.cwj.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "酒店楼层表")
@TableName("hotel_floor")
public class HotelFloor implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "floorNo", description = "楼层编号")
    @TableField("floor_no")
    private Integer floorNo; // 楼层编号

    @Schema(name = "floorName", description = "楼层名称")
    @TableField("floor_name")
    private String floorName; // 楼层名称
}
