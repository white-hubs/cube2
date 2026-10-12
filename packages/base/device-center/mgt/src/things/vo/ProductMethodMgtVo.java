package com.guhecloud.cube.iot.mgt.things.vo;

import lombok.Data;

import java.util.Map;

/**
 * 产品方法VO
 */
@Data
public class ProductMethodMgtVo {

    private Long id;

    /**
     * 产品ID
     */
    private Long prodId;

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
     * 是否支持重试：0-否 1-是
     */
    private Boolean retryable;

    /**
     * 最大重试次数
     */
    private Integer maxRetries;

    /**
     * 状态：1-启用 0-禁用
     */
    private Integer status;

    /**
     * 方法扩展配置
     */
    private Map<String, Object> extendConfig;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间格式化字符串
     */
    private String createTimeFormat;
}
