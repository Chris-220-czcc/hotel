package com.cwj.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwj.hotel.entity.HotelMember;
import com.cwj.hotel.mapper.MemberMapper;
import com.cwj.hotel.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, HotelMember> implements MemberService{
}
