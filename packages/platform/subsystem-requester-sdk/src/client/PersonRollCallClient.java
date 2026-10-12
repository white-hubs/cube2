package com.guhecloud.subsystem.requester.sdk.client;

import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.rollcall.PersonRollCallRecordParam;
import com.guhecloud.subsystem.requester.sdk.vo.rollcall.PersonRollCallRecordVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = SubsystemTypeConstants.PERSON_ROLLCALL_SERVER, configuration = DynamicConfiguration.class)
public interface PersonRollCallClient {
    /**
     * 采集点名任务记录
     *
     * @return 点名记录
     */
    @PostMapping("/rollcall-record")
    List<PersonRollCallRecordVO> getRollCallRecord(@RequestBody PersonRollCallRecordParam param);

    /**
     * 获取 人员点名类别枚举
     */
    @GetMapping("/call-type")
    List<Map<String, Object>> getCallTypeEnum();
}
