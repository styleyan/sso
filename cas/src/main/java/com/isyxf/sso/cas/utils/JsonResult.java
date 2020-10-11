package com.isyxf.sso.cas.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author Y.jer
 * 通用返回格式
 */
@Getter
@Setter
@ToString
public class JsonResult<T> implements Serializable {
    /**
     * 默认 code 码
     */
    private int code = 0;

    /**
     * 错误提示信息
     */
    private String message = "";

    /**
     * 返回状态(0: 失败, 1: 成功)
     */
    private int status = 1;

    /**
     * 返回数据
     */
    private T result;

    public JsonResult(){}

    /**
     * 单纯成功，不返回数据
     * @return
     */
    public static JsonResult success() {
        JsonResult resultSet = new JsonResult();
        return resultSet;
    }

    /**
     * 成功有数据
     */
    public static JsonResult success(Object result) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setResult(result);

        return jsonResult;
    }

    /**
     * 成功有数据，并有code码
     */
    public static JsonResult success(int code, Object result) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(code);
        jsonResult.setResult(result);

        return jsonResult;
    }

    /**
     * 请求失败
     */
    public static JsonResult failure(int code, String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus(0);
        jsonResult.setCode(code);
        jsonResult.setMessage(message);

        return jsonResult;
    }
}
