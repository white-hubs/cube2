package com.guhecloud.cube.base.dataintegrate.server.rocketmq;

import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyAlarmRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewAlarmMessage {
    /**
     * 消息主题
     */
    public static final String TOPIC = "di-new-alarm";

    private List<SafetyAlarmRecord> dataList;
}
