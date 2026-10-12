package com.guhecloud.cube.base.bracing.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 通知消息配置
 * @date 2023/9/1 16:28
 */
@Data
@TableName("assembly_notice")
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageAssemblyNotice extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 消息场景码
     */
    private String sceneCode;
    /**
     * 标签
     */
    private String label;
    /**
     * 通知时间
     */
    private Integer value;

    /**
     * 配置参数
     */
    private String config;
}
