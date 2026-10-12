package com.guhecloud.cube.base.dictionary.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/28 16:04
 * @Description:
 */
@Data
public class UserDictionaryDTO implements Serializable {

    private static final long serialVersionUID = -3132892108620496832L;
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
