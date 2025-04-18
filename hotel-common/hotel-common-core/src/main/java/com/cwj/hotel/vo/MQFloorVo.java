package com.cwj.hotel.vo;

import com.cwj.hotel.entity.HotelFloor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MQFloorVo {
    private HotelFloor hotelFloor;
    private Long time;
}
