package com.guhecloud.cube.base.alarm.client;

import com.guhecloud.cube.base.alarm.client.param.InitAssignAlarmRecordParam;
import com.guhecloud.cube.base.alarm.client.param.SatAlarmMsgReSendParam;
import com.guhecloud.cube.base.alarm.client.param.SatAlarmMsgSendParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "alarm")
public interface SatAlarmMsgSendClient {

    /**
     * 报警消息发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-alarm-msg/send")
    Boolean sendMsg(@Valid @RequestBody SatAlarmMsgSendParam param);

    /**
     * 再次发送报警通知
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-alarm-msg/re-send")
    Boolean reSendMsg(@Valid @RequestBody SatAlarmMsgReSendParam param);

    /**
     * 报警指派
     *
     * @param initAssignAlarmRecordParam
     */
    @PostMapping("/sat-alarm/assign")
    void initAssignAlarmRecord(@Valid @RequestBody InitAssignAlarmRecordParam initAssignAlarmRecordParam);
}
