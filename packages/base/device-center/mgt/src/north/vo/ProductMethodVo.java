package com.guhecloud.cube.iot.mgt.north.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Map;

/**
 * 产品方法VO
 */
@Data
public class ProductMethodVo {
    /**
     * 方法名称
     */
    private String name;

    /**
     * 方法编码（产品内唯一，用于跨环境映射）
     */
    private String methodCode;

    /**
     * 方法描述
     */
    private String description;

    /**
     * 方法类型：sync-同步方法、async-异步方法
     */
    private String methodType;

    /**
     * 输入参数定义
     */
    private Map<String, Object> inputParams;

    /**
     * 输出参数定义
     */
    private Map<String, Object> outputParams;

    /**
     * 执行超时时间（毫秒）
     */
    private Integer timeoutMs;

    /**
     * 创建时间格式化字符串
     */
    private String createTimeFormat;

    /**
     * 输入参数JSON字符串（数据库原始字段，序列化时隐藏）
     */
    @JsonIgnore
    private String inputParamsStr;

    /**
     * 输出参数JSON字符串（数据库原始字段，序列化时隐藏）
     */
    @JsonIgnore
    private String outputParamsStr;
}
