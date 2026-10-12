package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 广播模板数据模型
 *
 * @author lanchun
 * @date 2022/12/12 13:45
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("broadcast_template")
public class BroadcastTemplatePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    @Unique(composites = "deleted", message = "模板名称已存在")
    private String name;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否是默认模板
     */
    private Boolean defaulted;

}
