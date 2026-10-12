package com.guhecloud.cube.srv.gen.maintain.server.retrofit.commandgateway.impl;

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
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.commandgateway.RetrofitOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.dao.RetrofitOrderMapper;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.dto.RetrofitOrderDTO;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.enumeration.RetrofitResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.interfaces.vo.RetrofitOrderVO;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.po.RetrofitOrder;
import com.guhecloud.platform.base.model.Pagination;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RetrofitOrderServiceImpl extends ServiceImpl<RetrofitOrderMapper, RetrofitOrder> implements RetrofitOrderService {

    private final ProcessInstanceClient processInstanceClient;

    private final ObjInfoGainService objInfoGainService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RetrofitOrder addRetrofitOrder(ProcessStartedMessage message) {
        RetrofitOrder retrofitOrder = null;
        if (ObjectUtils.isNotEmpty(message) && ObjectUtils.isNotEmpty(message.getData())) {
            List<RetrofitOrder> list = this.lambdaQuery().eq(RetrofitOrder::getProcessInstanceId, message.getProcessInstanceId()).list();
            final RetrofitOrderDTO retrofitOrderDTO = JSONObject.parseObject(message.getData(), RetrofitOrderDTO.class);
            ObjInfoDTO objInfoDTO = objInfoGainService.getObjInfo(retrofitOrderDTO.getRetrofitObj()[0], ObjTypeEnum.DEVICE.name());
            if (ObjectUtils.isEmpty(list)) {
                //查询编号尾数
                var now = LocalDateTime.now();
                long count =
                        this.lambdaQuery().between(RetrofitOrder::getCreateTime, DateUtils.beginOfDay(now), DateUtils.endOfDay(now))
                                .count() +
                        1;
                String countCode = count > 99 ? String.valueOf(count) : count > 9 ? "0" + count : "00" + count;
                retrofitOrder = RetrofitOrder.builder()
                        .code("GX" + DateUtils.format(now, "yyyyMMdd") + countCode)
                        .processInstanceId(message.getProcessInstanceId())
                        .objId(objInfoDTO.getId())
                        .objName(objInfoDTO.getObjName())
                        .reason(retrofitOrderDTO.getReason())
                        .expectedResolutionDate(retrofitOrderDTO.getExpectedResolutionDate())
                        .attachment(retrofitOrderDTO.getAttachment())
                        .retrofitBeforeStatus(retrofitOrderDTO.getRetrofitBeforeStatus())
                        .identifyResult(retrofitOrderDTO.getIdentifyResult())
                        .handleResult(retrofitOrderDTO.getHandleResult())
                        .retrofitAfterStatus(retrofitOrderDTO.getRetrofitAfterStatus())
                        .build();
                log.info("新增更新改造单:{}", JSON.toJSONString(retrofitOrder));
                this.save(retrofitOrder);
                sendDeviceLifeEvent(retrofitOrder, FlowResult.ACTIVE.getDesc());
            } else {
                retrofitOrder = list.get(0);
            }
        }
        return retrofitOrder;
    }

    @Override
    public RetrofitOrder updateRetrofitOrder(RetrofitOrderDTO retrofitOrderDTO, String processInstanceId) {
        final boolean update = this.lambdaUpdate()
                .set(RetrofitOrder::getReason, retrofitOrderDTO.getReason())
                .set(RetrofitOrder::getExpectedResolutionDate, retrofitOrderDTO.getExpectedResolutionDate())
                .set(RetrofitOrder::getAttachment, retrofitOrderDTO.getAttachment())
                .set(RetrofitOrder::getRetrofitBeforeStatus, retrofitOrderDTO.getRetrofitBeforeStatus())
                .set(RetrofitOrder::getIdentifyResult, retrofitOrderDTO.getIdentifyResult())
                .set(RetrofitOrder::getHandleResult, retrofitOrderDTO.getHandleResult())
                .set(RetrofitOrder::getRetrofitAfterStatus, retrofitOrderDTO.getRetrofitAfterStatus())
                .eq(RetrofitOrder::getProcessInstanceId, processInstanceId).update();
        log.info("更新改造单流程审批结果更新结果:{},processInstanceId:{}", update, processInstanceId);
        return this.lambdaQuery().eq(RetrofitOrder::getProcessInstanceId, processInstanceId).one();
    }

    @Override
    public Pagination<RetrofitOrderVO> retrofitOrderPage(ProcessInstancePageQuery processInstancePageQuery) {
        Pagination<ProcessInstanceDTO> page = processInstanceClient.page(processInstancePageQuery);
        return PagingUtils.convert(page, coverPage(page));
    }

    public List<RetrofitOrderVO> coverPage(Pagination<ProcessInstanceDTO> page) {
        List<RetrofitOrderVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(record -> {
                RetrofitOrderVO vo = new RetrofitOrderVO();
                BeanUtils.copyProperties(record, vo);
                List<RetrofitOrder> list = this.lambdaQuery().eq(RetrofitOrder::getProcessInstanceId, record.getProcessInstanceId()).list();
                RetrofitOrder retrofitOrder;
                if (ObjectUtils.isNotEmpty(list)) {
                    retrofitOrder = list.get(0);
                } else {
                    //存在流引擎有而更新改造这边没有的数据需要向更新改造中补数据
                    ProcessStartedMessage message = processInstanceClient.getProcessInstanceFormInfo(record.getProcessInstanceId());
                    retrofitOrder = addRetrofitOrder(message);
                }
                setListField(vo, retrofitOrder);
                vos.add(vo);
            });
        }
        return vos;
    }

    private void setListField(RetrofitOrderVO vo, RetrofitOrder retrofitOrder) {
        if (ObjectUtils.isNotEmpty(retrofitOrder)) {
            vo.setCode(retrofitOrder.getCode());
            vo.setObjId(retrofitOrder.getObjId());
            vo.setObjName(retrofitOrder.getObjName());
            vo.setReason(retrofitOrder.getReason());
            vo.setExpectedResolutionDate(retrofitOrder.getExpectedResolutionDate());
            vo.setIdentifyResult(retrofitOrder.getIdentifyResult());
            vo.setIdentifyRemarks(retrofitOrder.getIdentifyRemarks());
            vo.setHandleResult(retrofitOrder.getHandleResult());
            if (StringUtils.isNotBlank(retrofitOrder.getHandleResult())) {
                vo.setHandleResultName(RetrofitResultEnum.valueOf(retrofitOrder.getHandleResult()).getDesc());
            }
            vo.setHandleRemarks(retrofitOrder.getHandleRemarks());
            vo.setActiveNodeName(StringUtils.isBlank(vo.getActiveNodeName()) ? "完成" : vo.getActiveNodeName());
        }
    }

    @Override
    public void sendDeviceLifeEvent(RetrofitOrder retrofitOrder, String state) {
        DeviceLifeEventMessage deviceLifeEventMessage = buildMessage(retrofitOrder, state);
        RocketMQContextHolder.send(DeviceLifeEventMessage.TOPIC, deviceLifeEventMessage);
    }

    private DeviceLifeEventMessage buildMessage(RetrofitOrder retrofitOrder, String state) {
        DeviceLifeEventMessage deviceLifeEventMessage = new DeviceLifeEventMessage();
        deviceLifeEventMessage.setEventId(retrofitOrder.getId());
        deviceLifeEventMessage.setProcessInstanceId(retrofitOrder.getProcessInstanceId());
        deviceLifeEventMessage.setDeviceIds(Sets.newHashSet(retrofitOrder.getObjId()));
        deviceLifeEventMessage.setEventStatus(state);
        deviceLifeEventMessage.setEventName(retrofitOrder.getObjName());
        deviceLifeEventMessage.setEventTime(LocalDateTime.now());
        deviceLifeEventMessage.setTenant(retrofitOrder.getTenant());
        deviceLifeEventMessage.setEventType(DeviceLifeEventTypeEnum.RETROFIT.name());
        deviceLifeEventMessage.setEventTypeName(DeviceLifeEventTypeEnum.RETROFIT.getDescription());
        deviceLifeEventMessage.setApplicationCode(DeviceLifeEventTypeEnum.RETROFIT.name());
        deviceLifeEventMessage.setApplicationName(DeviceLifeEventTypeEnum.RETROFIT.getDescription());
        return deviceLifeEventMessage;
    }
}
