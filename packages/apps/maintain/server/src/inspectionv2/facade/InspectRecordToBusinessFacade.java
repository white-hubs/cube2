package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.facade;

import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultOrder;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerNew;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordToBusinessVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import instance.command.ProcessStartedMessageAttrCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 巡检记录 转其他业务 查询防腐层
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordToBusinessFacade {

    private final FaultOrderService faultOrderService;
    private final HiddenDangerNewService hiddenDangerNewService;
    private final ProcessInstanceClient processInstanceClient;

    public void extendRecordToBusiness(InspectRecordToBusinessVO vo) {
        String status = "";
        LocalDateTime time = null;
        boolean exist = true;

        try {
            switch (vo.getBusinessCategory()) {
                case "HIDDENDANGER": {
                    HiddenDangerNew dto = hiddenDangerNewService.lambdaQuery()
                            .eq(HiddenDangerNew::getProcessInsId, vo.getBusinessId()).last("limit 1").one();
                    if (ObjectUtils.isEmpty(dto)) {
                        //存在流引擎有而隐患排查这边没有的数据需要向隐患排查中补数据
                        ProcessStartedMessageAttrCommand message = processInstanceClient.getProcessInstanceFormInfo(vo.getBusinessId());
                        dto = hiddenDangerNewService.addHiddenDangerNew(message);
                    }
                    if (dto != null) {
                        status = TicketStatusEnum.getDescription(dto.getState());
                        time = dto.getUpdateTime();
                    }
                }
                break;
                case "FAULT": {
                    FaultOrder dto = faultOrderService.lambdaQuery().eq(FaultOrder::getProcessInsId, vo.getBusinessId()).last("limit 1")
                            .one();
                    if (ObjectUtils.isEmpty(dto)) {
                        //存在流引擎有而故障这边没有的数据需要向故障中补数据
                        ProcessStartedMessageAttrCommand message = processInstanceClient.getProcessInstanceFormInfo(vo.getBusinessId());
                        dto = faultOrderService.addComplaintOrder(message);
                    }
                    if (dto != null) {
                        if (TicketStatusEnum.getNotActiveStatus().contains(dto.getState())) {
                            dto.setState(TicketStatusEnum.FINISHED.name());
                        }
                        if (FlowResult.ACTIVE.name().equals(dto.getState())) {
                            dto.setState(TicketStatusEnum.WAITING_FOR_HANDLE.name());
                        }
                        status = TicketStatusEnum.getDescription(dto.getState());
                        time = dto.getUpdateTime();
                    }
                }
                break;
            }
        } catch (Exception e) {
            log.error("查询巡检记录转其他业务信息异常", e);
            exist = false;
        }
        vo.setBusinessStatus(status);
        vo.setBusinessTime(time);
        vo.setBusinessExist(exist);
    }

    /**
     * 批量扩展业务转工单信息
     *
     * @param businessList 需要扩展的集合
     */
    public void extBusinessOrder(List<InspectRecordToBusinessVO> businessList) {
        businessList.removeIf(inspectRecordToBusinessVO -> inspectRecordToBusinessVO.getId() == null);
        if (CollectionUtils.isEmpty(businessList)) {
            return;
        }
        // 隐患单
        Map<String, List<HiddenDangerNew>> hiddenDangerMap = new HashMap<>();
        // 故障单
        Map<String, List<FaultOrder>> faultOrderMap = new HashMap<>();

        List<HiddenDangerNew> hiddenDangerlist = new ArrayList<>();
        List<FaultOrder> faultOrderList = new ArrayList<>();
        List<String> businessIdList = businessList.stream().map(InspectRecordToBusinessVO::getBusinessId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(businessIdList)) {
            // 获取隐患数据
            hiddenDangerlist = hiddenDangerNewService.lambdaQuery().in(HiddenDangerNew::getProcessInsId, businessIdList).list();
            // 获取故障数据
            faultOrderList = faultOrderService.lambdaQuery().in(FaultOrder::getProcessInsId, businessIdList).list();
        }
        /* 转map */
        if (!CollectionUtils.isEmpty(hiddenDangerlist)) {
            hiddenDangerMap = hiddenDangerlist.stream().collect(Collectors.groupingBy(HiddenDangerNew::getProcessInsId));
        }
        if (!CollectionUtils.isEmpty(faultOrderList)) {
            faultOrderMap = faultOrderList.stream().collect(Collectors.groupingBy(FaultOrder::getProcessInsId));
        }

        Map<String, List<HiddenDangerNew>> finalHiddenDangerMap = hiddenDangerMap;
        Map<String, List<FaultOrder>> finalFaultOrderMap = faultOrderMap;
        businessList.forEach(business -> {
            /* 判断是隐患还是故障，在对应的map里面取数据进行处理 */
            switch (business.getBusinessCategory()) {
                case "HIDDENDANGER": {
                    List<HiddenDangerNew> list = finalHiddenDangerMap.get(business.getBusinessId());
                    if (!CollectionUtils.isEmpty(list)) {
                        final HiddenDangerNew dto = list.get(0);
                        if (dto != null) {
                            business.setBusinessStatus(TicketStatusEnum.getDescription(dto.getState()));
                            business.setBusinessTime(dto.getUpdateTime());
                        }
                    }
                }
                break;
                case "FAULT": {
                    List<FaultOrder> list = finalFaultOrderMap.get(business.getBusinessId());
                    if (!CollectionUtils.isEmpty(list)) {
                        final FaultOrder dto = list.get(0);
                        if (dto != null) {
                            if (TicketStatusEnum.getNotActiveStatus().contains(dto.getState())) {
                                dto.setState(TicketStatusEnum.FINISHED.name());
                            }
                            if (FlowResult.ACTIVE.name().equals(dto.getState())) {
                                dto.setState(TicketStatusEnum.WAITING_FOR_HANDLE.name());
                            }
                            business.setBusinessStatus(TicketStatusEnum.getDescription(dto.getState()));
                            business.setBusinessTime(dto.getUpdateTime());
                        }
                    }
                    break;
                }
            }
        });
    }
}
