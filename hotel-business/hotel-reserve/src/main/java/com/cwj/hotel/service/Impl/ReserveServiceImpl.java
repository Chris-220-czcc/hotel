package com.cwj.hotel.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwj.hotel.entity.HotelReserve;
import com.cwj.hotel.mapper.ReserveMapper;
import com.cwj.hotel.service.ReserveService;
import org.springframework.stereotype.Service;

@Service
public class ReserveServiceImpl extends ServiceImpl<ReserveMapper, HotelReserve> implements ReserveService {

    @Override
    public boolean removeByOrderNumber(String orderNumber) {
        return remove(new QueryWrapper<HotelReserve>().eq("order_number", orderNumber));
    }
}
