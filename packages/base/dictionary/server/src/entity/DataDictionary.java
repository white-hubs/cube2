package com.guhecloud.cube.base.dictionary.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
@TableName("data_dictionary")
public class DataDictionary extends BasicEnhancedTenantEntity<Long> {
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
     * 字典项编码
     */
    private String itemCode;
    /**
     * 状态
     */
    private String status;
}
