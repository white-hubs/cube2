package com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.dao.DoorDeviceMapper;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.dao.EventRecordMapper;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.entity.DoorDevice;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.entity.EventRecord;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.JieShunApi;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request.Device;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request.DoorControlRequest;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request.DoorEventUploadRequest;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request.DoorStatusUploadRequest;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request.Person;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.response.JieShunResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums.ControTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums.DeviceStatusEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums.EventTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums.OpenModelEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums.RecordTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessPointsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.JieShunAccessService;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessPointVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 捷顺门禁对接文档：
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JieShunAccessServiceImpl implements JieShunAccessService {
    private static final String DOOR_CONTROL_URI = "/door/opendoor";
    private final EventRecordMapper eventRecordMapper;
    private final DoorDeviceMapper doorDeviceMapper;
    private final JieShunApi jieShunApi;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static LambdaQueryWrapper<EventRecord> buildQueryWrapper(DoorEventsParam doorEventsParam) {
        LambdaQueryWrapper<EventRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(doorEventsParam.getStartTime() != null && doorEventsParam.getEndTime() !=
                                                                       null, EventRecord::getEventTime, doorEventsParam.getStartTime(), doorEventsParam.getEndTime());
        queryWrapper.in(ListUtils.nonEmpty(doorEventsParam.getDoorIndexCodes()), EventRecord::getDoorIndexCode, doorEventsParam.getDoorIndexCodes());

        return queryWrapper;
    }

    private static Page<EventRecord> buildPage(DoorEventsParam doorEventsParam) {
        Page<EventRecord> page = new Page<>();
        page.setSize(doorEventsParam.getPageSize());
        page.setCurrent(doorEventsParam.getPageNo());
        return page;
    }

    private static List<DoorControlResultVo> getAsyncDoorControlResult(List<CompletableFuture<DoorControlResultVo>> futureList) {
        return futureList.stream()
                .map(future -> {
                    try {
                        return future.get(5, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        return new DoorControlResultVo("", "TIMEOUT", "门禁请求超时");
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<AccessPointVo> getDoorPoints(AccessPointsParam accessPointsParam) {
        return null;
    }

    @Override
    public Page<DoorDevicesVo> getDoorDevices(AccessDevicesParam accessDevicesParam) {
        return null;
    }

    @Override
    public Page<DoorEventsVo> getDoorEvents(DoorEventsParam doorEventsParam) throws Exception {

        throw new UnsupportedOperationException("门禁事件已有门禁上报保存,无需返回数据");
    }

    @Override
    public List<DoorControlResultVo> doorControl(DoorControlParam param) {
        log.info("捷顺门禁控制，参数为：{}", JsonUtils.serialize(param));
        List<String> doorIndexCodes = param.getDoorIndexCodes();
        Assert.isParameterError(ListUtils.isEmpty(doorIndexCodes), "门禁编号不能为空");
        ControTypeEnum controTypeEnum = ControTypeEnum.build(param.getControlType());
        //构建请求体
        List<DoorControlRequest> requestList = ListUtils.convertItem(doorIndexCodes, index -> this.doorIndexConvertDoorControlRequest(index, controTypeEnum));
        //转换为异步执行器
        List<CompletableFuture<DoorControlResultVo>> futureList = ListUtils.convertItem(requestList, this::futureRequestDoorControl);
        // 并行合并结果 + 超时控制
        CompletableFuture<List<DoorControlResultVo>> resultFuture = CompletableFuture
                // 并行执行
                .allOf(futureList.toArray(new CompletableFuture[0]))
                //获取异步执行结果
                .thenApply(v -> getAsyncDoorControlResult(futureList));
        //获取执行的结果
        return resultFuture.join();
    }

    @Override
    public List<AuthDoorStatusVo> getDoorStates(List<String> ids) {

        log.info("捷顺门禁状态参数，参数为：{}", JsonUtils.serialize(ids));
        if (ListUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        //设置查询参数
        LambdaQueryWrapper<DoorDevice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(!CollectionUtils.isEmpty(ids), DoorDevice::getOutBusinessId, ids);
        List<DoorDevice> doorDevices = doorDeviceMapper.selectList(lambdaQueryWrapper);
        return doorDevices.stream().map(this::convertToDoorStatsVo).collect(Collectors.toList());

    }

    @Override
    public JieShunResponse<Void> uploadDoorEvent(DoorEventUploadRequest request) {
        //接收请求，转换为对应的实体
        EventRecord eventRecord = convertRequestToEventType(request);
        String eventId = eventRecord.getEventId();
        Assert.isParameterError(StringUtils.isEmpty(eventId), "捷顺门禁上报的事件id为空");
        long count = eventRecordMapper.selectCount(new LambdaQueryWrapper<EventRecord>().eq(EventRecord::getEventId, eventId));
        if (count > 0) {
            eventRecordMapper.update(eventRecord, new LambdaUpdateWrapper<EventRecord>().eq(EventRecord::getEventId, eventId));
            log.info("捷顺门禁事件上传,事件id:{},数据库已存在该事件,进行更新", eventId);
            return JieShunResponse.success(null);
        }
        //写入数据库
        int insert = eventRecordMapper.insert(eventRecord);
        log.info("捷顺门禁事件上传,写入数据库结果：{}", insert);
        //返回结果
        return insert > 0 ? JieShunResponse.success(null) : JieShunResponse.fail("上传失败");
    }

    /**
     * 由前端直接调用物联,不走后端
     *
     * @param request
     *
     * @return
     */
    @Deprecated
    @Override
    public JieShunResponse<Void> uploadDoorStatus(DoorStatusUploadRequest request) {
        //接收请求，转换为对应的实体
        Integer status = DeviceStatusEnum.getReturnStatus(request.getDeviceStatus());
        String deviceGuid = request.getDeviceGuid();
        return ObjectUtils.getOrElse(
                //判断是否满足条件
                StringUtils.isNotBlank(deviceGuid)
                , () -> {
                    //修改数据库
                    LambdaUpdateWrapper<DoorDevice> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(DoorDevice::getOutBusinessId, deviceGuid);
                    updateWrapper.set(DoorDevice::getStatus, status);
                    int update = doorDeviceMapper.update(new DoorDevice(), updateWrapper);
                    //返回结果
                    JieShunResponse<Void> success = JieShunResponse.success();
                    JieShunResponse<Void> fail = JieShunResponse.fail("上传失败");
                    return update > 0 ? success : fail;
                }, JieShunResponse.fail("上传失败"));

    }

    private AuthDoorStatusVo convertToDoorStatsVo(DoorDevice doorDevice) {
        return AuthDoorStatusVo.builder()
                .doorIndexCode(doorDevice.getDoorCode())
                .doorState(Integer.valueOf(doorDevice.getStatus()))
                .build();
    }

    private EventRecord convertRequestToEventType(@NonNull DoorEventUploadRequest request) {
        try {
            LocalDateTime eventTime = DateUtils.parse(request.getCrossTime());
            Person person = request.getPerson();
            Device device = request.getDevice();
            EventRecord eventRecord = new EventRecord();
            eventRecord.setId(IdWorker.getId());
            eventRecord.setEventId(request.getRecordId());
            eventRecord.setEventName(RecordTypeEnum.getDescBy(request.getRecordType()));
            eventRecord.setEventTime(eventTime);
            eventRecord.setPersonId(person.getPersonId());
            eventRecord.setPersonName(person.getPersonName());
            eventRecord.setDoorIndexCode(device.getDeviceGuid());
            eventRecord.setDoorName(device.getDeviceName());
            eventRecord.setEventType(RecordTypeEnum.getDescBy(request.getRecordType()));
            eventRecord.setInAndOutType(null);
            eventRecord.setEventResult(EventTypeEnum.getEventResult(request.getEventType()));
            eventRecord.setEventWay(EventTypeEnum.getDescBy(request.getEventType()));
            eventRecord.setCardNo(request.getCardNo());
            log.info("捷顺门禁事件上传:转换结果：{}", JsonUtils.serialize(eventRecord));
            return eventRecord;
        } catch (Exception e) {
            throw new ServiceException("捷顺门禁事件上传,类型转换错误:%s", e.getMessage());
        }
    }

    private CompletableFuture<DoorControlResultVo> futureRequestDoorControl(DoorControlRequest request) {
        Requester requester = SessionContextHolder.getRequester();

        return CompletableFuture.supplyAsync(() -> {
            try {
                SessionContextHolder.setRequester(requester);
                JieShunResponse<Void> result = jieShunApi.jieShunAuthPostApi(DOOR_CONTROL_URI, request);
                return new DoorControlResultVo(
                        request.getDeviceGuid(),
                        result.getCode(),
                        result.getMessage()
                );
            } catch (Exception e) {  //捕获所有异常
                return new DoorControlResultVo(
                        request.getDeviceGuid(),
                        "SYSTEM_ERROR",
                        e.getMessage()
                );
            }
        }, threadPoolTaskExecutor);// 使用自定义线程池
    }

    private DoorControlRequest doorIndexConvertDoorControlRequest(String index, ControTypeEnum controTypeEnum) {
        return DoorControlRequest.builder().deviceGuid(index).openModel(OpenModelEnum.getBy(controTypeEnum).getCode()).build();
    }
}
