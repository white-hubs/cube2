package com.guhecloud.cube.base.dictionary.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/10/20 16:49
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryMessage {
    public static final String TOPIC = "DICTIONARY";
    /**
     * 字典项
     */
    private String itemCode;
    /**
     * 字典编码
     */
    private String code;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 租户code
     */
    private String tenant;
    /**
     * 字典变量
     */
    private String variable;
    /**
     * 字典状态
     */
    private String status;
    /**
     * 字典类型
     */
    private String type;
    /**
     * 操作类型
     */
    private String operationType;
}
