package com.cwj.hotel.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashSet;

/**
 * 自定义异常类
 */
@Getter
public class BadRequestException extends RuntimeException {


    private int code = HttpStatus.BAD_REQUEST.value();
    private String message = HttpStatus.BAD_REQUEST.getReasonPhrase();

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
