package com.cwj.hotel.feign;

import com.cwj.hotel.entity.HotelFloor;
import com.cwj.hotel.vo.FloorVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "hotel-floor-service",path = "/floor")
public interface FloorService {
    @GetMapping("/all")
    public List<FloorVo> all();
    @PutMapping("/addfloor")
    public FloorVo addFloor(@RequestBody HotelFloor floor);
}
