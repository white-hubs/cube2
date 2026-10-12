package com.guhecloud.cube.base.dictionary.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;

@Data
@TableName("sys_dictionary")
public class SysDictionary extends BasicEnhancedEntity<Long> {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 编号
     */
    private String code;
    /**
     * 备注
     */
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
     * 字典项编号
     */
    private String itemCode;
    /**
     * 状态：启用（ENABLE）、禁用（DISABLE）、删除（DELETE）
     */
    private String status;
}
