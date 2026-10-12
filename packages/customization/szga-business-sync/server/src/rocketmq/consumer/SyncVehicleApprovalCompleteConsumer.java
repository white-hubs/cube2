package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.ProcessCompletedMessage;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.VechicleApprovalUpdateVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.VechicleApprovalVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.VechicleDeleteVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserParkingPermissionService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/11 15:22
 * @Description: 车辆审批结束业务监听
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "VEHICLE_APPROVAL_COMPLETE_SYNC", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "VECHICLE_INSERT || VECHICLE_UPDATE || VECHICLE_DELETE"
)
public class SyncVehicleApprovalCompleteConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final SyncUserParkingPermissionService userParkingPermissionService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("车辆流程审批结束:{},data:{},result:{}", message.getProcessInstanceId(), message.getData(), message.getResult().name());
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            if (!"PASS".equals(message.getResult().name())) {
                return;
            }
            switch (message.getBusinessId().getBusinessType()) {
                case "VECHICLE_INSERT":
                    VechicleApprovalVO vechicleApprovalVO = JSONObject.parseObject(message.getData(), VechicleApprovalVO.class);
                    userParkingPermissionService.vehicleInsert(vechicleApprovalVO);
                    break;
                case "VECHICLE_UPDATE":
                    VechicleApprovalUpdateVO vechicleApprovalUpdateVO = JSONObject.parseObject(message.getData(), VechicleApprovalUpdateVO.class);
                    userParkingPermissionService.vehicleUpdate(vechicleApprovalUpdateVO);
                    break;
                case "VECHICLE_DELETE":
                    VechicleDeleteVO vechicleDeleteVO = JSONObject.parseObject(message.getData(), VechicleDeleteVO.class);
                    userParkingPermissionService.vehicleDelete(vechicleDeleteVO);
                    break;
                default:
                    log.error("业务类型错误:{}", message.getBusinessId().getBusinessType());
            }
        } catch (Exception e) {
            log.error("车辆审批结束业务处理异常:{}", e.getMessage());
            SessionContextHolder.clearSessionContext();
        }
    }
}
