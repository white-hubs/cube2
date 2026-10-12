package com.guhecloud.cube.base.dictionary.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
@TableName("cascade_dictionary")
public class CascadeDictionary extends BasicEnhancedTenantEntity<Long> {
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
     * 字典项id
     */
    private String itemCode;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 父id
     */
    private Long superiorsId;
    /**
     * 状态
     */
    private String status;
    /**
     * 数据权限
     */
    private String permissionState;
}
