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
 * @describe 通知方式
 * @date 2023/9/1 17:30
 */
@Data
@TableName("assembly_notice_way")
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageAssemblyNoticeWay extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 消息配置id
     */
    private Long noticeId;
    /**
     * 通知方式
     */
    private String noticeWay;
    /**
     * 模板id
     */
    private Long templateId;
}
