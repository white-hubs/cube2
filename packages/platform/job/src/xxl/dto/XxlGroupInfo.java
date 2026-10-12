package com.guhecloud.platform.job.xxl.dto;

import lombok.Data;

import java.util.List;

/**
 * 执行器
 */
@Data
public class XxlGroupInfo {
    /**
     * 执行器id
     */
    private Integer id;
    /**
     * 执行器名称
     */
    private String appname;
    /**
     * 类型
     */
    private Integer addressType;
    /**
     * 执行器地址(逗号隔开)
     */
    private String addressList;
    /**
     * 执行器地址集合
     */
    private List<String> registryList;
}
