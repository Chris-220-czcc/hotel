package com.cwj.hotel.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Log4j2
@Component
public class MybatisObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充...");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        // 注意：此处的 fieldVal 应该是从 redis 中获取当前登录的用户编号来进行填充
        this.strictInsertFill(metaObject, "createBy", Long.class, 1L);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        // 注意：此处的 fieldVal 应该是从 redis 中获取当前登录的用户编号来进行填充
        this.strictInsertFill(metaObject, "updateBy", Long.class, 1L);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充...");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        // 注意：此处的 fieldVal 应该是从 redis 中获取当前登录的用户编号来进行填充
        this.strictUpdateFill(metaObject, "updateBy", Long.class, 1L);
    }
}
