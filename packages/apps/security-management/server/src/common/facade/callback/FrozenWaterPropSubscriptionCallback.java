package com.guhecloud.cube.srv.gen.securitymanagement.server.common.facade.callback;

import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ClodWaterAttributeMap;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.OperateLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.enumeration.LogTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ClodWaterAttributeMapService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.OperateLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.facade.PropCallbackResponse;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2023/11/3 9:33
 * @description 冷冻水属性订阅回调处理
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FrozenWaterPropSubscriptionCallback {
    private final ClodWaterAttributeMapService clodWaterAttributeMapService;

    private final OperateLogService operateLogService;

    @PostMapping("/subscribe/frozen_water/callback")
    public void execute(@RequestBody PropCallbackResponse propCallbackResponse) {
        log.info("属性订阅回调响应:{}", propCallbackResponse);
        try {
            List<ClodWaterAttributeMap> list = clodWaterAttributeMapService.list();
            SessionContextHolder.requestTenant(list.get(0).getTenant());
            Map<String, ClodWaterAttributeMap> collect = list.stream()
                    .collect(Collectors.toMap(ClodWaterAttributeMap::getAttributeCode, s -> s));
            Long deviceId = propCallbackResponse.getDeviceId();
            List<PropCallbackResponse.Value> values = propCallbackResponse.getValues();
            for (PropCallbackResponse.Value update : values) {
                String key = update.getKey();
                ClodWaterAttributeMap clodWaterAttributeMap = collect.get(key);
                OperateLog operateLog = new OperateLog();
                operateLog.setDeviceId(deviceId);
                operateLog.setLogType(LogTypeEnum.HARDWARE_LOG.name());
                LocalDateTime date = DateUtils.parse(update.getTs());
                operateLog.setLogTime(date);
                String remark = "";
                if (clodWaterAttributeMap.getValueType().equals("1")) {
                    remark = clodWaterAttributeMap.getTranslateName() + update.getPreValue() + "调成" + update.getValue();
                } else if (clodWaterAttributeMap.getValueType().equals("0")) {
                    String[] split = clodWaterAttributeMap.getTranslateName().split(",");
                    for (String s : split) {
                        String[] split1 = s.split("-");
                        if (split1[0].equals(update.getValue())) {
                            remark = split1[1];
                        }
                    }
                }
                operateLog.setRemark(remark);
                operateLogService.save(operateLog);
                log.info("设备操作记录成功");
            }

        } catch (Exception e) {
            log.info("设备操作记录失败============={}", e);
        }
    }
}
