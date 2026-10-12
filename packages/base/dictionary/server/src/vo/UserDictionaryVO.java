package com.guhecloud.cube.base.dictionary.server.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/28 14:20
 * @Description:
 */
@Data
public class UserDictionaryVO implements Serializable {

    private static final long serialVersionUID = -2314972395325822265L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 名称
     */
    private String name;

    /**
     * 编号
     */
    private String code;

    /**
     * 变量
     */
    private String variable;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 版本号
     */
    private String version;

    /**
     * 字典项编号
     */
    private String itemCode;

    /**
     * 状态：启用：enable；禁用：disable
     */
    private String status;
}
