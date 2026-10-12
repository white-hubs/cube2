package com.guhecloud.platform.cloud.mq;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SysOperationLogMessage {
    public static final String TOPIC = "SYSTEM_OPERATION_LOG";

}
