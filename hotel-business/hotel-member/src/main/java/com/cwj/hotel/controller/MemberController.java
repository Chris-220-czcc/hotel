package com.cwj.hotel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cwj.hotel.entity.HotelMember;
import com.cwj.hotel.service.MemberService;
import com.cwj.hotel.utils.PageUtil;
import com.cwj.hotel.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Resource
    private MemberService memberService;
    /*
        根据realname查找
     */
    @GetMapping("/list")
    public Result<Object> list(
            @RequestParam(value = "searchValue",defaultValue = "")String searchValue,
            @RequestParam(value = "pageIndex",defaultValue = "1") Long pageIndex,
            @RequestParam(value = "pageSize",defaultValue = "10") Long pageSize
    ){
        QueryWrapper<HotelMember> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(searchValue),"real_name",searchValue);
        Page<HotelMember> page=new Page<>(pageIndex,pageSize);
        memberService.page(page,queryWrapper);
        PageUtil<HotelMember> hotelMembers=new PageUtil<>(page.getCurrent(),page.getTotal(),page.getPages(),page.getRecords());
        return Result.ok().data(hotelMembers).message("查询会员列表成功");
    }
    @PostMapping("/add")
    public Result<Object> add(@RequestBody HotelMember hotelMember){
        memberService.save(hotelMember);
        return Result.ok().message("添加会员成功");
    }
    @PostMapping("/update")
    public Result<Object> update(@RequestBody HotelMember hotelMember){
        memberService.updateById(hotelMember);
        return Result.ok().message("修改会员成功");
    }
    @GetMapping("/delete")
    public Result<Object> delete(Long id){
        memberService.removeById(id);
        return Result.ok().message("删除会员成功");
    }

}
