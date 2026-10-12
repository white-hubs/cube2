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
@TableName("alarm_notice_obj")
public class AlarmNoticeObjPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警通知配置主键
     */
    private Long alarmNoticeId;

    /**
     * 通知对象类型
     */
    private String objType;

    /**
     * 通知对象主键
     */
    private String objId;

    /**
     * 通知对象json信息
     */
    private String objInfo;
}
