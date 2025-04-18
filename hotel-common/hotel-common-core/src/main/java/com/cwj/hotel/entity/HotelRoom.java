package com.cwj.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "hotel_room",description = "酒店房间表")
@TableName("hotel_room")
public class HotelRoom implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "roomName", description = "房间名称")
    @TableField("room_name")
    private String roomName; // 房间名称

    @Schema(name = "roomNumber", description = "房间号")
    @TableField("room_number")
    private String roomNumber; // 房间号

    @Schema(name = "bedType", description = "床型")
    @TableField("bed_type")
    private String bedType; // 床型

    @Schema(name = "broadband", description = "宽带")
    private String broadband; // 宽带

    @Schema(name = "standardPrice", description = "标准价")
    @TableField("standard_price")
    private Integer standardPrice; // 标准价

    @Schema(name = "memberPrice", description = "会员价")
    @TableField("member_price")
    private Integer memberPrice; // 会员价

    @Schema(name = "roomStatus", description = "房间状态")
    @TableField("room_status")
    private int roomStatus; // 房间状态

    @Schema(name = "roomTypeId", description = "房间类型编号")
    @TableField("room_type_id")
    private Long roomTypeId; // 房间类型编号

    @Schema(name = "floorId", description = "楼层编号")
    @TableField("floor_id")
    private Long floorId; // 楼层编号

    @Schema(name = "coverImg", description = "房间封面图")
    @TableField("cover_img")
    private String coverImg; // 房间封面图

    @TableField(exist = false)
    private HotelRoomType hotelRoomType; // 房间类型，这个字段不会出现在数据库中
    @TableField(exist = false)
    private HotelFloor hotelFloor; // 房间楼层，这个字段不会出现在数据库中
}
