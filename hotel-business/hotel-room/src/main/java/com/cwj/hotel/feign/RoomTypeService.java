package com.cwj.hotel.feign;

import com.cwj.hotel.entity.HotelRoomType;
import com.cwj.hotel.vo.RoomTypeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "hotel-type-service",path = "/roomtype")
public interface RoomTypeService {
    @GetMapping("/all")
    public List<RoomTypeVo> getRoomType() ;
    @PutMapping("/addtype")
    public void addType(@RequestBody HotelRoomType hotelRoomType);
}
