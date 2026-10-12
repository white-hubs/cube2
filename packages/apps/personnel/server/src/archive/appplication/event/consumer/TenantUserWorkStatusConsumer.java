package com.guhecloud.cube.srv.gen.personnel.management.server.archive.appplication.event.consumer;

import com.guhecloud.cube.biz.ouaa.client.dto.TenantUserWorkStatusDTO;
import com.guhecloud.cube.biz.ouaa.client.message.TenantUserWorkStatusMessage;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.StaffQueryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 租户人员工作状态监听
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TenantUserWorkStatusMessage.TOPIC + "_" + "PERSONNEL", topic = TenantUserWorkStatusMessage.TOPIC)
public class TenantUserWorkStatusConsumer implements RocketMQListener<TenantUserWorkStatusMessage> {
    private final StaffQueryService staffQueryService;
    private final StaffCommandService staffCommandService;

    @Override
    public void onMessage(TenantUserWorkStatusMessage message) {
        SessionContextHolder.clearSessionContext();
        if (message == null || message.getTenant() == null) {
            return;
        }
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            List<StaffPO> staffs = new ArrayList<>();
            Map<Long, StaffPO> staffMap = staffQueryService
                    .listByIds(message.getUpdateWorkStatusList().stream().map(TenantUserWorkStatusDTO::getUserId)
                                       .collect(Collectors.toSet()))
                    .stream().collect(Collectors.toMap(StaffPO::getId, staffPO -> staffPO));
            message.getUpdateWorkStatusList().forEach(workStatusDTO -> {
                                                          StaffPO staffPO = staffMap.get(workStatusDTO.getUserId());
                                                          if (Objects.isNull(staffPO)) {
                                                              return;
                                                          }

                                                          staffPO.setState(StringUtils.isNoneBlank(workStatusDTO.getWorkStatus()) ? workStatusDTO.getWorkStatus() : "");
                                                          staffs.add(staffPO);
                                                      }
                                                     );
            staffCommandService.updateBatchById(staffs);
        } catch (Exception e) {
            log.error("人事消费ouaa推送工作状态变更记录失败,失败原因:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }

    }
}
