package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmFilterScopeEnum;
import lombok.Getter;

@Getter
public class AlarmFilterScope {

    /**
     * 报警作用域
     */
    private AlarmFilterScopeEnum filterScope;

    public AlarmFilterScope(AlarmFilterScopeEnum filterScope) {
        this.filterScope = filterScope;
    }
}
