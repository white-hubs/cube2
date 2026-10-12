package com.guhecloud.cube.biz.linkage.server.linkage.strategy.action.result;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallbackResult {

    private Long eventId;

    private Long eventActionId;

    private ExecuteStatusEnum executeStatus;

    public static final CallbackResult FAIL = new CallbackResult();
}
