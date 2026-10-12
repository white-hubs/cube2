package com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.device.supervise.client.enumeration.DeviceLifeEventTypeEnum;
import com.guhecloud.cube.srv.gen.device.supervise.client.message.DeviceLifeEventMessage;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.ObjInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.ObjTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.ObjInfoGainService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration.FaultResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.OutsourceWorkOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dao.OutsourceWorkOrderMapper;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dto.OutsourceWorkOrderDTO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo.OutsourceWorkOrderVO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceWorkOrder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wql
 * @describe 委外维修
 * @date 2024/1/8 16:01
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OutsourceWorkOrderServiceImpl extends ServiceImpl<OutsourceWorkOrderMapper, OutsourceWorkOrder> implements OutsourceWorkOrderService {
    private final ProcessInstanceClient processInstanceClient;
    private final ObjInfoGainService objInfoGainService;

    @Override
    public Pagination<OutsourceWorkOrderVO> personalPage(ProcessInstancePageQuery processInstancePageQuery) {
        Pagination<ProcessInstanceDTO> page = processInstanceClient.page(processInstancePageQuery);
        return PagingUtils.convert(page, coverPage(page));
    }

    @Override
    public OutsourceWorkOrder updateOutsourceWorkOrder(OutsourceWorkOrderDTO outsourceWorkOrderDTO, String processInstanceId) {
        log.info("更新委外维修单:{}", JSON.toJSONString(outsourceWorkOrderDTO));
        this.lambdaUpdate().set(OutsourceWorkOrder::getIdentifyResult, outsourceWorkOrderDTO.getIdentifyResult())
                .set(OutsourceWorkOrder::getIdentifyRemarks, outsourceWorkOrderDTO.getIdentifyRemarks())
                .set(OutsourceWorkOrder::getCompanyId, outsourceWorkOrderDTO.getCompanyId())
                .set(OutsourceWorkOrder::getTypeId, outsourceWorkOrderDTO.getTypeId())
                .set(OutsourceWorkOrder::getHandleResult, outsourceWorkOrderDTO.getHandleResult())
                .set(OutsourceWorkOrder::getHandleRemarks, outsourceWorkOrderDTO.getHandleRemarks())
                .set(OutsourceWorkOrder::getAppraise, outsourceWorkOrderDTO.getAppraise())
                .set(OutsourceWorkOrder::getAppraiseRemarks, outsourceWorkOrderDTO.getAppraiseRemarks())
                .set(OutsourceWorkOrder::getAppraiseAttachment, outsourceWorkOrderDTO.getAppraiseAttachment())
                .eq(OutsourceWorkOrder::getProcessInstanceId, processInstanceId).update();
        return this.lambdaQuery().eq(OutsourceWorkOrder::getProcessInstanceId, processInstanceId).one();
    }

    private List<OutsourceWorkOrderVO> coverPage(Pagination<ProcessInstanceDTO> page) {
        List<OutsourceWorkOrderVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(record -> {
                OutsourceWorkOrderVO vo = new OutsourceWorkOrderVO();
                BeanUtils.copyProperties(record, vo);
                List<OutsourceWorkOrder> list = this.lambdaQuery()
                        .eq(OutsourceWorkOrder::getProcessInstanceId, record.getProcessInstanceId()).list();
                OutsourceWorkOrder outsourceWorkOrder;
                if (ObjectUtils.isNotEmpty(list)) {
                    outsourceWorkOrder = list.get(0);
                } else {
                    //存在流引擎有而故障这边没有的数据需要向故障中补数据
                    ProcessStartedMessage message = processInstanceClient.getProcessInstanceFormInfo(record.getProcessInstanceId());
                    outsourceWorkOrder = addOutsourceWorkOrder(message);
                }
                setListField(vo, outsourceWorkOrder);
                vos.add(vo);
            });
        }
        return vos;
    }

    /**
     * 设置列表页面业务字段值
     *
     * @param vo                 委外vo
     * @param outsourceWorkOrder 委外DTO
     */
    private void setListField(OutsourceWorkOrderVO vo, OutsourceWorkOrder outsourceWorkOrder) {
        if (ObjectUtils.isNotEmpty(outsourceWorkOrder)) {
            vo.setCode(outsourceWorkOrder.getCode());
            vo.setObjName(outsourceWorkOrder.getObjName());
            vo.setHandleResult(FaultResultEnum.getDescription(outsourceWorkOrder.getHandleResult()));
            vo.setActiveNodeName(StringUtils.isBlank(vo.getActiveNodeName()) ? "完成" : vo.getActiveNodeName());
        }
    }

    private OutsourceWorkOrder addOutsourceWorkOrder(ProcessStartedMessage message) {
        OutsourceWorkOrder outsourceWorkOrder = null;
        if (ObjectUtils.isNotEmpty(message) && ObjectUtils.isNotEmpty(message.getData())) {
            List<OutsourceWorkOrder> list = this.lambdaQuery().eq(OutsourceWorkOrder::getProcessInstanceId, message.getProcessInstanceId())
                    .list();
            final OutsourceWorkOrderDTO dto = JSONObject.parseObject(message.getData(), OutsourceWorkOrderDTO.class);
            ObjInfoDTO objInfoDTO = objInfoGainService.getObjInfo(dto.getOutsourceObj()[0], ObjTypeEnum.DEVICE.name());
            if (ObjectUtils.isEmpty(list)) {
                //查询编号尾数
                var now = LocalDateTime.now();
                long count =
                        this.lambdaQuery().between(OutsourceWorkOrder::getCreateTime, DateUtils.beginOfDay(now), DateUtils.endOfDay(now))
                                .count() + 1;
                String countCode = count > 99 ? String.valueOf(count) : count > 9 ? "0" + count : "00" + count;
                outsourceWorkOrder = OutsourceWorkOrder.builder()
                        .code("WW" + DateUtils.format(now, "yyyyMMdd") + countCode)
                        .objId(objInfoDTO.getId())
                        .objName(objInfoDTO.getObjName())
                        .processInstanceId(message.getProcessInstanceId())
                        .reason(dto.getReason())
                        .attachment(dto.getAttachment())
                        .identifyResult(dto.getIdentifyResult())
                        .identifyRemarks(dto.getIdentifyRemarks())
                        .companyId(ObjectUtils.isNotEmpty(dto.getCompanyId()) ? Long.parseLong(dto.getCompanyId()) : null)
                        .typeId(ObjectUtils.isNotEmpty(dto.getTypeId()) ? Long.parseLong(dto.getTypeId()) : null)
                        .handleResult(dto.getHandleResult())
                        .handleRemarks(dto.getHandleRemarks())
                        .appraise(dto.getAppraise())
                        .appraiseRemarks(dto.getAppraiseRemarks())
                        .appraiseAttachment(dto.getAppraiseAttachment())
                        .build();
                log.info("新增委外维修单:{}", JSON.toJSONString(outsourceWorkOrder));
                this.save(outsourceWorkOrder);
                sendDeviceLifeEvent(outsourceWorkOrder, FlowResult.ACTIVE.getDesc());
            } else {
                outsourceWorkOrder = list.get(0);
            }
        }
        return outsourceWorkOrder;
    }

    @Override
    public void sendDeviceLifeEvent(OutsourceWorkOrder outsourceWorkOrder, String state) {
        DeviceLifeEventMessage deviceLifeEventMessage = buildMessage(outsourceWorkOrder, state);
        RocketMQContextHolder.send(DeviceLifeEventMessage.TOPIC, deviceLifeEventMessage);
    }

    @Override
    public Long getRepairNumberByDeviceId(Long deviceInstanceId, Period period) {
        return this.lambdaQuery().eq(OutsourceWorkOrder::getObjId, deviceInstanceId)
                .between(ObjectUtils.isNotEmpty(period), OutsourceWorkOrder::getCreateTime,
                         ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd)).count();
    }

    private DeviceLifeEventMessage buildMessage(OutsourceWorkOrder outsourceWorkOrder, String state) {
        DeviceLifeEventMessage deviceLifeEventMessage = new DeviceLifeEventMessage();
        deviceLifeEventMessage.setEventId(outsourceWorkOrder.getId());
        deviceLifeEventMessage.setProcessInstanceId(outsourceWorkOrder.getProcessInstanceId());
        deviceLifeEventMessage.setDeviceIds(Sets.newHashSet(outsourceWorkOrder.getObjId()));
        deviceLifeEventMessage.setEventStatus(state);
        deviceLifeEventMessage.setEventName(outsourceWorkOrder.getObjName());
        deviceLifeEventMessage.setEventTime(LocalDateTime.now());
        deviceLifeEventMessage.setTenant(outsourceWorkOrder.getTenant());
        deviceLifeEventMessage.setEventType(DeviceLifeEventTypeEnum.OUTSOURCE_REPAIR.name());
        deviceLifeEventMessage.setEventTypeName(DeviceLifeEventTypeEnum.OUTSOURCE_REPAIR.getDescription());
        deviceLifeEventMessage.setApplicationCode(DeviceLifeEventTypeEnum.OUTSOURCE_REPAIR.name());
        deviceLifeEventMessage.setApplicationName(DeviceLifeEventTypeEnum.OUTSOURCE_REPAIR.getDescription());
        return deviceLifeEventMessage;
    }
}
