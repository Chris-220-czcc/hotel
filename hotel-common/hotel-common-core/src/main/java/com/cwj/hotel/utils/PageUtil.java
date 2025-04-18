package com.cwj.hotel.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

// 分页工具类
@Getter
@AllArgsConstructor
public final class PageUtil<T> {
    /* 页码 */
    private Long pageIndex;
    /* 总记录数 */
    private Long total;
    /* 总页数 */
    private Long pages;
    /* 数据列表 */
    private List<T> content;
    private PageUtil(){}
}
