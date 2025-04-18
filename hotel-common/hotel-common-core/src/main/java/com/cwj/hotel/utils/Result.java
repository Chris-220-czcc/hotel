package com.cwj.hotel.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 统一返回结果对象
 * @param <T> 泛型参数
 */
@Getter
@Setter
public final class Result<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    private Result() {

    }

    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        result.setMessage("操作成功");
        return result;
    }

    public static <T> Result<T> fail() {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.PRECONDITION_FAILED.value());
        result.setMessage("操作失败");
        return result;
    }

    public Result<T> code(int code) {
        this.setCode(code);
        return this;
    }

    public Result<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public Result<T> data(T data) {
        this.setData(data);
        return this;
    }

}
