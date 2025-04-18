package com.cwj.hotel.vo;

import com.cwj.hotel.entity.HotelRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomTypeWithTokenVo {
    private HotelRoomType hotelRoomType;
    private String token;
}
