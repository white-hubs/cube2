package com.guhecloud.cube.biz.linkage.server.linkage.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 联动预案动作数据模型
 *
 * @author lanchun
 * @date 2022/6/21 13:56
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LinkageAction extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 联动预案主键
     */
    private Long linkageId;

    /**
     * 动作类型
     */
    private LinkageActionTypeEnum actionType;

    /**
     * 动作json配置
     */
    private String actionConfig;

    /**
     * 启用状态
     */
    private Boolean enabled = true;

}
