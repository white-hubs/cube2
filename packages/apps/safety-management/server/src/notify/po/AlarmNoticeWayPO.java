package com.guhecloud.cube.srv.gen.safety.management.notify.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("alarm_notice_way")
public class AlarmNoticeWayPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警消息配置主键
     */
    private Long alarmNoticeId;

    /**
     * 通知方式
     */
    private String noticeWay;

    /**
     * 模板主键
     */
    private Long templateId;

}
