package com.cwj.hotel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cwj.hotel.entity.HotelReserve;
import com.cwj.hotel.service.ReserveService;
import com.cwj.hotel.utils.PageUtil;
import com.cwj.hotel.utils.Result;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/reserve")
public class ReserveController {
    @Resource
    private ReserveService reserveService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/list")
    public Result<Object> list(
            @RequestParam(value = "pageIndex", defaultValue = "1") Long pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize,
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(value = "status", defaultValue = "5") Integer status
    ) {
        QueryWrapper<HotelReserve> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.hasLength(searchValue), "order_number", searchValue);
        if (status != 5) wrapper.eq("status", status);
        Page<HotelReserve> page = new Page<>(pageIndex, pageSize);
        reserveService.page(page, wrapper);
        PageUtil<HotelReserve> pageUtil = new PageUtil<>(page.getCurrent(), page.getTotal(), page.getSize(), page.getRecords());
        return Result.ok().message("成功查询预定订单").data(pageUtil);
    }

    @PostMapping("/add")
    public Result<Object> add(@RequestBody HotelReserve hotelReserve) {
        hotelReserve.setStatus(0);
        hotelReserve.setCheckinDate(LocalDate.now());
        hotelReserve.setCheckoutDate(LocalDate.now().plusDays(hotelReserve.getReserveDays()));
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String orderNumber = now.format(dateTimeFormatter) + "-" + UUID.randomUUID().toString();
        hotelReserve.setOrderNumber(orderNumber);
        boolean save = reserveService.save(hotelReserve);
        if (!save) return Result.fail().message("创建订单失败");
        //自定义延迟消息
        Map<String, String> map = new HashMap<>();
        map.put("orderNumber", orderNumber);
        map.put("delayTime", String.valueOf(System.currentTimeMillis() + 5000));
        rocketMQTemplate.syncSend("reserve-add", map);
        return Result.ok().message("创建订单成功，等待支付");
    }

    @DeleteMapping("/delete")
    public Result<Object> delete(@RequestParam String orderNumber) {
        boolean removed = reserveService.removeByOrderNumber(orderNumber);
        if (!removed) return Result.fail().message("删除订单失败");
        return Result.ok().message("删除订单成功");
    }
}