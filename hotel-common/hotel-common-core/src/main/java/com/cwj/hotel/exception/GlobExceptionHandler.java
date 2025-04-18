package com.cwj.hotel.exception;


import com.cwj.hotel.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@Slf4j
//@RestControllerAdvice
public class GlobExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public Result<Object> badRequestException(BadRequestException ex) {
        log.error("错误的请求：{}", ex.getMessage());
        return Result.fail().code(HttpStatus.BAD_REQUEST.value()).message(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Object> exception(Exception ex) {
        log.error("发生异常了：{}", ex.getMessage());
        return Result.fail().code(999).message(ex.getMessage());
    }
}
