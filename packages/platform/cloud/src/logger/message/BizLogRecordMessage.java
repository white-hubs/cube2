package com.guhecloud.platform.cloud.logger.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizLogRecordMessage {
    public final static String TOPIC = "BIZ-LOG-RECORD";
    private BizLogRecordDTO bizLogRecord;
}
