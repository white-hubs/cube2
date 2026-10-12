package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-23  16:33
 * @Description:
 * @Version: 1.0
 */
@Data
@TableName("att_dumu_message")
public class AttDumuMessagePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String msgId;

    public AttDumuMessagePO(String msgId) {
        this.msgId = msgId;
    }
}
