package com.cwj.hotel.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 对日期进行序列化和返回序列化操作的配置类
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
                builder.locale(Locale.CHINA) // 指定中国格式的日期
                        .timeZone(TimeZone.getTimeZone(ZoneId.systemDefault())) // 根据系统中的时区编号来设置时区
                        // 设置日期格式
                        .simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN)
                        // 解决long类型的精度损失问题
                        .serializerByType(Long.class, ToStringSerializer.instance)
                        // 日期格式自定义类
                        .modules(new HotelJavaTimeModule());
        };
        /*return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                jacksonObjectMapperBuilder.locale(Locale.CHINA) // 指定中国格式的日期
                        .timeZone(TimeZone.getTimeZone(ZoneId.systemDefault())) // 根据系统中的时区编号来设置时区
                        // 设置日期格式
                        .simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN)
                        // 解决long类型的精度损失问题
                        .serializerByType(Long.class, ToStringSerializer.instance)
                        // 日期格式自定义类
                        .modules(new HotelJavaTimemModule());
            }
        };*/
    }
}
