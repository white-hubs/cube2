package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmFilterScopeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlarmPushConfig {

    /**
     * 报警作用域
     */
    private AlarmFilterScopeEnum filterScope;

    /**
     * 是否开启警告声音
     */
    private Boolean turnWarningSound;
}
