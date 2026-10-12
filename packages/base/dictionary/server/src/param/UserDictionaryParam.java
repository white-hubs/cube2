package com.guhecloud.cube.base.dictionary.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/28 14:17
 * @Description:
 */
@Data
public class UserDictionaryParam implements Serializable {

    private static final long serialVersionUID = 5837514162263155014L;
    /**
     * 字典编码
     */
    private String code;
    /**
     * 字典项编码
     */
    @NotEmpty(message = "字典项不能为空")
    private String itemCode;
    /**
     * 字典名称（精准查询）
     */
    private String name;

    /**
     * 字典编码集合
     */
    private List<String> codes;

    /**
     * 是否查询禁用数据，默认false
     */
    private Boolean disable;
    /**
     * 用户id
     */
    private Set<Long> userIds;
    /**
     * 查询全部或者个人
     */
    private String queryType;
}
