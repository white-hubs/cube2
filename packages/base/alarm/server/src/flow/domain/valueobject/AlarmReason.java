package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmReasonConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmReasonConfigMapper;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Data;

import java.util.Objects;

@Data
public class AlarmReason {

    private Long id;

    /**
     * 原因编号
     */
    private String code;

    /**
     * 报警原因
     */
    private String reason;

    /**
     * 原因描述
     */
    private String remark;

    public static AlarmReason getAlarmReasonById(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        AlarmReasonConfigMapper alarmReasonConfigMapper = ApplicationContextHolder.getBean(AlarmReasonConfigMapper.class);
        AlarmReasonConfigPO alarmReasonConfigPO = alarmReasonConfigMapper.selectById(id);
        return ObjectUtils.copying(alarmReasonConfigPO, AlarmReason.class);
    }
}
