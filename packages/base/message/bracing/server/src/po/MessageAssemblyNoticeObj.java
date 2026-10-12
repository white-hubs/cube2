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
 * @describe 通知对象
 * @date 2023/9/1 17:33
 */
@Data
@TableName("assembly_notice_obj")
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageAssemblyNoticeObj extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 消息配置id
     */
    private Long noticeId;
    /**
     * 对象类型
     */
    private String objType;
    /**
     * 对象id
     */
    private String objId;
    /**
     * 对象信息
     */
    private String objInfo;
}
