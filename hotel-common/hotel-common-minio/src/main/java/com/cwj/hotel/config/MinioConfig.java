package com.cwj.hotel.config;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {
    @Resource
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://192.168.237.129:9000")
                .credentials("5Y14ZLL0CX12FHPHWL0Q", "+OE4y8tvP7C1X5lSYLRs0fJ1DJiN2V72o2tPR97O")
                .build();
    }
}
