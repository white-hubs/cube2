package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectItemTemplateCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectResultCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordItemQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectionItemTagEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordDataMode;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordItemDataMode;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordItemStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.ErrItemTagRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordCancelBatchCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordItemQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordListSubmitCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordSubmitCheckParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordSubmitCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordToBusinessCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTempListSubmitCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionRecordErrorParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TargetMaxRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTaskRecordSubmitCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectItemTemplatePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectResultPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.param.InspectOfflineSubmitStatusParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.result.InspectOfflineSubmitItemResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.service.InspectOfflineSubmitOperationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.RecordDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TargetMaxRecordResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ErrItemTagRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordErrorVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordItemListVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordListVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordPageExportData;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ReplicableTargetVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 巡检记录 前端控制器
 *
 * @author zwb
 * @since 2022-04-26
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordController {

    private final InspectRecordQueryService queryService;
    private final InspectRecordCommandService commandService;
    private final InspectRecordItemQueryService inspectRecordItemQueryService;
    private final InspectResultCommandService inspectResultCommandService;
    private final InspectStandardItemCommandService inspectStandardItemCommandService;
    private final InspectRecordItemCommandService inspectRecordItemCommandService;
    private final InspectItemTemplateCommandService inspectItemTemplateCommandService;
    private final InspectOfflineSubmitOperationService offlineSubmitOperationService;

    /**
     * 详情
     */
    @GetMapping("/inspect-record/{id}")
    public InspectRecordVO getVOById(@PathVariable Long id) {
        return queryService.getVOById(id);
    }

    /**
     * 根据目标对象 获取巡检记录
     */
    @GetMapping("/inspect-record/{targetCategory}/{targetId}")
    public InspectRecordVO getVOByTarget(@PathVariable("targetCategory") String targetCategory,
                                         @PathVariable("targetId") Long targetId,
                                         @RequestParam("taskId") @NotBlank(message = "任务ID不能为空") Long taskId
                                        ) {
        return queryService.getVOByTarget(targetCategory, targetId, taskId);
    }

    /**
     * 分页
     */
    @GetMapping("/inspect-record/page")
    @DataPermissionSwitch(dataModel = "inspect:inspectRecord")
    public Pagination<InspectRecordVO> getPage(InspectRecordQueryParam param) {
        return queryService.getPage(param);
    }

    /**
     * 巡检记录提交
     */
    @PostMapping("/inspect-record/{id}/submit")
    public void submitRecord(@PathVariable("id") Long id, @RequestBody @Valid List<InspectRecordItemCommandModel> commandModels) {
        commandService.submitRecord(id, commandModels);
    }

    /**
     * 巡检记录提交
     */
    @PostMapping("/inspect-record/{targetCategory}/{targetId}/submit")
    public void submitRecord(@PathVariable("targetCategory") String targetCategory,
                             @PathVariable("targetId") Long targetId,
                             @RequestParam("areaId") String areaId,
                             @RequestParam("areaName") String areaName,
                             @RequestParam("targetName") String targetName,
                             @RequestBody @Valid List<InspectRecordItemCommandModel> commandModels
                            ) {
        commandService.submitRecord(targetCategory, targetId, targetName, commandModels, areaId, areaName);
    }

    /**
     * 巡检记录提交（新）结构改造
     */
    @PostMapping("/inspect-record/submit/new")
    public void submitRecordNew(@RequestBody @Valid InspectRecordSubmitCommand recordSubmitCommand) {
        commandService.submitRecord(recordSubmitCommand.getTargetCategory(),
                                    recordSubmitCommand.getTargetId(),
                                    recordSubmitCommand.getTargetName(), recordSubmitCommand.getCommandModels(), recordSubmitCommand.getAreaId(), recordSubmitCommand.getAreaName());
    }

    /**
     * 巡检校验
     *
     * @param param 记录id集合
     *
     * @return 是否符合
     */
    @PostMapping("/inspect-record/task-submit/check")
    public Boolean checkSubmitRecord(@RequestBody InspectRecordSubmitCheckParam param) {
        return queryService.checkSubmitRecord(param);
    }

    /**
     * 批量提交任务的巡检记录
     * <p>
     * 老客户端不传 clientOperationId 时保持原有整体 Boolean 返回；离线客户端传 clientOperationId 时进入幂等提交流程。
     */
    @PostMapping("/inspect-record/task-submit/list")
    public Object listSubmitRecordNew(@RequestBody @Valid InspectRecordListSubmitCommand recordSubmitCommandList) {
        Assert.isParameterError(CollectionUtils.isEmpty(recordSubmitCommandList.getCommandList()), "提交内容不能为空！");
        if (hasOfflineOperation(recordSubmitCommandList)) {
            return offlineSubmitOperationService.submit(recordSubmitCommandList);
        }
        commandService.submitTaskRecordBatch(recordSubmitCommandList.getCommandList());
        return true;
    }

    /**
     * 查询离线批量提交的操作状态。
     * <p>
     * 用于 App 在业务提交响应丢失后确认该 clientOperationId 是否已被服务端执行。
     */
    @PostMapping("/inspect-record/task-submit/status")
    public List<InspectOfflineSubmitItemResult> listSubmitStatus(@RequestBody InspectOfflineSubmitStatusParam param) {
        return offlineSubmitOperationService.status(param);
    }

    /**
     * 批量提交临时巡检的巡检记录
     */
    @PostMapping("/inspect-record/temp-submit/list")
    public Boolean submitRecordBatch(@RequestBody @Valid InspectTempListSubmitCommand recordSubmitCommandList) {
        Assert.isParameterError(CollectionUtils.isEmpty(recordSubmitCommandList.getCommandList()), "提交内容不能为空！");
        return commandService.submitTempRecordBatch(recordSubmitCommandList.getCommandList());
    }

    /**
     * 判断批量提交请求是否应按离线幂等提交处理。
     */
    private boolean hasOfflineOperation(InspectRecordListSubmitCommand command) {
        if (command == null || CollectionUtils.isEmpty(command.getCommandList())) {
            return false;
        }
        return command.getCommandList().stream()
                .map(InspectTaskRecordSubmitCommand::getClientOperationId)
                .anyMatch(clientOperationId -> clientOperationId != null && !clientOperationId.isBlank());
    }

    /**
     * 巡检记录转其他业务
     */
    @PostMapping("/inspect-record/{id}/toBusiness")
    public void toBusiness(@PathVariable("id") Long id, @RequestBody @Valid InspectRecordToBusinessCommandModel commandModel) {
        commandService.toBusiness(id, commandModel);
    }

    /**
     * 巡检档案分页列表2.0
     */
    @GetMapping("/inspect-record/target-record-page")
    @DataPermissionSwitch(dataModel = "inspect:inspectRecord")
    public Pagination<RecordDetailResult> getTargetRecord(InspectRecordQueryParam param) {
        return queryService.getTargetRecordPage(param);
    }

    /**
     * 巡检档案详情2.0
     *
     * @param targetId
     *
     * @return
     */
    @GetMapping("/inspect-record/target-record-detail")
    public RecordDetailResult getTargetRecordDetail(@RequestParam("targetId") Long targetId,
                                                    @RequestParam(value = "targetCategory", required = false) String targetCategory
                                                   ) {
        return queryService.getTargetRecordDetail(targetId, targetCategory);
    }

    /**
     * 巡检档案详情-记录分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect-record/detail-recordPage")
    @DataPermissionSwitch(dataModel = "inspect:inspectRecord")
    public Pagination<InspectRecordVO> getRecordPage(@NotNull @Valid InspectRecordQueryParam param) {
        Assert.isParameterError(param == null || param.getTargetId() == null, "targetId 不能为空");
        return queryService.getRecordPage(param);
    }

    /**
     * @Author wql
     * @Description 判断巡检记录是否过期
     * @Date 17:06 2023/5/10
     **/
    @GetMapping("/inspect-record/recordIsExpired/{recordId}")
    public Boolean recordIsExpired(@PathVariable("recordId") Long recordId) {
        return queryService.recordIsExpired(recordId);
    }

    /**
     * 设备巡检次数统计
     *
     * @param param 统计参数
     *
     * @return 巡检次数
     */
    @PostMapping("/inspect-record/deviceInspectNum")
    public List<DeviceOperationStatisticsDTO> deviceInspectNumStatistics(@RequestBody DeviceStatisticParam param) {
        return queryService.deviceInspectNumStatistics(param);
    }

    /**
     * 对象巡检次数统计
     *
     * @param param 统计参数
     *
     * @return 巡检次数
     */
    @GetMapping("/inspect-record/objInspectNum")
    @DataPermissionSwitch(dataModel = "inspect:inspectRecord")
    public Long objInspectNumStatistics(DeviceStatisticParam param) {
        return queryService.objInspectNumStatistics(param);
    }

    /**
     * 巡检记录列表
     *
     * @param param period:巡检时间,status:状态
     *
     * @return 列表
     */
    @PostMapping("/inspect-record/list")
    public List<InspectRecordListVO> inspectRecordVOList(@RequestBody InspectRecordQueryParam param) {
        return queryService.inspectRecordVOList(param);
    }

    /**
     * 巡检记录项列表
     *
     * @param param 条件
     *
     * @return 列表
     */
    @PostMapping("/inspect-record/itemList")
    public List<InspectRecordItemListVO> inspectRecordItemList(@RequestBody InspectRecordItemQueryParam param) {
        return inspectRecordItemQueryService.inspectRecordItemList(param);
    }

    /**
     * 根据巡检目标获取异常目标数
     *
     * @param targetIdList 目标id集合
     *
     * @return 异常的目标数量
     */
    @PostMapping("/inspect-record/postErrNumByTargets")
    public Integer getErrNumByTargets(@RequestParam("targetIdList") List<Long> targetIdList) {
        return queryService.getErrNumByTargets(targetIdList);
    }

    /**
     * 巡检记录采集
     *
     * @param period
     *
     * @return
     */
    @PreAuthorize("hasScope('inspect_manage')")
    @GetMapping("/inspect-record/getInspectDataModel")
    public List<InspectRecordDataMode> getInspectRecordSafetyData(@RequestParam("period") Period period) {
        return queryService.getInspectRecordSafetyData(period);
    }

    /**
     * 异常巡检记录项tag采集
     *
     * @param period
     *
     * @return
     */
    @PreAuthorize("hasScope('inspect_manage')")
    @GetMapping("/inspect-record/getInspectItemDataModel")
    public List<InspectRecordItemDataMode> getInspectRecordItemSafetyData(@RequestParam("period") Period period) {
        return inspectRecordItemQueryService.getInspectRecordItemSafetyData(period);
    }

    /**
     * 查询巡检项选择为异常的巡检记录
     *
     * @param param 条件
     *
     * @return 异常记录
     */
    @PostMapping("/inspect-record/selectErrItemTagRecord")
    public Pagination<ErrItemTagRecordVO> selectErrItemTagRecord(@RequestBody ErrItemTagRecordParam param) {
        if (CollectionUtils.isEmpty(param.getTagList())) {
            param.setTagList(Arrays.stream(InspectionItemTagEnum.values()).map(InspectionItemTagEnum::name).collect(Collectors.toList()));
        }
        return queryService.selectErrItemTagRecord(param);
    }

    @GetMapping("/inspect/dataHandler/itemImageAndRemark")
    public Boolean imageRemarkDataHandler() {
        var nowTime = LocalDateTime.now();
        // 巡检结果map
        Map<Long, ResultValueDTO> resultMap = new HashMap<>();
        Map<Long, List<InspectResultPO>> resultListMap = new HashMap<>();
        List<InspectResultPO> resultPOList = inspectResultCommandService.list();
        if (!CollectionUtils.isEmpty(resultPOList)) {
            resultListMap = resultPOList.stream().collect(Collectors.groupingBy(InspectResultPO::getId));
        }
        resultListMap.forEach((k, list) -> {
            if (!CollectionUtils.isEmpty(list)) {
                ResultValueDTO dto = new ResultValueDTO();
                StringBuilder stringBuilder = new StringBuilder();
                AtomicReference<String> resultErrValue = new AtomicReference<>();
                list.forEach(result -> {
                    stringBuilder.append(result.getItemValue()).append(",");
                    if (Boolean.TRUE.equals(result.getErrorFlag())) {
                        resultErrValue.set(result.getItemValue());
                    }
                });
                if (resultErrValue.get() != null) {
                    dto.setErrValue(resultErrValue.get());
                }
                if (stringBuilder.length() > 0) {
                    dto.setAllValue(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
                }
                resultMap.put(k, dto);
            }
        });

        List<InspectItemTemplatePO> templateUpdateList = new ArrayList<>();
        List<InspectItemTemplatePO> templatePOList = inspectItemTemplateCommandService.list(Wrappers.<InspectItemTemplatePO>lambdaQuery()
                                                                                                    .orderByDesc(InspectItemTemplatePO::getCreateTime)
                                                                                                    .last(" limit 3000")
                                                                                           );
        if (!CollectionUtils.isEmpty(templatePOList)) {
            templatePOList.forEach(po -> {
                ResultValueDTO dto = resultMap.get(po.getResultId());
                if (dto != null) {
                    po.setRemarkResult(dto.getErrValue());
                    if ("1".equals(po.getImageResult())) {
                        po.setImageResult(dto.getAllValue());
                    } else {
                        po.setImageResult(dto.getErrValue());
                    }
                    po.setUpdateTime(nowTime);
                    templateUpdateList.add(po);
                }
            });
        }
        if (!CollectionUtils.isEmpty(templateUpdateList)) {
            inspectItemTemplateCommandService.updateBatchById(templateUpdateList);
        }

        List<InspectRecordItemPO> recordItemUpdateList = new ArrayList<>();
        List<InspectRecordItemPO> recordItemList = inspectRecordItemCommandService.list(Wrappers.<InspectRecordItemPO>lambdaQuery()
                                                                                                .orderByDesc(InspectRecordItemPO::getCreateTime)
                                                                                                .last(" limit 3000")
                                                                                       );
        if (!CollectionUtils.isEmpty(recordItemList)) {
            recordItemList.forEach(po -> {
                ResultValueDTO dto = resultMap.get(po.getItemResultId());
                if (dto != null) {
                    po.setRemarkResult(dto.getErrValue());
                    if ("1".equals(po.getItemImageResult())) {
                        po.setItemImageResult(dto.getAllValue());
                    } else {
                        po.setItemImageResult(dto.getErrValue());
                    }
                    po.setUpdateTime(nowTime);
                    recordItemUpdateList.add(po);
                }
            });
        }
        if (!CollectionUtils.isEmpty(recordItemUpdateList)) {
            inspectRecordItemCommandService.updateBatchById(recordItemUpdateList);
        }

        List<InspectStandardItemPO> standardItemUpdateList = new ArrayList<>();
        List<InspectStandardItemPO> standardItemList = inspectStandardItemCommandService.list(Wrappers.<InspectStandardItemPO>lambdaQuery()
                                                                                                      .orderByDesc(InspectStandardItemPO::getId)
                                                                                                      .last(" limit 3000")
                                                                                             );
        if (!CollectionUtils.isEmpty(standardItemList)) {
            standardItemList.forEach(po -> {
                ResultValueDTO dto = resultMap.get(po.getResultId());
                if (dto != null) {
                    po.setRemarkResult(dto.getErrValue());
                    if ("1".equals(po.getImageResult())) {
                        po.setImageResult(dto.getAllValue());
                    } else {
                        po.setImageResult(dto.getErrValue());
                    }
                    standardItemUpdateList.add(po);
                }
            });
        }
        if (!CollectionUtils.isEmpty(standardItemUpdateList)) {
            inspectStandardItemCommandService.updateBatchById(standardItemUpdateList);
        }
        return true;
    }

    /**
     * 巡检记录分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/inspect-record/pagination")
    @DataPermissionSwitch(dataModel = "inspect:inspectRecord")
    public Pagination<InspectRecordVO> InspectRecordPage(InspectRecordQueryParam param) {
        return queryService.InspectRecordPage(param);
    }

    /**
     * 巡检记录列表导出数据
     * (导出巡检记录分页列表时使用)
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/inspect-record/page-export")
    public InspectRecordPageExportData InspectRecordExportData(InspectRecordQueryParam param) {
        return queryService.InspectRecordExportData(param);
    }

    /**
     * 电动车最新违规巡检
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/inspect-record/postTargetMaxError")
    public Map<Long, Map<String, TargetMaxRecordResult>> postTargetMaxError(@RequestBody TargetMaxRecordParam param) {
        return queryService.getTargetMaxError(param);
    }

    /**
     * 异常巡检记录
     *
     * @param param 条件
     *
     * @return 巡检记录
     */
    @GetMapping("/inspect-record/error-page")
    public Pagination<InspectRecordErrorVO> inspectRecordErrorPage(InspectionRecordErrorParam param) {
        if (!StringUtils.isEmpty(param.getErrorTags())) {
            List<String> errorTagList = Stream.of(param.getErrorTags().split(","))
                    .collect(Collectors.toList());
            if (param.getErrorTagList() != null) {
                param.getErrorTagList().addAll(errorTagList);
            } else {
                param.setErrorTagList(errorTagList);
            }
        }
        return queryService.inspectRecordErrorPage(param);
    }

    /**
     * 获取巡检记录项分组统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect-record/items/statistics")
    public List<InspectRecordItemStatisticsVO> getInspectRecordItemsStatistics(InspectRecordItemQueryParam param) {
        return inspectRecordItemQueryService.getInspectRecordItemsStatistics(param);
    }

    /**
     * 是否存在巡检异常
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/inspect-record/error/flag")
    public Boolean isExitAbnormal(@RequestParam("id") Long id, @RequestParam("businessType") String businessType) {
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(businessType);
        InspectionRecordErrorParam param = new InspectionRecordErrorParam();
        param.setErrorTagBool(true);
        param.setTargetId(id);
        Pagination<InspectRecordErrorVO> inspectRecordErrorVOPagination = queryService.inspectRecordErrorPage(param);
        return !CollectionUtils.isEmpty(inspectRecordErrorVOPagination.getRecords());
    }

    /**
     * 获取可以复制内容的对象
     *
     * @param param id 巡检记录id, targetCategory 类型
     *
     * @return 可复制内容的对象列表
     */
    @GetMapping("/inspect-record/replicable-target")
    public List<ReplicableTargetVO> getReplicableTargetList(InspectRecordQueryParam param) {
        return queryService.getReplicableTargetList(param);
    }

    /**
     * 取消巡检
     *
     * @param param 取消内容
     *
     * @return 是否成功
     */
    @PutMapping("/inspect-record/batch/cancel-inspect")
    public Boolean batchCancelInspect(@RequestBody InspectRecordCancelBatchCommand param) {
        return commandService.batchCancelInspect(param);
    }

    @Data
    private class ResultValueDTO {
        private String errValue;
        private String allValue;
    }
}
