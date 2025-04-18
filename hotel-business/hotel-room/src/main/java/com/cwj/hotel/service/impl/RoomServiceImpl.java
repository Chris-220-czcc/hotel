package com.cwj.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwj.hotel.entity.HotelRoom;
import com.cwj.hotel.mapper.RoomMapper;
import com.cwj.hotel.service.RoomService;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, HotelRoom> implements RoomService {
}
