package com.guhecloud.cube.srv.gen.safety.management.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmMessage {

    public static final String TOPIC = "di-new-alarm";

    private List<AlarmRecordMessage> dataList;
}
