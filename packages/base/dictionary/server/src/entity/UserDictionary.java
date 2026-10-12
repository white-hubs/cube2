package com.guhecloud.cube.base.dictionary.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户字典
 *
 * @TableName user_dictionary
 */
@Data
@TableName("user_dictionary")
public class UserDictionary extends BasicEnhancedTenantEntity<Long> implements Serializable {

    private static final long serialVersionUID = -7193682480282376040L;
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