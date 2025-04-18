package com.openlab.hotel.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SpringDocConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(
                new Info()
                        .title("酒店管理系统接口测试文档")
                        .description("这个文档只在开发阶段使用，在生产阶段建议关闭。")
                        .contact(new Contact())
                        .version("v1.0.0"))
                .externalDocs(new ExternalDocumentation().description("酒店管理系统"));
    }

    @Bean
    public GroupedOpenApi logApi() {
        return GroupedOpenApi.builder()
                .group("日志接口")
                .pathsToMatch("/log/**, /logs")
                .packagesToScan("com.cwj.hotel.controller")
                .build();
    }
    @Bean
    public GroupedOpenApi hotelApi() {
        return GroupedOpenApi.builder()
                .group("酒店接口")
                .pathsToMatch("/hotel/**, /roomtype/**, /room/**, /floor/**")
                .packagesToScan("com.cwj.hotel.controller")
                .build();
    }
    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder()
                .group("系统接口")
                .pathsToMatch("/role/**, /user/**")
                .packagesToScan("com.cwj.hotel.controller")
                .build();
    }
}
