package com.cwj.hotel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cwj.hotel.entity.HotelFloor;
import com.cwj.hotel.service.HotelFloorService;
import com.cwj.hotel.utils.Cache;
import com.cwj.hotel.utils.PageUtil;
import com.cwj.hotel.utils.Result;
import com.cwj.hotel.vo.FloorVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/floor")
public class HotelFloorController {
    @Resource
    private HotelFloorService hotelFloorService;
    @GetMapping("/list")
    public Result<Object> list(
            @RequestParam(value = "pageSize",defaultValue = "10") Long pageSize,
            @RequestParam(value = "pageIndex",defaultValue = "1") Long pageIndex,
            @RequestParam(value = "searchValue",defaultValue = "") String searchValue
    ){
        LambdaQueryWrapper<HotelFloor> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StringUtils.hasLength(searchValue), HotelFloor::getFloorNo, searchValue)
                .or()
                .like(StringUtils.hasLength(searchValue), HotelFloor::getFloorName, searchValue);

        Page<HotelFloor> page = new Page<>(pageIndex, pageSize);
        hotelFloorService.page(page, queryWrapper);
//        System.out.println(hotelFloorService.list());
        PageUtil<HotelFloor> pageUtil = new PageUtil<>(page.getCurrent(), page.getTotal(), page.getPages(), page.getRecords());
        return Result.ok().data(pageUtil);
    }

    @Operation(summary = "添加楼层接口")
    @Parameter(name = "hotelFloor", description = "楼层对象", in = ParameterIn.QUERY, required = true)
    @PostMapping("/add")
    public Result<Object> add(@RequestBody @Validated HotelFloor hotelFloor) {
        if (Objects.isNull(hotelFloor)) return Result.fail().message("楼层对象不能为空！");
        boolean flag = hotelFloorService.save(hotelFloor);
        return flag ? Result.ok().message("楼层添加成功！") : Result.fail().message("楼层添加失败！");
    }

    @Operation(summary = "获取楼层详情接口")
    @Parameter(name = "id", description = "楼层编号", in = ParameterIn.PATH, required = true)
    @GetMapping("/get/{id}")
    public Result<Object> get(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) return Result.fail().message("楼层编号不能为空！");
        HotelFloor hotelFloor = hotelFloorService.getById(id);
        if (Objects.isNull(hotelFloor)) {
            return Result.fail().message("楼层不存在！");
        }
        return Result.ok().data(hotelFloor);
    }

    @Operation(summary = "添加楼层接口")
    @Parameter(name = "hotelFloor", description = "楼层对象", in = ParameterIn.QUERY, required = true)
    @PutMapping("/edit")
    public Result<Object> update(@RequestBody @Validated HotelFloor hotelFloor) {
        if (Objects.isNull(hotelFloor)) return Result.fail().message("楼层对象不能为空！");
        boolean flag = hotelFloorService.updateById(hotelFloor);
        return flag ? Result.ok().message("楼层修改成功！") : Result.fail().message("楼层修改失败！");
    }

    @Operation(summary = "删除楼层")
    @Parameter(name = "id", description = "楼层编号", in = ParameterIn.PATH, required = true)
    @DeleteMapping("/del/{id}")
    public Result<Object> delete(@PathVariable("id") Long id) {
        boolean flag = hotelFloorService.removeById(id);
        return flag ? Result.ok().message("楼层删除成功！") : Result.fail().message("楼层删除失败!!");
    }



    // 用于添加房间时的下拉列表使用
    @GetMapping("/all")
    public List<FloorVo> all() {
        List<HotelFloor> list = hotelFloorService.list();
        return list.stream().map(floor -> {
            FloorVo floorVo = new FloorVo();
            floorVo.setId(floor.getId());
            floorVo.setName(floor.getFloorName());
            return floorVo;
        }).collect(Collectors.toList());
    }
    //test
    @PutMapping("/addfloor")
    public void addFloor(@RequestBody HotelFloor floor){
        hotelFloorService.save(floor);
    }
    @PutMapping("/cache1")
    public void addCache1(){
        HotelFloor floor = new HotelFloor();
        floor.setFloorName("123");
        Cache.put(floor.getFloorName(),floor);
        System.out.println(Cache.getCache());
    }
    @PutMapping("/cache2")
    public void addCache2(){
        HotelFloor floor = new HotelFloor();
        floor.setFloorName("222");
        Cache.put(floor.getFloorName(),floor);
        System.out.println(Cache.getCache());
    }
}
