package com.guhecloud.cube.biz.emergency.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EmergencyFileNoticeObj extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 消息场景编号
     */
    private String sceneCode;

    /**
     * 文件类型主键
     */
    private Long emergencyFileTypeId;

    /**
     * 发送对象类型
     */
    private String objType;

    /**
     * 发送对象主键
     */
    private String objId;
}
