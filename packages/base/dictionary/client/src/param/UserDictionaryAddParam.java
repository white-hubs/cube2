package com.guhecloud.cube.base.dictionary.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/28 14:48
 * @Description:
 */
@Data
public class UserDictionaryAddParam implements Serializable {

    private static final long serialVersionUID = -2263600306525549415L;

    /**
     * 名称
     */
    @NotEmpty(message = "字典名称不能为空")
    @Size(min = 1, max = 50, message = "字典名称只能在1-50个字符")
    private String name;
    /**
     * 编号
     */
    @Size(max = 100, message = "字典编码只能在1-100个字符")
    private String code;

    /**
     * 备注
     */
    @Size(max = 255, message = "备注不能超过255个字符")
    private String remarks;
    /**
     * 版本号
     */
    private String version;
    /**
     * 变量
     */
    private String variable;
    /**
     * 字典项编码
     */
    @NotEmpty(message = "字典项不能为空")
    private String itemCode;
    /**
     * 状态
     */
    private String status;
}
