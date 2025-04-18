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
@Schema(description = "酒店房间类型表")
@TableName("hotel_room_type")
public class HotelRoomType implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "typeName", description = "类型名称")
    @TableField("type_name")
    //@NotBlank(message = "房间类型名称不能为空")
    private String typeName; // 名称

    @Schema(name = "typeSort", description = "类型排序")
    @TableField("type_sort")
    private int typeSort; // 排序

}
