package com.cwj.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cwj.hotel.entity.HotelReserve;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public interface ReserveService extends IService<HotelReserve> {

    boolean removeByOrderNumber(String orderNumber);
}