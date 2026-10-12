package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.impl;

import base.constant.VariableConstant;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.util.CustomCellWriteUtil;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.PageOfDataBoardDTO;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.dto.TaskLogVO;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.property.management.client.property.client.property.ConsumableManageClient;
import com.guhecloud.cube.property.management.client.property.param.consumable.ConsumableReduceStockParam;
import com.guhecloud.cube.property.management.client.property.vo.consumable.ConsumableUseInfo;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.ObjTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.ResourceEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketSourceTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.OrderHomePageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.FaultBatchDeleteCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dao.FaultOrderMapper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dto.FaultOrderDTO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration.FaultResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration.UrgencyLevelEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.paran.FaultTypeStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultCompletedMetricVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultOrderEventVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultOrderVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultTypeStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultyEquipmentTopVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultOrder;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultType;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.FaultTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultOrderPageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultTypeCollectionQuery;
import com.guhecloud.cube.srv.gen.maintain.server.fault.rocketmq.produce.FaultEventProduce;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import instance.command.ProcessStartedMessageAttrCommand;
import instance.command.SubmitInitFormCommand;
import instance.event.NodePassMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wql
 * @describe 故障单2.0
 * @date 2023/11/6 9:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultOrderServiceImpl extends ServiceImpl<FaultOrderMapper, FaultOrder> implements FaultOrderService {
    private final ProcessInstanceClient processInstanceClient;
    private final FaultTypeQueryService faultTypeQueryService;
    private final UserClient userClient;
    private final FaultOrderMapper faultOrderMapper;
    private final FlowClient flowClient;
    private final ConsumableManageClient consumableManageClient;

    @Override
    public synchronized FaultOrder addFaultOrder(ProcessStartedMessageAttrCommand message) {
        FaultOrder faultOrder = null;
        if (ObjectUtils.isNotEmpty(message) && ObjectUtils.isNotEmpty(message.getData())) {
            List<FaultOrder> list = this.lambdaQuery().eq(FaultOrder::getProcessInsId, message.getProcessInstanceId()).list();
            FaultOrderDTO faultOrderDTO = null;
            try {
                faultOrderDTO = JSONObject.parseObject(message.getData(), FaultOrderDTO.class);
            } catch (Exception e) {
                faultOrderDTO = new FaultOrderDTO();
            }
            if (ObjectUtils.isEmpty(faultOrderDTO)) {
                return faultOrder;
            }
            FaultOrderDTO.FaultObject objInfoDTO =
                    Objects.nonNull(faultOrderDTO.getFaultObj()) ? faultOrderDTO.getFaultObj() : new FaultOrderDTO.FaultObject();
            Long areaId = CollectionUtils.isNotEmpty(faultOrderDTO.getAreaId()) ? faultOrderDTO.getAreaId().getLast() : null;
            ProcessInstanceDTO instanceDTO = processInstanceClient.processInstanceGet(message.getProcessInstanceId());
            if (ObjectUtils.isEmpty(list)) {
                //查询编号尾数
                var now = LocalDateTime.now();
                long count =
                        this.lambdaQuery().between(FaultOrder::getCreateTime, DateUtils.beginOfDay(now), DateUtils.endOfDay(now)).count() +
                        1;
                String countCode = count > 99 ? String.valueOf(count) : count > 9 ? "0" + count : "00" + count;
                final String resource = (String) message.getProcessVariables().get("resource");
                faultOrder = FaultOrder.builder()
                        .code("GZ" + DateUtils.format(now, "yyyyMMdd") + countCode)
                        .processInsId(message.getProcessInstanceId())
                        .name(message.getInstanceName())
                        .submitStaff(message.getOutsiderName())
                        .submitTime(message.getStartTime())
                        .state(TicketStatusEnum.WAITING_CONFIRM.name())
                        .objId(objInfoDTO.getId())
                        .objName(StringUtils.isNotBlank(objInfoDTO.getName()) ? objInfoDTO.getName() : faultOrderDTO.getRepairObj())
                        .objType(faultOrderDTO.getObjType())
                        .areaId(areaId)
                        .faultTypeId(faultOrderDTO.getFaultType())
                        .faultDescribe(faultOrderDTO.getFaultDescribe())
                        .source(ObjectUtils.isEmpty(resource) ? ResourceEnum.MANUAL_SUBMISSION.getDesc()
                                                              : ResourceEnum.getDescription(resource))
                        .urgencyLevel(UrgencyLevelEnum.getDescription(faultOrderDTO.getUrgencyLevel()))
                        .result(StringUtils.isBlank(faultOrderDTO.getResult()) ? ""
                                                                               : FaultResultEnum.getDescription(faultOrderDTO.getResult()))
                        .applicationCode("FAULT")
                        .progress(Objects.nonNull(instanceDTO) ? instanceDTO.getActiveNodeName() : null)
                        .build();
                log.info("新增故障单:{}", JSON.toJSONString(faultOrder));
                this.save(faultOrder);
                FaultOrder faultOrder1 = this.lambdaQuery().eq(FaultOrder::getProcessInsId, message.getProcessInstanceId()).one();
                sendFaultEventMessage(faultOrder1, FlowResult.ACTIVE.getDesc());
            } else {
                faultOrder = list.get(0);
            }
            //耗材消费
            consumerConsumables(faultOrderDTO.getConsumable(), message.getProcessInstanceId(), message.getStartUserId(), message.getStartTime());
        }
        return faultOrder;
    }

    @Override
    public FaultOrder addComplaintOrder(ProcessStartedMessageAttrCommand message) {
        FaultOrder faultOrder = null;
        if (ObjectUtils.isNotEmpty(message) && ObjectUtils.isNotEmpty(message.getData())) {
            List<FaultOrder> list = this.lambdaQuery().eq(FaultOrder::getProcessInsId, message.getProcessInstanceId()).list();
            final FaultOrderDTO faultOrderDTO = JSONObject.parseObject(message.getData(), FaultOrderDTO.class);
            if (ObjectUtils.isEmpty(faultOrderDTO)) {
                return faultOrder;
            }
            if (ObjectUtils.isEmpty(list)) {
                //查询编号尾数
                var now = LocalDateTime.now();
                long count =
                        this.lambdaQuery().between(FaultOrder::getCreateTime, DateUtils.beginOfDay(now), DateUtils.endOfDay(now)).count() +
                        1;
                String countCode = count > 99 ? String.valueOf(count) : count > 9 ? "0" + count : "00" + count;
                faultOrder = FaultOrder.builder()
                        .code("TSJY" + DateUtils.format(now, "yyyyMMdd") + countCode)
                        .processInsId(message.getProcessInstanceId())
                        .name(message.getInstanceName())
                        .submitStaff(message.getOutsiderName())
                        .submitTime(message.getStartTime())
                        .state(TicketStatusEnum.WAITING_CONFIRM.name())
                        .objName(faultOrderDTO.getRepairObj())
                        .objType(faultOrderDTO.getObjType())
                        .areaId(CollectionUtils.isNotEmpty(faultOrderDTO.getAreaId()) ? faultOrderDTO.getAreaId().getLast() : null)
                        .faultTypeId(faultOrderDTO.getFaultType())
                        .urgencyLevel(UrgencyLevelEnum.getDescription(faultOrderDTO.getUrgencyLevel()))
                        .result(StringUtils.isBlank(faultOrderDTO.getResult()) ? ""
                                                                               : FaultResultEnum.getDescription(faultOrderDTO.getResult()))
                        .applicationCode("COMPLAINT")
                        .build();
                log.info("新增投诉建议单:{}", JSON.toJSONString(faultOrder));
                this.save(faultOrder);
            } else {
                faultOrder = list.get(0);
            }
        }
        return faultOrder;
    }

    @Override
    public Pagination<FaultOrderVO> personalPage(ProcessInstancePageQuery processInstancePageQuery) {
        Pagination<ProcessInstanceDTO> page = processInstanceClient.page(processInstancePageQuery);
        return PagingUtils.convert(page, coverPage(page));
    }

    @Override
    public Pagination<FaultOrderVO> allPage(FaultOrderPageQuery faultOrderPageQuery) {
        Period period = faultOrderPageQuery.getSubmitTime();
        List<String> noActivList = new ArrayList<>();
        if (FlowResult.ACTIVE.name().equals(faultOrderPageQuery.getStatus())) {
            noActivList = Stream.of(FlowResult.REJECT.name(), FlowResult.UNDO.name(), FlowResult.PASS.name()).collect(Collectors.toList());
            faultOrderPageQuery.setStatus(null);
        }
        Page<FaultOrder> faultOrderPage = this.lambdaQuery()
                .between(ObjectUtils.isNotNull(period), FaultOrder::getSubmitTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .eq(FaultOrder::getApplicationCode, faultOrderPageQuery.getBusinessType())
                .notIn(CollectionUtils.isNotEmpty(noActivList), FaultOrder::getState, noActivList)
                .eq(StringUtils.isNotBlank(faultOrderPageQuery.getStatus()), FaultOrder::getState, faultOrderPageQuery.getStatus())
                .eq(StringUtils.isNotBlank(faultOrderPageQuery.getFaultTypeId()), FaultOrder::getFaultTypeId, faultOrderPageQuery.getFaultTypeId())
                .eq(StringUtils.isNotBlank(faultOrderPageQuery.getResult()), FaultOrder::getResult, faultOrderPageQuery.getResult())
                .in(CollectionUtils.isNotEmpty(faultOrderPageQuery.getAreaIds()), FaultOrder::getAreaId, faultOrderPageQuery.getAreaIds())
                .and(StringUtils.isNotBlank(faultOrderPageQuery.getKeyword()), t ->
                        t
                                .like(StringUtils.isNotBlank(faultOrderPageQuery.getKeyword()), FaultOrder::getCode, faultOrderPageQuery.getKeyword())
                                .or()
                                .like(StringUtils.isNotBlank(faultOrderPageQuery.getKeyword()), FaultOrder::getName, faultOrderPageQuery.getKeyword())
                                .or()
                                .like(StringUtils.isNotBlank(faultOrderPageQuery.getKeyword()), FaultOrder::getFaultDescribe, faultOrderPageQuery.getKeyword())
                                .or()
                                .like(StringUtils.isNotBlank(faultOrderPageQuery.getKeyword()), FaultOrder::getObjName, faultOrderPageQuery.getKeyword()))
                .like(StringUtils.isNotBlank(faultOrderPageQuery.getProgress()), FaultOrder::getProgress, faultOrderPageQuery.getProgress())
                .last(" ORDER BY  CASE WHEN state IN ('REJECT', 'UNDO', 'PASS') THEN 1  ELSE 0  END ASC, create_time DESC ")
                .page(new Page<>(faultOrderPageQuery.getPageNumber(), faultOrderPageQuery.getPageSize()));
        //数据组装
        return PagingUtils.convert(faultOrderPage, this.buildResultPage(faultOrderPage, faultOrderPageQuery));
    }

    /**
     * 组装分页结果
     *
     * @param faultOrderPage
     * @param faultOrderPageQuery
     *
     * @return
     */
    private List<FaultOrderVO> buildResultPage(Page<FaultOrder> faultOrderPage, FaultOrderPageQuery faultOrderPageQuery) {
        List<FaultOrderVO> vos = new ArrayList<>();
        List<FaultOrder> updateList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(faultOrderPage.getRecords())) {
            // 获取流程实例id
            List<String> processInstanceIdList = faultOrderPage.getRecords().stream()
                    .map(FaultOrder::getProcessInsId)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
            String processInstanceIds = String.join(",", processInstanceIdList);
            ProcessInstancePageQuery processInstancePageQuery = new ProcessInstancePageQuery();
            processInstancePageQuery.setPageNumber(1);
            processInstancePageQuery.setPageSize(999);
            processInstancePageQuery.setProcessInstanceIds(processInstanceIds);
            Pagination<ProcessInstanceDTO> processInstanceDTOPagination = processInstanceClient.allPageBatch(processInstancePageQuery);
            Map<String, ProcessInstanceDTO> flowData = processInstanceDTOPagination.getRecords().stream()
                    .collect(Collectors.toMap(ProcessInstanceDTO::getProcessInstanceId, ProcessInstanceDTO -> ProcessInstanceDTO));
            Map<String, List<TaskLogVO>> historicNodeMap = CollectionUtils.isEmpty(processInstanceIdList)
                                                           ? Map.of() : processInstanceClient.getHistoricNode(processInstanceIdList);
            Map<Long, FaultType> faultTypeMap = faultTypeQueryService.list().stream().collect(Collectors.toMap(FaultType::getId, s -> s));
            faultOrderPage.getRecords().forEach(record -> {
                ProcessInstanceDTO processInstanceDTO = flowData.get(record.getProcessInsId());
                FaultOrderVO vo = ConvertUtils.convert(FaultOrderVO.class, record);
                if (faultTypeMap.containsKey(record.getFaultTypeId())) {
                    FaultType faultType = faultTypeMap.get(record.getFaultTypeId());
                    vo.setFaultType(ObjectUtils.isNotNull(faultType) ? faultType.getName() : "");
                }
                if (Objects.nonNull(processInstanceDTO)) {
                    vo.setActiveNodeName(
                            StringUtils.isBlank(processInstanceDTO.getActiveNodeName()) ? "完成" : processInstanceDTO.getActiveNodeName());
                    if (!StringUtils.equals(vo.getActiveNodeName(), record.getProgress())) {
                        record.setProgress(vo.getActiveNodeName());
                        updateList.add(record);
                    }
                    vo.setSubmitedTime(processInstanceDTO.getSubmitedTime());
                    vo.setStartUserInfo(processInstanceDTO.getStartUserInfo());
                    vo.setStartUserId(processInstanceDTO.getStartUserId());
                    vo.setStartUserName(processInstanceDTO.getStartUserName());
                    vo.setStatus(processInstanceDTO.getStatus());
                    vo.setInstanceName(processInstanceDTO.getInstanceName());
                    vo.setProcessInstanceId(processInstanceDTO.getProcessInstanceId());
                    setFaultOrderTimeFields(vo, processInstanceDTO, historicNodeMap.get(record.getProcessInsId()));
                }
                vos.add(vo);

            });
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            this.updateBatchById(updateList);
        }
        return vos;
    }

    private void setFaultOrderTimeFields(FaultOrderVO vo, ProcessInstanceDTO processInstanceDTO, List<TaskLogVO> taskLogVOS) {
        TaskLogVO firstTask = CollectionUtils.isEmpty(taskLogVOS) ? null : taskLogVOS.stream()
                .filter(taskLogVO -> Objects.nonNull(taskLogVO) && !VariableConstant.ROOT.equals(taskLogVO.getNodeKey()))
                .findFirst()
                .orElse(new TaskLogVO());
        LocalDateTime completeTime =
                FlowResult.FINISHED.name().equals(processInstanceDTO.getStatus()) ? processInstanceDTO.getUpdateTime() : null;
        vo.setConfirmTime(firstTask.getOperationTime());
        vo.setCompleteTime(completeTime);
        if (Objects.nonNull(firstTask.getOperationTime()) && Objects.nonNull(completeTime)) {
            vo.setTimeConsuming(DateUtils.between(firstTask.getOperationTime(), completeTime, ChronoUnit.MINUTES));
        }
    }

    @Override
    public FaultStatisticsVO faultStatistics(Period period) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        List<FaultOrder> list = this.lambdaQuery().between(ObjectUtils.isNotNull(period), FaultOrder::getCreateTime,
                                                           ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .eq(FaultOrder::getCreateBy, userId).list();
        FaultStatisticsVO statisticsVO = new FaultStatisticsVO();
        if (ObjectUtils.isNotEmpty(list)) {
            statisticsVO.setTotal(list.size());
            long count = list.stream().filter(o -> FaultResultEnum.RESOLVED.name().equals(o.getResult())).count();
            statisticsVO.setSuccess(Long.valueOf(count).intValue());
        }
        return statisticsVO;
    }

    public List<FaultOrderVO> coverPage(Pagination<ProcessInstanceDTO> page) {
        List<FaultOrderVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(record -> {
                FaultOrderVO vo = new FaultOrderVO();
                BeanUtils.copyProperties(record, vo);
                FaultOrder faultOrder = super.baseMapper.selectOne(new LambdaQueryWrapper<FaultOrder>()
                                                                           .eq(FaultOrder::getProcessInsId, record.getProcessInstanceId())
                                                                           .last("limit 1"));
                if (ObjectUtils.isEmpty(faultOrder)) {
                    //存在流引擎有而故障这边没有的数据需要向故障中补数据
                    ProcessStartedMessageAttrCommand message = processInstanceClient.getProcessInstanceFormInfo(record.getProcessInstanceId());
                    faultOrder = TicketSourceTypeEnum.FAULT.name().equals(message.getBusinessType()) ? addFaultOrder(message)
                                                                                                     : addComplaintOrder(message);
                }
                setListField(vo, faultOrder);
                vos.add(vo);
            });
        }
        return vos;
    }

    @Override
    public void updateResult(String processInstanceId, String data, FlowResult flowResult) {
        final FaultOrderDTO faultOrderDTO = JSONObject.parseObject(data, FaultOrderDTO.class);
        FaultOrderDTO.FaultObject objInfoDTO = Objects.nonNull(faultOrderDTO.getFaultObj())
                                               ? faultOrderDTO.getFaultObj() : new FaultOrderDTO.FaultObject();
        Long areaId = CollectionUtils.isNotEmpty(faultOrderDTO.getAreaId()) ? faultOrderDTO.getAreaId().getLast() : null;
        FaultOrder faultOrder = this.lambdaQuery().eq(FaultOrder::getProcessInsId, processInstanceId).one();
        final boolean update = this.lambdaUpdate().set(FaultOrder::getState, flowResult.name())
                .set(Objects.nonNull(faultOrderDTO.getCost()), FaultOrder::getCost, faultOrderDTO.getCost())
                .set(Objects.nonNull(objInfoDTO.getId()), FaultOrder::getObjId, objInfoDTO.getId())
                .set(StringUtils.isNotBlank(objInfoDTO.getName()), FaultOrder::getObjName, objInfoDTO.getName())
                .set(Objects.nonNull(areaId), FaultOrder::getAreaId, areaId)
                .set(Objects.nonNull(faultOrderDTO.getFaultType()), FaultOrder::getFaultTypeId, faultOrderDTO.getFaultType())
                .set(StringUtils.isNotBlank(faultOrderDTO.getUrgencyLevel()), FaultOrder::getUrgencyLevel, UrgencyLevelEnum.getDescription(faultOrderDTO.getUrgencyLevel()))
                .set(FaultOrder::getResult, FaultResultEnum.getDescription(faultOrderDTO.getResult()))
                .set(FaultOrder::getProgress, "完成")
                .eq(FaultOrder::getProcessInsId, processInstanceId).update();
        log.info("故障单流程审批结果更新结果:{},processInstanceId:{},result:{}", update, processInstanceId, flowResult.getDesc());
        sendFaultEventMessage(faultOrder, flowResult.getDesc());
    }

    /**
     * @Author wql
     * @Description 组装设备生命周期事件信息
     * @Date 16:28 2023/11/7
     **/
    private void sendFaultEventMessage(FaultOrder faultOrder, String faultStatus) {
        final FaultOrderEventVO eventVO = ObjectUtils.copying(faultOrder, FaultOrderEventVO.class);
        eventVO.setFaultStatus(faultStatus);
        if (eventVO.getObjId() != null) {
            FaultEventProduce.sendFaultEvent(eventVO);
        }
    }

    @Override
    public Page<FaultOrder> pageQuery(Paging page, List<String> ids) {
        return this.lambdaQuery().in(ObjectUtils.isNotEmpty(ids), FaultOrder::getProcessInsId, ids).page(PagingUtils.convert(page));
    }

    @Override
    public Pagination<FaultOrderVO> backlog(PageOfDataBoardDTO pageOfDataBoardDTO) {
        final Pagination<ProcessInstanceDTO> page = processInstanceClient.pageOfDataBoard(pageOfDataBoardDTO);
        return PagingUtils.convert(page, coverPage(page));
    }

    @Override
    public void updateFaultOrder(NodePassMessage message) {
        final FaultOrderDTO faultOrderDTO = JSONObject.parseObject(message.getData(), FaultOrderDTO.class);
        log.info("更新故障单,故障确认:{},解决结果:{}", faultOrderDTO.getConfirm(), faultOrderDTO.getResult());
        String state = StringUtils.isNotBlank(faultOrderDTO.getConfirm()) ? TicketStatusEnum.WAITING_FOR_HANDLE.name() : "";
        state = StringUtils.isNotBlank(faultOrderDTO.getResult()) ? TicketStatusEnum.TO_BE_CLOSED.name() : state;
        if (StringUtils.isNotBlank(state)) {
            FaultOrderDTO.FaultObject objInfoDTO = Objects.nonNull(faultOrderDTO.getFaultObj())
                                                   ? faultOrderDTO.getFaultObj() : new FaultOrderDTO.FaultObject();
            Long areaId = CollectionUtils.isNotEmpty(faultOrderDTO.getAreaId()) ? faultOrderDTO.getAreaId().getLast() : null;
            ProcessInstanceDTO instanceDTO = processInstanceClient.processInstanceGet(message.getProcessInstanceId());
            this.lambdaUpdate().set(FaultOrder::getState, state)
                    .set(FaultOrder::getCost, faultOrderDTO.getCost())
                    .set(Objects.nonNull(objInfoDTO.getId()), FaultOrder::getObjId, objInfoDTO.getId())
                    .set(StringUtils.isNotBlank(objInfoDTO.getName()), FaultOrder::getObjName, objInfoDTO.getName())
                    .set(Objects.nonNull(areaId), FaultOrder::getAreaId, areaId)
                    .set(Objects.nonNull(faultOrderDTO.getFaultType()), FaultOrder::getFaultTypeId, faultOrderDTO.getFaultType())
                    .set(StringUtils.isNotBlank(faultOrderDTO.getUrgencyLevel()), FaultOrder::getUrgencyLevel, UrgencyLevelEnum.getDescription(faultOrderDTO.getUrgencyLevel()))
                    .set(StringUtils.isNotBlank(faultOrderDTO.getResult()), FaultOrder::getResult, FaultResultEnum.getDescription(faultOrderDTO.getResult()))
                    .set(FaultOrder::getProgress, Objects.nonNull(instanceDTO) ? instanceDTO.getActiveNodeName() : null)
                    .eq(FaultOrder::getProcessInsId, message.getProcessInstanceId())
                    .ne(FaultOrder::getState, FlowResult.PASS.name())
                    .update();
            consumerConsumables(faultOrderDTO.getConsumable(), message.getProcessInstanceId(), message.getStartUserId(), message.getStartTime());
        }
    }

    /**
     * 消费耗材
     *
     * @param consumableUseInfos
     * @param message
     * @param startUserId
     * @param startTime
     */
    private void consumerConsumables(List<ConsumableUseInfo> consumableUseInfos, String message, Long startUserId,
                                     LocalDateTime startTime
                                    ) {
        //耗材消费
        if (CollectionUtils.isNotEmpty(consumableUseInfos)) {
            UserDTO user = Optional.ofNullable(userClient.getUser(startUserId)).orElse(new UserDTO());
            ConsumableReduceStockParam param = new ConsumableReduceStockParam();
            param.setClaimant(user.getId());
            param.setDeptId(Objects.nonNull(user.getOrganization()) ? user.getOrganization().getId() : null);
            param.setHandler(user.getId());
            param.setProcessInstanceId(message);
            param.setStoreId(consumableUseInfos.getFirst().getStoreId());
            param.setReduceDate(startTime);
            param.setDetails(consumableUseInfos);
            consumableManageClient.outboundSubmitClient(param);
        }
    }

    @Override
    public void updateComplaintOrder(String processInstanceId, String data, String state) {
        log.info("投诉建议工单更新");
        final FaultOrderDTO faultOrderDTO = JSONObject.parseObject(data, FaultOrderDTO.class);
        Long areaId = CollectionUtils.isNotEmpty(faultOrderDTO.getAreaId()) ? faultOrderDTO.getAreaId().getLast() : null;
        this.lambdaUpdate().set(FaultOrder::getState, state)
                .set(FaultOrder::getObjName, faultOrderDTO.getRepairObj())
                .set(FaultOrder::getObjType, faultOrderDTO.getObjType())
                .set(Objects.nonNull(areaId), FaultOrder::getAreaId, areaId)
                .set(FaultOrder::getFaultTypeId, faultOrderDTO.getFaultType())
                .set(FaultOrder::getUrgencyLevel, UrgencyLevelEnum.getDescription(faultOrderDTO.getUrgencyLevel()))
                .set(FaultOrder::getResult,
                     StringUtils.isBlank(faultOrderDTO.getResult()) ? "" : FaultResultEnum.getDescription(faultOrderDTO.getResult()))
                .eq(FaultOrder::getProcessInsId, processInstanceId)
                .ne(FaultOrder::getState, FlowResult.PASS.name())
                .update();
    }

    @Override
    public List<FaultyEquipmentTopVO> getFaultyEquipmentTop(Period period) {
        return faultOrderMapper.getFaultyEquipmentTop(period);
    }

    @Override
    public List<DeviceOperationStatisticsDTO> deviceFaultNumStatistics(DeviceStatisticParam param) {
        if (ObjectUtils.isNotEmpty(param.getDeviceIds())) {
            return faultOrderMapper.deviceFaultNumStatistics(param);
        }
        return new ArrayList<>();
    }

    @Override
    public Long isFaultValid(String deviceId, Period period) {
        return this.lambdaQuery().eq(FaultOrder::getObjType, ObjTypeEnum.DEVICE.name())
                .eq(FaultOrder::getObjId, deviceId)
                .between(ObjectUtils.isNotEmpty(period), FaultOrder::getSubmitTime,
                         ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd)).count();
    }

    @Override
    public OrderHomePageVO faultHomePageStatistics(BaseDateStatisticsParam param) {
        Assert.parameterIsNull(param.getPeriod(), "请选则时间范围");
        //总数
        Long total = this.lambdaQuery()
                .between(FaultOrder::getCreateTime, param.getPeriod().getBegin(),
                         param.getPeriod().getEnd())
                .in(CollectionUtils.isNotEmpty(param.getAreaIds()), FaultOrder::getAreaId, param.getAreaIds())
                .count();
        //环比
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        Long ringNum = this.lambdaQuery()
                .between(FaultOrder::getCreateTime, previousPeriod.getBegin(),
                         previousPeriod.getEnd())
                .in(CollectionUtils.isNotEmpty(param.getAreaIds()), FaultOrder::getAreaId, param.getAreaIds())
                .count();
        //完成数
        Long finishedNum = this.lambdaQuery()
                .between(FaultOrder::getCreateTime, param.getPeriod().getBegin(),
                         param.getPeriod().getEnd())
                .in(CollectionUtils.isNotEmpty(param.getAreaIds()), FaultOrder::getAreaId, param.getAreaIds())
                .eq(FaultOrder::getState, TicketStatusEnum.FINISHED.name())
                .count();
        //处理中
        Long processCount = this.lambdaQuery()
                .between(FaultOrder::getCreateTime, param.getPeriod().getBegin(),
                         param.getPeriod().getEnd())
                .in(CollectionUtils.isNotEmpty(param.getAreaIds()), FaultOrder::getAreaId, param.getAreaIds())
                .ne(FaultOrder::getState, TicketStatusEnum.FINISHED.name())
                .count();
        return OrderHomePageVO.builder()
                .orderCount(total)
                .orderRatio(MathUtils.calculateRingRatioPercentage(total, ringNum).floatValue())
                .processOrder(processCount)
                .finishOrder(finishedNum)
                .build();
    }

    @Override
    public Pagination<FaultTypeStatisticsVO> faultTypeStatistics(FaultTypeStatisticsParam param) {
        List<FaultTypeStatisticsVO> records = this.getFaultTypeStatisticsExtend(param);
        if (ObjectUtils.isNull(records)) {
            return PagingUtils.convert(new Page<>(param.getPageNumber(), param.getPageSize()));
        }
        Page<FaultTypeStatisticsVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page.setTotal(records.size());
        Integer maxSize = param.getPageNumber() * param.getPageSize();
        Integer minSize = param.getPageSize() * (param.getPageNumber() - 1);
        if (records.size() > minSize) {
            page.setRecords(records.subList(minSize, records.size() < maxSize ? records.size() : maxSize));
        }
        return PagingUtils.convert(page);
    }

    @Override
    public void faultTypeExcelStatistics(FaultTypeStatisticsParam param) {
        List<FaultTypeStatisticsVO> records = this.getFaultTypeStatisticsExtend(param);
        Assert.parameterIsNull(records, "暂无数据，无法导出");
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = null;
        try {
            excelName = URLEncoder.encode("故障报修工单统计", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), FaultTypeStatisticsVO.class)
                    .registerWriteHandler(new CustomCellWriteUtil())
                    .sheet("故障报修工单统计")
                    .doWrite(records);
        } catch (Exception e) {
            log.debug("maintain:故障报修工单导出异常：", e);
            throw new ServiceException("下载异常");
        }
    }

    @Override
    public void checkFirstSubmit(SubmitInitFormCommand formFlowCommand) {
        FormDataParam formDataParam = new FormDataParam();
        formDataParam.setFormId(formFlowCommand.getFormDefinitionId());
        formDataParam.setFormData(formFlowCommand.getData());
        String data = flowClient.formParse(formDataParam);
        try {
            FaultOrderDTO faultOrderDTO = JSONObject.parseObject(data, FaultOrderDTO.class);
            Assert.isNotAcceptable(Objects.isNull(faultOrderDTO), "未绑定表单数据");
        } catch (Exception e) {
            throw new ServiceException("表单数据绑定失败");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteFault(FaultBatchDeleteCommand deleteCommand) {
        if (CollectionUtils.isEmpty(deleteCommand.getProcessInstanceIds())) {
            return;
        }
        List<FaultOrder> list = this.lambdaQuery().in(FaultOrder::getProcessInsId).list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        DatabaseContextHolder.softDelete(FaultOrder.class, list);
        flowClient.deleteFormFlow(deleteCommand.getProcessInstanceIds());
    }

    @Override
    public FaultCompletedMetricVO faultCompletedMetic(Period period, String type) {
        //总数
        Long total = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(type), FaultOrder::getObjType, type)
                .between(FaultOrder::getCreateTime, period.getBegin(),
                         period.getEnd()).count();
        //完成数
        Long finishedNum = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(type), FaultOrder::getObjType, type)
                .between(FaultOrder::getCreateTime, period.getBegin(),
                         period.getEnd())
                .eq(FaultOrder::getState, FlowResult.PASS.name())
                .count();
        return FaultCompletedMetricVO.builder()
                .total(total)
                .completed(finishedNum)
                .rate(MathUtils.calculateProportionPercentage(finishedNum, total).floatValue())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFaultOrder(List<String> processInstanceIdList) {
        long timeMillis = System.currentTimeMillis();
        //删除流程实例
        this.baseMapper.update(null, Wrappers.<FaultOrder>lambdaUpdate()
                                       .set(FaultOrder::getDeleted, timeMillis)
                                       .set(FaultOrder::getUpdateTime, LocalDateTime.now())
                                       .in(FaultOrder::getProcessInsId, processInstanceIdList)
                              );
    }

    @Override
    public Set<String> objFaultOrderStatistics(DeviceStatisticParam param) {
        List<FaultOrder> list = this.lambdaQuery().between(ObjectUtils.isNotEmpty(param.getPeriod()), FaultOrder::getSubmitTime,
                                                           ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .in(ObjectUtils.isNotEmpty(param.getDeviceIds()), FaultOrder::getObjId, param.getDeviceIds())
                .select(FaultOrder::getProcessInsId).list();
        if (ObjectUtils.isNotEmpty(list)) {
            return list.stream().map(FaultOrder::getProcessInsId).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    @Override
    public Map<Long, Long> countUnfinishedByAreaIds(List<Long> areaIds) {
        Map<Long, Long> resultMap = initAreaCountMap(areaIds);
        if (resultMap.isEmpty()) {
            return resultMap;
        }
        List<FaultOrder> faultOrders = this.lambdaQuery()
                .select(FaultOrder::getAreaId)
                .in(FaultOrder::getAreaId, resultMap.keySet())
                .ne(FaultOrder::getState, TicketStatusEnum.FINISHED.name())
                .list();
        if (CollectionUtils.isEmpty(faultOrders)) {
            return resultMap;
        }
        Map<Long, Long> countMap = faultOrders.stream()
                .map(FaultOrder::getAreaId)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(areaId -> areaId, Collectors.counting()));
        resultMap.putAll(countMap);
        return resultMap;
    }

    @Override
    public Map<Long, Long> countByAreaIds(List<Long> areaIds) {
        Map<Long, Long> resultMap = initAreaCountMap(areaIds);
        if (resultMap.isEmpty()) {
            return resultMap;
        }
        List<FaultOrder> faultOrders = this.lambdaQuery()
                .select(FaultOrder::getAreaId)
                .in(FaultOrder::getAreaId, resultMap.keySet())
                .list();
        if (CollectionUtils.isEmpty(faultOrders)) {
            return resultMap;
        }
        Map<Long, Long> countMap = faultOrders.stream()
                .map(FaultOrder::getAreaId)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(areaId -> areaId, Collectors.counting()));
        resultMap.putAll(countMap);
        return resultMap;
    }

    private Map<Long, Long> initAreaCountMap(List<Long> areaIds) {
        Map<Long, Long> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(areaIds)) {
            return resultMap;
        }
        areaIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .forEach(areaId -> resultMap.put(areaId, 0L));
        return resultMap;
    }

    private void setListField(FaultOrderVO vo, FaultOrder faultOrder) {
        if (ObjectUtils.isNotEmpty(faultOrder)) {
            vo.setCode(faultOrder.getCode());
            vo.setName(faultOrder.getName());
            vo.setObjName(faultOrder.getObjName());
            vo.setAreaId(faultOrder.getAreaId());
            FaultType faultType = faultTypeQueryService.getById(faultOrder.getFaultTypeId());
            vo.setFaultType(ObjectUtils.isNotNull(faultType) ? faultType.getName() : "");
            vo.setSource(faultOrder.getSource());
            vo.setResult(faultOrder.getResult());
            vo.setUrgencyLevel(faultOrder.getUrgencyLevel());
            vo.setActiveNodeName(StringUtils.isBlank(vo.getActiveNodeName()) ? "完成" : vo.getActiveNodeName());
        }
    }

    /**
     * 工单数据扩展
     */
    private List<FaultTypeStatisticsVO> getFaultTypeStatisticsExtend(FaultTypeStatisticsParam param) {
        var begin = ObjectUtils.isNotEmpty(param.getPeriod()) && ObjectUtils.isNotEmpty(param.getPeriod().getBegin()) ? param.getPeriod()
                .getBegin() : null;
        var end =
                ObjectUtils.isNotEmpty(param.getPeriod()) && ObjectUtils.isNotEmpty(param.getPeriod().getEnd()) ? param.getPeriod().getEnd()
                                                                                                                : null;
        List<FaultOrder> faultOrders = this.faultOrderMapper.selectList(new LambdaQueryWrapper<FaultOrder>()
                                                                                .isNotNull(FaultOrder::getFaultTypeId)
                                                                                .eq(FaultOrder::getApplicationCode, param.getApplicationCode())
                                                                                .in(ObjectUtils.isNotEmpty(param.getFaultTypeId()), FaultOrder::getFaultTypeId, param.getFaultTypeId())
                                                                                .between(ObjectUtils.isNotEmpty(begin) &&
                                                                                         ObjectUtils.isNotEmpty(end), FaultOrder::getSubmitTime, begin, end)
                                                                                .orderByAsc(FaultOrder::getFaultTypeId));
        if (ObjectUtils.isEmpty(faultOrders)) {
            return null;
        }
        Integer total;
        List<ProcessInstanceDTO> data = new ArrayList<>();
        ProcessInstancePageQuery flowParam = new ProcessInstancePageQuery();
        flowParam.setPageSize(1000);
        flowParam.setPageNumber(0);
        flowParam.setApplicationCode(param.getApplicationCode());
        flowParam.setSubmitTime(param.getPeriod());
        Pagination<ProcessInstanceDTO> flowData = null;
        do {
            flowParam.setPageNumber(flowParam.getPageNumber() + 1);
            total = flowParam.getPageNumber() * flowParam.getPageSize();
            flowData = processInstanceClient.allPage(flowParam);
            data.addAll(flowData.getRecords());
        } while (flowData.getTotal() > total);
        List<FaultTypeStatisticsVO> records = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(data)) {
            FaultTypeCollectionQuery faultTypeCollectionQuery = new FaultTypeCollectionQuery();
            faultTypeCollectionQuery.setApplicationCode(param.getApplicationCode());
            List<FaultType> faultTypeList = faultTypeQueryService.listFaultType(faultTypeCollectionQuery);
            Map<Long, String> faultTypeNameMap = faultTypeList.stream().collect(Collectors.toMap(FaultType::getId, FaultType::getName));
            Map<Long, List<FaultOrder>> faultOrderMap = faultOrders.stream().collect(Collectors.groupingBy(FaultOrder::getFaultTypeId));
            Map<String, ProcessInstanceDTO> processInstanceMap = data.stream()
                    .collect(Collectors.toMap(ProcessInstanceDTO::getProcessInstanceId, ProcessInstanceDTO -> ProcessInstanceDTO));
            for (Map.Entry<Long, List<FaultOrder>> entry : faultOrderMap.entrySet()) {
                FaultTypeStatisticsVO dto = new FaultTypeStatisticsVO();
                AtomicReference<Boolean> bool = new AtomicReference<>(false);
                entry.getValue().forEach(ex -> {
                    dto.setObjName(faultTypeNameMap.get(ex.getFaultTypeId()));
                    ProcessInstanceDTO processInstanceDTO = processInstanceMap.get(ex.getProcessInsId());
                    var flowResultBool =
                            !ObjectUtils.isNotEmpty(processInstanceDTO) || FlowResult.UNDO.name().equals(processInstanceDTO.getStatus()) ||
                            FlowResult.REJECT.name().equals(processInstanceDTO.getStatus());
                    if (!flowResultBool) {
                        bool.set(true);
                        dto.setTotalNumber(dto.getTotalNumber() + 1);
                        if (processInstanceDTO.getStatus().equals("FINISHED")) {
                            dto.setDealNumber(dto.getDealNumber() + 1);
                        } else {
                            dto.setWaitNumber(dto.getWaitNumber() + 1);
                        }
                        if (ObjectUtils.isNotEmpty(ex.getResult()) && "已解决".equals(ex.getResult())) {
                            dto.setResultNumber(dto.getResultNumber() + 1);
                        }
                        if (ObjectUtils.isNotEmpty(processInstanceDTO.getCreateTime()) &&
                            ObjectUtils.isNotEmpty(processInstanceDTO.getUpdateTime())) {
                            Long minute = DateUtils.between(processInstanceDTO.getCreateTime(), processInstanceDTO.getUpdateTime(), ChronoUnit.MINUTES);
                            dto.setTotalWaitTime(ObjectUtils.isNotEmpty(dto.getTotalWaitTime()) ? dto.getTotalWaitTime() + minute : minute);
                        }
                    }
                });
                if (ObjectUtils.isNotEmpty(bool) && bool.get()) {
                    dto.getExtend();
                    records.add(dto);
                }
            }
        }
        return records;
    }

}
