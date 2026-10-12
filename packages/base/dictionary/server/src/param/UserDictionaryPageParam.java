package com.guhecloud.cube.base.dictionary.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/28 14:42
 * @Description:
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDictionaryPageParam extends Paging implements Serializable {

    private static final long serialVersionUID = -7176959130620608556L;
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
}
