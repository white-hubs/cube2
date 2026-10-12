package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.videogw.client.VideoClient;
import com.guhecloud.cube.base.videogw.client.dto.VideoFileDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.InspectionRecordNumMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordItemQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordToBusinessQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordRepairMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskTargetItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.TaskTargetRelationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectionItemTagEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.facade.InspectRecordToBusinessFacade;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordAndTaskDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordDataMode;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.AbnormalResultsStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.ErrItemTagRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordSubmitCheckParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRepairQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionRecordErrorParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TargetMaxRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordRepairPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskTargetItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.TaskTargetRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.AbnormalResultsStatisticsResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.RecordDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TargetMaxRecordResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ErrItemTagRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordErrorVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordItemVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordListVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordPageExportData;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordRepairVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordStandardItemVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordStandardVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordToBusinessVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTargetRepairVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ReplicableTargetVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.utils.DateUtil;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordQueryServiceImpl extends ServiceImpl<InspectRecordMapper, InspectRecordPO> implements InspectRecordQueryService {

    private final InspectRecordItemQueryService recordItemQueryService;
    private final InspectRecordToBusinessQueryService recordToBusinessQueryService;
    private final InspectRecordMapper inspectRecordMapper;
    private final InspectTaskMapper inspectTaskMapper;
    private final InspectRecordItemMapper inspectRecordItemMapper;
    private final InspectQuery inspectQuery;
    private final InspectTargetInfoQuery inspectTargetInfoQuery;
    private final InspectRecordToBusinessFacade inspectRecordToBusinessFacade;
    private final InspectRecordRepairMapper inspectRecordRepairMapper;
    private final TaskTargetRelationMapper taskTargetRelationMapper;
    private final FileClient fileClient;
    private final InspectTaskTargetItemMapper inspectTaskTargetItemMapper;
    private final VideoClient videoClient;

    @Override
    public InspectRecordVO getVOById(Long id) {
        InspectRecordPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的巡检记录");
        InspectTaskPO taskPO = inspectTaskMapper.selectById(po.getTaskId());
        InspectRecordVO vo = ObjectUtils.copying(po, InspectRecordVO.class);
        vo.setRecordItemVOS(recordItemQueryService.getListByRecordId(vo.getId()));
        vo.setRecordToBusinessVOS(recordToBusinessQueryService.getListByRecordId(vo.getId()));
        vo.setAssistNames(inspectQuery.extendUserNames(po.getTenant(), Objects.nonNull(taskPO) ? taskPO.getAssistIds() : null));
        this.buildStandardVO(vo);
        Map<Long, List<InspectRecordRepairVO>> repairMap = this.getRecordRepairList(Collections.singletonList(id));
        vo.setRecordHandledManuallyPOS(repairMap.get(id));
        if (vo.getCancelFileKey() != null) {
            vo.setCancelFileList(fileClient.getByBusinessKey(vo.getCancelFileKey()));
        }
        if (vo.getCancelInspectFileKey() != null) {
            vo.setCancelInspectFileList(fileClient.getByBusinessKey(vo.getCancelInspectFileKey()));
        }
        this.extSnapCapture(vo);
        return vo;
    }

    private void extSnapCapture(InspectRecordVO vo) {
        if (!InspectTargetCategoryEnum.SITE_POINT.name().equalsIgnoreCase(vo.getTargetCategory()) ||
            StringUtils.isBlank(vo.getSnapAttachmentIds())) {
            return;
        }
        try {
            Set<Long> fileIds = Arrays.stream(vo.getSnapAttachmentIds().split(",")).map(Long::valueOf).collect(Collectors.toSet());
            List<VideoFileDTO> videoFilesByIds = videoClient.getVideoFilesByIds(fileIds);
            if (CollectionUtils.isEmpty(videoFilesByIds)) {
                return;
            }
            vo.setSnapCaptureUrls(videoFilesByIds.stream()
                                          .filter(t -> "LOCAL_SUCCESS_STORAGE".equals(t.getLocallyStoredStatus()))
                                          .map(VideoFileDTO::getPath).collect(Collectors.toList()));
        } catch (Exception e) {
            log.warn("获取巡检抓拍图片失败, recordId={}", vo.getId(), e);
        }
    }

    private void buildStandardVO(InspectRecordVO vo) {
        List<InspectRecordStandardVO> standardVOS = new ArrayList<>();
        vo.getRecordItemVOS().stream().collect(Collectors.groupingBy(InspectRecordItemVO::getStandardId))
                .forEach((aLong, inspectRecordItemVOS) -> {
                    InspectRecordStandardVO standardVO = ObjectUtils.copying(inspectRecordItemVOS.get(0), InspectRecordStandardVO.class);
                    standardVO.setItemVOS(ObjectUtils.copying(inspectRecordItemVOS, InspectRecordStandardItemVO.class));
                    standardVOS.add(standardVO);
                });

        vo.setStandardVOS(standardVOS);
        vo.setRecordItemVOS(null);
    }

    @Override
    public InspectRecordVO getVOByTarget(String targetCategory, Long targetId, Long taskId) {

        InspectRecordPO po = this.lambdaQuery()
                .eq(InspectRecordPO::getTargetCategory, targetCategory)
                .eq(InspectRecordPO::getTargetId, targetId)
                .eq(InspectRecordPO::getTaskId, taskId)
                .one();

        Assert.isNotFound(po, "未找到指定的巡检记录");

        InspectRecordVO vo = ObjectUtils.copying(po, InspectRecordVO.class);
        vo.setRecordItemVOS(recordItemQueryService.getListByRecordId(vo.getId()));
        vo.setRecordToBusinessVOS(recordToBusinessQueryService.getListByRecordId(vo.getId()));

        return vo;
    }

    @Override
    public List<InspectRecordVO> getListByTaskId(Long taskId) {
        List<InspectRecordPO> pos = this.lambdaQuery()
                .eq(InspectRecordPO::getTaskId, taskId)
                .list();

        return Optional.ofNullable(pos).orElse(Collections.emptyList())
                .stream().map(po -> ObjectUtils.copying(po, InspectRecordVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<InspectRecordVO> getPage(InspectRecordQueryParam param) {
        Period period = DateUtil.simpleDateTypeHandle(param.getDateType(), param.getPeriod());
        Page<InspectRecordPO> page = this.lambdaQuery().between(period !=
                                                                null, InspectRecordPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(InspectRecordPO::getTaskName, param.getKeyword())
                        .or()
                        .like(InspectRecordPO::getTargetName, param.getKeyword())
                    )
                .and(StringUtils.isNotEmpty(param.getTaskIds()), q -> q
                        .eq(!param.getTaskIds().contains(","), InspectRecordPO::getTaskId, param.getTaskIds())
                        .in(param.getTaskIds().contains(","), InspectRecordPO::getTaskId, Arrays.stream(param.getTaskIds().split(","))
                                .map(Long::valueOf).collect(Collectors.toList()))
                    )
                .eq(param.getUserId() != null, InspectRecordPO::getUserId, param.getUserId())
                .eq(param.getStatus() != null, InspectRecordPO::getStatus, param.getStatus())
                .eq(param.getTargetId() != null, InspectRecordPO::getTargetId, param.getTargetId())
                .eq(StringUtils.isNotEmpty(param.getResult()), InspectRecordPO::getResult, param.getResult())
                .isNotNull(Boolean.TRUE.equals(param.getRepairBool()), InspectRecordPO::getRepairStatus)
                .eq(StringUtils.isNotBlank(param.getRepairStatus()), InspectRecordPO::getRepairStatus, param.getRepairStatus())
                .orderByDesc(InspectRecordPO::getCreateTime)
                .page(PagingUtils.convert(param));

        List<InspectRecordVO> collect = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList())
                .parallelStream().map(po -> ObjectUtils.copying(po, InspectRecordVO.class)).collect(Collectors.toList());
        return PagingUtils.convert(page, collect);
    }

    /**
     * 巡检档案分页列表2.0
     *
     * @param param 巡检记录查询参数
     *
     * @return 巡检记录
     */
    @Override
    public Pagination<RecordDetailResult> getTargetRecordPage(InspectRecordQueryParam param) {

        Page<InspectRecordPO> page = inspectRecordMapper.selectRecordPage(PagingUtils.convert(param), param);
        List<InspectRecordPO> recordPOList = page.getRecords();
        if (CollectionUtils.isEmpty(recordPOList)) {
            return Pagination.empty();
        }
        /* 获取记录的所有错误描述 */
        Map<Long, List<InspectRecordItemPO>> recordErrorMap = new HashMap<>();
        List<Long> recordIdList = recordPOList.stream().map(InspectRecordPO::getId).collect(Collectors.toList());
        List<InspectRecordItemPO> itemErrorList = inspectRecordItemMapper.listRecordErrorDesc(recordIdList);
        if (!CollectionUtils.isEmpty(itemErrorList)) {
            recordErrorMap = itemErrorList.stream().collect(Collectors.groupingBy(InspectRecordItemPO::getRecordId));
        }
        /* 填充错误描述 */
        Map<Long, List<InspectRecordItemPO>> finalMap = recordErrorMap;
        List<RecordDetailResult> resultList = new ArrayList<>();
        recordPOList.forEach(po -> {
            RecordDetailResult result = new RecordDetailResult();
            BeanUtils.copyProperties(po, result);
            result.setTargetCategory(
                    StringUtils.isNotBlank(po.getTargetCategory()) ? InspectTargetCategoryEnum.valueOf(po.getTargetCategory()) : null);
            List<InspectRecordItemPO> itemPOList = finalMap.get(po.getId());
            if (!CollectionUtils.isEmpty(itemPOList)) {
                result.setErrorDesc(itemPOList.stream().map(InspectRecordItemPO::getErrorDesc).collect(Collectors.toList()));
            }
            resultList.add(result);
        });

        return PagingUtils.convert(page, resultList);
    }

    /**
     * 巡检档案详情2.0
     *
     * @param targetId 对象Id
     *
     * @return 巡检记录
     */
    @Override
    public RecordDetailResult getTargetRecordDetail(Long targetId, String targetCategory) {
        RecordDetailResult result;

        // 设置基础信息
        InspectRecordPO recordPO = inspectRecordMapper.selectTargetRecordOne(targetId);
        if (recordPO == null) {
            // 如果没有巡检记录就查询对象
            InspectTargetInfoDTO targetInfoDTO = inspectTargetInfoQuery.getTargetInfoById(targetId, targetCategory);
            if (targetInfoDTO == null) {
                result = new RecordDetailResult();
            } else {
                result = ObjectUtils.copying(targetInfoDTO, RecordDetailResult.class);
                result.setOrganizationName(targetInfoDTO.getDepName());
            }
        } else {
            result = ObjectUtils.copying(recordPO, RecordDetailResult.class);
        }
        // 日常巡检数
        result.setDailyNum(this.lambdaQuery()
                                   .eq(InspectRecordPO::getTargetId, targetId)
                                   .isNotNull(InspectRecordPO::getResult)
                                   .isNotNull(InspectRecordPO::getTaskId).count());
        // 日常巡检异常数
        result.setDailyErrNum(this.lambdaQuery()
                                      .eq(InspectRecordPO::getTargetId, targetId)
                                      .isNotNull(InspectRecordPO::getTaskId)
                                      .eq(InspectRecordPO::getResult, InspectRecordResultEnum.ABNORMAL).count());
        // 零时巡检数
        result.setTemporaryNum(this.lambdaQuery()
                                       .eq(InspectRecordPO::getTargetId, targetId)
                                       .isNull(InspectRecordPO::getTaskId).count());
        // 临时巡检异常项数
        result.setTemporaryErrNum(this.lambdaQuery()
                                          .eq(InspectRecordPO::getTargetId, targetId)
                                          .isNull(InspectRecordPO::getTaskId)
                                          .eq(InspectRecordPO::getResult, InspectRecordResultEnum.ABNORMAL).count());
        return result;
    }

    /**
     * 获取台账的巡检记录
     * 台账就是以对象为单位的巡检记录
     * targetId 对象id
     * temporary 0||null:全部记录，1日常记录，2临时记录
     *
     * @return 巡检记录
     */
    @Override
    public Pagination<InspectRecordVO> getRecordPage(InspectRecordQueryParam param) {
        Page<InspectRecordVO> page = inspectRecordMapper.getRecordList(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        List<Long> taskIdList = page.getRecords().stream().map(InspectRecordVO::getTaskId)
                .filter(Objects::nonNull).collect(Collectors.toList());
        Map<Long, InspectTaskPO> taskMap;
        if (!CollectionUtils.isEmpty(taskIdList)) {
            taskMap = inspectTaskMapper.selectList(Wrappers.<InspectTaskPO>lambdaQuery().in(InspectTaskPO::getId, taskIdList))
                    .stream().collect(Collectors.toMap(InspectTaskPO::getId, t -> t));
        } else {
            taskMap = new HashMap<>();
        }
        List<InspectRecordVO> collect = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList())
                .stream().map(po -> {
                    InspectRecordVO inspectRecordVO = ObjectUtils.copying(po, InspectRecordVO.class);
                    InspectTaskPO taskPO = taskMap.get(po.getTaskId());
                    if (taskPO != null) {
                        inspectRecordVO.setTaskName(taskPO.getName());
                    }
                    return inspectRecordVO;
                }).collect(Collectors.toList());
        return PagingUtils.convert(page, collect);

    }

    /**
     * Author wql
     * Description 判断巡检记录是否已过期
     * Date 17:08 2023/5/10
     **/
    @Override
    public Boolean recordIsExpired(Long recordId) {
        InspectRecordPO recordPO = inspectRecordMapper.selectById(recordId);
        Assert.isNotFound(recordPO, "巡检记录不存在！");
        InspectTaskPO taskPO = inspectTaskMapper.selectById(recordPO.getTaskId());
        Assert.isNotFound(taskPO, "巡检任务不存在！");
        return TaskStatusEnum.EXPIRE.name().equals(taskPO.getStatus().name())
               || InspectRecordStatusEnum.EXPIRE.name().equals(recordPO.getStatus().name());
    }

    @Override
    public List<DeviceOperationStatisticsDTO> deviceInspectNumStatistics(DeviceStatisticParam param) {
        if (ObjectUtils.isNotEmpty(param.getDeviceIds())) {
            return inspectRecordMapper.deviceInspectNumStatistics(param);
        }
        return CollectionUtils.emptyList();
    }

    @Override
    public Long objInspectNumStatistics(DeviceStatisticParam param) {
        if (ObjectUtils.isNotEmpty(param.getDeviceIds())) {
            return this.lambdaQuery().
                    between(ObjectUtils.isNotEmpty(param.getPeriod()), InspectRecordPO::getTime,
                            ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                    .in(ObjectUtils.isNotEmpty(param.getDeviceIds()), InspectRecordPO::getTargetId, param.getDeviceIds())
                    .select(InspectRecordPO::getTargetId).count();
        }
        return 0L;
    }

    /**
     * 巡检记录列表
     *
     * @param param period:巡检时间,status:状态
     *
     * @return 列表
     */
    @Override
    public List<InspectRecordListVO> inspectRecordVOList(InspectRecordQueryParam param) {
        LambdaQueryWrapper<InspectRecordPO> wrapper = Wrappers.lambdaQuery();
        if (param != null) {
            if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
                wrapper.ge(InspectRecordPO::getTime, param.getPeriod().getBegin())
                        .le(InspectRecordPO::getTime, param.getPeriod().getEnd());
            }
            wrapper.eq(param.getStatus() != null, InspectRecordPO::getStatus, param.getStatus());
            wrapper.eq(StringUtils.isNotBlank(param.getResult()), InspectRecordPO::getResult, param.getResult());
        }
        List<InspectRecordPO> poList = this.list(wrapper);
        return ConvertUtils.toList(InspectRecordListVO.class, poList);
    }

    /**
     * 根据巡检目标获取异常目标数
     *
     * @param targetIdList 目标id集合
     *
     * @return 异常的目标数量
     */
    @Override
    public Integer getErrNumByTargets(List<Long> targetIdList) {
        if (CollectionUtils.isEmpty(targetIdList)) {
            return 0;
        }
        return inspectRecordMapper.getErrorNumByTargetIdList(targetIdList);
    }

    @Override
    public List<InspectRecordDataMode> getInspectRecordSafetyData(Period period) {
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set("IGNORE");
        try {
            List<InspectRecordPO> poList = this.baseMapper.getTaskRecordCollectData(SessionContextHolder.getRequester()
                                                                                            .getTenant(), period);
            if (CollectionUtils.isEmpty(poList)) {
                return Collections.emptyList();
            }
            return ObjectUtils.copying(poList, InspectRecordDataMode.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    /**
     * 查询巡检项选择为异常的巡检记录
     *
     * @param param 条件
     *
     * @return 异常记录
     */
    @Override
    public Pagination<ErrItemTagRecordVO> selectErrItemTagRecord(ErrItemTagRecordParam param) {
        Page<ErrItemTagRecordVO> page = this.baseMapper.selectErrItemTagRecord(PagingUtils.convert(param), param);
        return PagingUtils.convert(page);
    }

    /**
     * 巡检记录分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<InspectRecordVO> InspectRecordPage(InspectRecordQueryParam param) {
        Page<InspectRecordVO> page = this.baseMapper.inspectRecords(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return PagingUtils.convert(page);
        }
        this.extRecordListVO(page.getRecords(), null);
        return PagingUtils.convert(page);
    }

    /**
     * 巡检记录列表
     *
     * @param param 条件
     *
     * @return 列表
     */
    @Override
    public InspectRecordPageExportData InspectRecordExportData(InspectRecordQueryParam param) {
        InspectRecordPageExportData result = InspectRecordPageExportData.getEmptyData();

        List<InspectRecordVO> list = this.baseMapper.inspectRecords(param);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        this.extRecordListVO(list, result);
        return result;
    }

    private void extRecordListVO(List<InspectRecordVO> list, InspectRecordPageExportData result) {
        List<Long> recordIds = list.stream().map(InspectRecordVO::getId).collect(Collectors.toList());
        Map<Long, List<InspectRecordItemVO>> recordItemVOMap = recordItemQueryService.getErrorItemByRecordIdList(recordIds);
        AtomicInteger normal = new AtomicInteger();
        AtomicInteger abnormal = new AtomicInteger();
        AtomicInteger waitRepair = new AtomicInteger();
        AtomicInteger cancelRepair = new AtomicInteger();
        AtomicInteger over = new AtomicInteger();
        /* 获取取消修复的文件 */
        Map<Long, List<FileVO>> canceFileMap = new HashMap<>();
        Set<Long> cleanFileSet = list.stream().map(InspectRecordVO::getCancelFileKey).filter(Objects::nonNull).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(cleanFileSet)) {
            BusinessKeysParam fileParam = new BusinessKeysParam();
            fileParam.setBusinessKeys(new ArrayList<>(cleanFileSet));
            canceFileMap = fileClient.getByBusinessKeys(fileParam);
        }
        Map<Long, List<FileVO>> finalCanceFileMap = canceFileMap;
        list.forEach(record -> {
            /* 设置取消修复的文件*/
            if (record.getCancelFileKey() != null) {
                record.setCancelFileList(finalCanceFileMap.get(record.getCancelFileKey()));
            }
            /* 计算统计 */
            if (result != null) {
                if (InspectRecordResultEnum.NORMAL.name().equals(record.getResult())) {
                    normal.getAndIncrement();
                } else if (InspectRecordResultEnum.ABNORMAL.name().equals(record.getResult())) {
                    abnormal.getAndIncrement();
                }
                if (InspectRecordStatusEnum.WAIT_REPAIR.name().equals(record.getStatus())) {
                    waitRepair.getAndIncrement();
                } else if (InspectRecordStatusEnum.OVER.name().equals(record.getStatus())) {
                    over.getAndIncrement();
                } else if (InspectRecordStatusEnum.CANCEL_REPAIR.name().equals(record.getStatus())) {
                    cancelRepair.getAndIncrement();
                }
            }
            /* 设置异常巡检项内容*/
            List<InspectRecordItemVO> inspectRecordItemVOList = recordItemVOMap.get(record.getId());
            if (!CollectionUtils.isEmpty(inspectRecordItemVOList)) {
                List<InspectRecordStandardVO> standardVOS = new ArrayList<>();
                inspectRecordItemVOList.stream().collect(Collectors.groupingBy(InspectRecordItemVO::getStandardId))
                        .forEach((aLong, inspectRecordItemVOS) -> {
                            InspectRecordStandardVO standardVO = ObjectUtils.copying(inspectRecordItemVOS.get(0), InspectRecordStandardVO.class);
                            standardVO.setItemVOS(ObjectUtils.copying(inspectRecordItemVOS, InspectRecordStandardItemVO.class));
                            standardVOS.add(standardVO);
                        });
                record.setStandardVOS(standardVOS);
            }
        });
        /* 设置统计值*/
        if (result != null) {
            result.setNormal(normal.get());
            result.setAbnormal(abnormal.get());
            result.setWaitRepair(waitRepair.get());
            result.setCancelRepair(cancelRepair.get());
            result.setOver(over.get());
            result.setTotal(list.size());
            result.setInspectRecordList(list);
        }
    }

    /**
     * 一段时间的异常项数量统计
     *
     * @param param 条件
     *
     * @return 结果
     */
    @Override
    public AbnormalResultsStatisticsResult abnormalResultsStatistics(AbnormalResultsStatisticsParam param) {
        return this.baseMapper.abnormalResultsStatistics(Wrappers.query()
                                                                 .eq("iri.error_value_flag", 1)
                                                                 .between("ir.time", ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                                                        );
    }

    /**
     * 电动车最新违规巡检
     *
     * @param param 条件
     *
     * @return map
     */
    @Override
    public Map<Long, Map<String, TargetMaxRecordResult>> getTargetMaxError(TargetMaxRecordParam param) {
        if (CollectionUtils.isEmpty(param.getTargetIdList())) {
            return new HashMap<>();
        }
        if (CollectionUtils.isEmpty(param.getTagList())) {
            param.setTagList(Arrays.stream(InspectionItemTagEnum.values()).map(Enum::name).collect(Collectors.toList()));
        }

        Map<String, Integer> countMap = new HashMap<>();
        Map<String, String> itemMap = new HashMap<>();
        param.getTagList().forEach(tag -> {
            List<TargetMaxRecordResult> countTagList = this.baseMapper.getTargetErrorCount(param, tag);
            if (CollectionUtils.isNotEmpty(countTagList)) {
                countTagList.forEach(c -> countMap.put(c.getTargetId() + tag, c.getCount()));
            }

            List<TargetMaxRecordResult> itemTagLit = this.baseMapper.getMaxTimeErrorItem(param, tag);
            if (CollectionUtils.isNotEmpty(itemTagLit)) {
                itemTagLit.forEach(i -> itemMap.put(i.getTag() + tag, i.getItem()));
            }
        });

        Map<Long, Map<String, TargetMaxRecordResult>> resultMap = new HashMap<>();
        param.getTargetIdList().forEach(id -> {
            Map<String, TargetMaxRecordResult> tagMap = new HashMap<>();
            param.getTagList().forEach(tag -> {
                Integer count = countMap.get(id + tag);
                String item = itemMap.get(id + tag);
                tagMap.put(tag, TargetMaxRecordResult.builder().targetId(id).tag(tag).item(item).count(count).build());
            });
            resultMap.put(id, tagMap);
        });

        return resultMap;
    }

    /**
     * 异常巡检记录
     *
     * @param param 条件
     *
     * @return 巡检记录
     */
    @Override
    public Pagination<InspectRecordErrorVO> inspectRecordErrorPage(InspectionRecordErrorParam param) {
        Page<InspectRecordErrorVO> page = this.baseMapper.inspectRecordErrorPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(page);
    }

    /**
     * 批量获取任务对象的巡检记录
     *
     * @param taskId       任务id
     * @param targetIdList 对象id集合
     *
     * @return map key:对象id，value:巡检记录集合
     */
    @Override
    public Map<Long, InspectRecordVO> getRecordTargetIdMap(Long taskId, List<Long> targetIdList) {
        Map<Long, InspectRecordVO> resultMap = new HashMap<>();
        List<InspectRecordVO> recordList = this.baseMapper.getTaskRecordByTargetIds(taskId, targetIdList);
        if (CollectionUtils.isNotEmpty(recordList)) {
            List<Long> recordIds = recordList.stream().map(InspectRecordVO::getId).collect(Collectors.toList());
            Map<Long, List<InspectRecordToBusinessVO>> recordToBusinessMap = recordToBusinessQueryService.getMapByRecordIdList(recordIds);
            Map<Long, List<InspectRecordItemVO>> recordItemVOMap = recordItemQueryService.getListByRecordIdList(recordIds);
            recordList.forEach(record -> {
                record.setRecordItemVOS(recordItemVOMap.get(record.getId()));
                record.setRecordToBusinessVOS(recordToBusinessMap.get(record.getId()));
            });
            return recordList.stream().collect(Collectors.toMap(InspectRecordVO::getTargetId, t -> t, (a, b) -> a));
        }
        return resultMap;
    }

    /**
     * 巡检记录异常率
     *
     * @param param 时间段
     *
     * @return 异常率
     */
    @Override
    public TotalNumberRateDTO getRecordErrRate(InspectionCommonParam param) {
        Period period = param.getPeriod();
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(param.getBusinessType());
        var recordIdSet = new ArrayList<Long>();
        if (StringUtils.isNotBlank(param.getItemName())) {
            var inspectRecordItemPOList = inspectRecordItemMapper.selectList(Wrappers.<InspectRecordItemPO>lambdaQuery()
                                                                                     .like(InspectRecordItemPO::getItemName, param.getItemName()));
            inspectRecordItemPOList.forEach(inspectRecordItemPO -> recordIdSet.add(inspectRecordItemPO.getRecordId()));
        }

        // 异常数量
        Long errInt = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getAreaName()), InspectRecordPO::getAreaName, param.getAreaName())
                .eq(StringUtils.isNotBlank(param.getObjectType()), InspectRecordPO::getTargetCategory, param.getObjectType())
                .ge(period != null &&
                    period.getBegin() != null, InspectRecordPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin))
                .le(period != null && period.getEnd() != null, InspectRecordPO::getCreateTime, ObjectUtils.ifNull(period, Period::getEnd))
                .eq(InspectRecordPO::getResult, InspectRecordResultEnum.ABNORMAL.name())
                .in(CollectionUtils.isNotEmpty(recordIdSet), InspectRecordPO::getId, recordIdSet)
                .in(CollectionUtils.isNotEmpty(param.getPlanIdList()), InspectRecordPO::getPlanId, param.getPlanIdList())
                .count();
        if (errInt == null) {
            errInt = 0L;
        }
        // 总数量
        Long totalInt = this.getTotalNotExecNumber(period, param);
        // 异常率
        BigDecimal errRate = BigDecimal.ZERO;
        if (totalInt > 0) {
            errRate = BigDecimal.valueOf(errInt).divide(BigDecimal.valueOf(totalInt), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        // 返回
        return TotalNumberRateDTO.builder().total(totalInt).number(errInt).rate(errRate).build();
    }

    /**
     * 巡检记录完成率
     *
     * @param param 时间段
     *
     * @return 率
     */
    @Override
    public TotalNumberRateDTO getRecordCompleteRate(InspectionCommonParam param) {
        Period period = param.getPeriod();
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(param.getBusinessType());
        // 过期数
        Long expireInt = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getAreaName()), InspectRecordPO::getAreaName, param.getAreaName())
                .eq(StringUtils.isNotBlank(param.getObjectType()), InspectRecordPO::getTargetCategory, param.getObjectType())
                .ge(period != null &&
                    period.getBegin() != null, InspectRecordPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin))
                .le(period != null && period.getEnd() != null, InspectRecordPO::getCreateTime, ObjectUtils.ifNull(period, Period::getEnd))
                .eq(InspectRecordPO::getStatus, InspectRecordStatusEnum.OVER.name())
                .count();
        if (expireInt == null) {
            expireInt = 0L;
        }
        // 总数
        Long totalInt = this.getTotalNotExecNumber(period, param);
        // 完成数 = 总数-异常数
        long completeInt = Math.max(totalInt - expireInt, 0);
        // 完成率
        BigDecimal rate = BigDecimal.ZERO;
        if (totalInt > 0) {
            rate = BigDecimal.valueOf(completeInt).divide(BigDecimal.valueOf(totalInt), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        // 返回
        return TotalNumberRateDTO.builder().total(totalInt).number(completeInt).rate(rate).build();
    }

    @Override
    public InspectionRecordNumMetricDTO getRecordUserNumMetric(Period period) {
        Long totalNum = this.baseMapper.selectCountCompletedRecord(period);
        return InspectionRecordNumMetricDTO.builder()
                .inspectionRecordNum(totalNum)
                .topInspectUser(this.baseMapper.selectRankUserInspectRecordNum(period, "desc"))
                .bottomInspectUser(this.baseMapper.selectRankUserInspectRecordNum(period, null))
                .build();
    }

    /**
     * 获取可以复制内容的对象
     *
     * @param param id 巡检记录id,targetCategory 对象类型
     *
     * @return 可复制内容的对象列表
     */
    @Override
    public List<ReplicableTargetVO> getReplicableTargetList(InspectRecordQueryParam param) {
        List<ReplicableTargetVO> resultList = new ArrayList<>();
        // 查询记录
        InspectRecordPO recordPO = this.getById(param.getId());
        if (recordPO == null) {
            log.warn("getReplicableTargetList：recordPO is null!");
            return resultList;
        }
        // 查询巡检内容
        List<InspectTaskTargetItemPO> sourceTargetList = inspectTaskTargetItemMapper.selectList(Wrappers
                                                                                                        .<InspectTaskTargetItemPO>lambdaQuery()
                                                                                                        .select(InspectTaskTargetItemPO::getTargetId, InspectTaskTargetItemPO::getStandardId, InspectTaskTargetItemPO::getItemId, InspectTaskTargetItemPO::getResultId)
                                                                                                        .eq(InspectTaskTargetItemPO::getTaskId, recordPO.getTaskId())
                                                                                                        .eq(InspectTaskTargetItemPO::getTargetId, recordPO.getTargetId()));

        // 获取可选者的记录
        List<InspectRecordPO> recordPOS = this.lambdaQuery()
                .select(InspectRecordPO::getId, InspectRecordPO::getTargetCategory, InspectRecordPO::getTargetId, InspectRecordPO::getTargetName, InspectRecordPO::getAreaId, InspectRecordPO::getAreaName)
                .eq(InspectRecordPO::getTaskId, recordPO.getTaskId())
                .eq(InspectRecordPO::getStatus, InspectRecordStatusEnum.EXECUTING.name())
                .eq(StringUtils.isNotBlank(param.getTargetCategory()), InspectRecordPO::getTargetCategory, param.getTargetCategory())
                .like(StringUtils.isNotBlank(param.getKeyword()), InspectRecordPO::getTargetName, param.getKeyword())
                .ne(InspectRecordPO::getTargetId, recordPO.getTargetId())
                .list();
        if (CollectionUtils.isEmpty(recordPOS)) {
            return resultList;
        }
        Map<Long, InspectRecordPO> recordPOMap = new HashMap<>();
        List<Long> targetIds = recordPOS.stream().map(r -> {
            recordPOMap.put(r.getTargetId(), r);
            return r.getTargetId();
        }).collect(Collectors.toList());
        // 获取该记录的任务下的其他对象的巡检内容
        List<InspectTaskTargetItemPO> taskItemRelationPOS = inspectTaskTargetItemMapper.selectList(Wrappers
                                                                                                           .<InspectTaskTargetItemPO>lambdaQuery()
                                                                                                           .select(InspectTaskTargetItemPO::getTargetId, InspectTaskTargetItemPO::getStandardId, InspectTaskTargetItemPO::getItemId, InspectTaskTargetItemPO::getResultId)
                                                                                                           .eq(InspectTaskTargetItemPO::getTaskId, recordPO.getTaskId())
                                                                                                           .in(InspectTaskTargetItemPO::getTargetId, targetIds));
        if (CollectionUtils.isEmpty(taskItemRelationPOS)) {
            return resultList;
        }
        // 按对象id分组
        Map<Long, List<InspectTaskTargetItemPO>> targetRelationMap = taskItemRelationPOS.stream()
                .collect(Collectors.groupingBy(InspectTaskTargetItemPO::getTargetId));
        /* 遍历对象map比较对象的巡检内容是否相等，将相等对象加入返回列表中，并记录相等对象的id */
        List<Long> replicableTtargetIdList = new ArrayList<>();
        targetRelationMap.forEach((targetId, targetList) -> {
            // 判断巡检内容是否与源对象相等
            if (this.batchEqTargetItem(sourceTargetList, targetList)) {
                ReplicableTargetVO replicableTargetVO = new ReplicableTargetVO();
                replicableTargetVO.setTargetId(targetId);
                // 加入返回列表中
                resultList.add(replicableTargetVO);
                // 记录对象id
                replicableTtargetIdList.add(targetId);
            }
        });
        if (CollectionUtils.isEmpty(replicableTtargetIdList)) {
            return resultList;
        }
        // 填充对象名
        resultList.forEach(vo -> {
            InspectRecordPO record = recordPOMap.get(vo.getTargetId());
            if (record != null) {
                vo.setRecordId(record.getId());
                vo.setTargetId(record.getTargetId());
                vo.setTargetName(record.getTargetName());
                vo.setTargetCategory(record.getTargetCategory());
                vo.setAreaId(record.getAreaId());
                vo.setAreaName(record.getAreaName());
            }
        });

        return resultList;
    }

    private boolean batchEqTargetItem(List<InspectTaskTargetItemPO> sourceList, List<InspectTaskTargetItemPO> targetList) {
        if (CollectionUtils.isEmpty(sourceList) || CollectionUtils.isEmpty(targetList)) {
            return false;
        }
        if (sourceList.size() != targetList.size()) {
            return false;
        }
        Set<List<Long>> sourceSet = sourceList.stream()
                .map(obj -> Arrays.asList(obj.getStandardId(), obj.getItemId(), obj.getResultId()))
                .collect(Collectors.toSet());
        Set<List<Long>> targetSet = targetList.stream()
                .map(obj -> Arrays.asList(obj.getStandardId(), obj.getItemId(), obj.getResultId()))
                .collect(Collectors.toSet());

        return sourceSet.equals(targetSet);
    }

    /**
     * 获取修复对象分页
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<InspectTargetRepairVO> getRecordRepairPage(InspectRepairQueryParam param) {
        Page<InspectTargetRepairVO> page = inspectRecordMapper.getRecordRepairPage(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        page.getRecords().forEach(record -> record.getRecordToBusinessVOS()
                .removeIf(inspectRecordToBusinessVO -> inspectRecordToBusinessVO.getId() == null));

        /* 扩展工单 */
        List<InspectRecordToBusinessVO> recordToBusinessVOList = new ArrayList<>();
        List<Long> recordIdList = new ArrayList<>();
        Set<Long> taskIdSet = new HashSet<>();
        page.getRecords().forEach(r -> {
            recordToBusinessVOList.addAll(r.getRecordToBusinessVOS());
            recordIdList.add(r.getId());
            taskIdSet.add(r.getTaskId());
        });
        inspectRecordToBusinessFacade.extBusinessOrder(recordToBusinessVOList);
        /* 扩展具体地址 */
        Map<Long, TaskTargetRelationPO> targetInfoMap = new HashMap<>();
        List<TaskTargetRelationPO> planRelationPOList = taskTargetRelationMapper.selectList(Wrappers.<TaskTargetRelationPO>lambdaQuery()
                                                                                                    .in(TaskTargetRelationPO::getTaskId, taskIdSet)
                                                                                                    //这里排序是为了map最新的覆盖旧的
                                                                                                    .orderByAsc(TaskTargetRelationPO::getCreateTime));
        planRelationPOList.forEach(planRelationPO -> targetInfoMap.put(planRelationPO.getTargetId(), planRelationPO));
        /* 扩展异常 */
        List<InspectRecordItemPO> itemPOList = inspectRecordItemMapper.selectList(Wrappers.<InspectRecordItemPO>lambdaQuery()
                                                                                          .in(InspectRecordItemPO::getRecordId, recordIdList)
                                                                                          .eq(InspectRecordItemPO::getErrorValueFlag, 1));
        Map<Long, List<InspectRecordItemPO>> itemMap = Optional.ofNullable(itemPOList).orElse(Collections.emptyList()).stream()
                .collect(Collectors.groupingBy(InspectRecordItemPO::getRecordId));
        page.getRecords().forEach(r -> {
            TaskTargetRelationPO targetInfo = targetInfoMap.get(r.getTargetId());
            if (targetInfo != null) {
                r.setAddress(targetInfo.getTargetAddress());
            }
            List<InspectRecordItemPO> tempList = itemMap.get(r.getId());
            if (CollectionUtils.isNotEmpty(tempList)) {
                // 拼接异常名
                r.setErrIds(tempList.stream().map(i -> String.valueOf(i.getId())).collect(Collectors.joining(",")));
                r.setErrNames(tempList.stream().map(InspectRecordItemPO::getItemName).collect(Collectors.joining(",")));
            }

        });
        return PagingUtils.convert(page);
    }

    /**
     * 获取修复对象详情
     *
     * @param param 条件
     *
     * @return 详情
     */
    @Override
    public InspectTargetRepairVO getRecordRepairDetail(InspectRepairQueryParam param) {
        Assert.isParameterError(param.getRecordId() == null, "记录不能为空");
        InspectTargetRepairVO repairVO = new InspectTargetRepairVO();
        Page<InspectTargetRepairVO> page = inspectRecordMapper.getRecordRepairPage(PagingUtils.convert(param), param);
        page.getRecords().forEach(record -> record.getRecordToBusinessVOS()
                .removeIf(inspectRecordToBusinessVO -> inspectRecordToBusinessVO.getId() == null));
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return repairVO;
        } else {
            repairVO = page.getRecords().getFirst();
        }
        /* 协同人 */
        var task = inspectTaskMapper.selectById(repairVO.getTaskId());
        if (task != null && StringUtils.isNotBlank(task.getAssistIds())) {
            repairVO.setAssistIds(task.getAssistIds());
            repairVO.setAssistNames(inspectQuery.extendUserNames(task.getTenant(), task.getAssistIds()));
        }
        /* 取消修复的文件 */
        if (repairVO.getCancelFileKey() != null) {
            repairVO.setCancelFileVOList(fileClient.getByBusinessKey(repairVO.getCancelFileKey()));
        }
        /* 取消巡检的文件 */
        if (repairVO.getCancelInspectFileKey() != null) {
            repairVO.setCancelInspectFileList(fileClient.getByBusinessKey(repairVO.getCancelInspectFileKey()));
        }
        /* 扩展工单 */
        inspectRecordToBusinessFacade.extBusinessOrder(repairVO.getRecordToBusinessVOS());
        /* 获取手动处理内容 */
        Map<Long, List<InspectRecordRepairVO>> repairMap = this.getRecordRepairList(Collections.singletonList(param.getRecordId()));
        repairVO.setInspectRecordRepairVOList(repairMap.get(param.getRecordId()));
        /* 扩展异常项 */
        List<InspectRecordItemPO> itemPOList = inspectRecordItemMapper.selectList(Wrappers.<InspectRecordItemPO>lambdaQuery()
                                                                                          .eq(InspectRecordItemPO::getRecordId, param.getRecordId())
                                                                                          .eq(InspectRecordItemPO::getErrorValueFlag, 1));
        if (CollectionUtils.isNotEmpty(itemPOList)) {
            List<InspectRecordItemVO> itemVOList = ObjectUtils.copying(itemPOList, InspectRecordItemVO.class);
            InspectRecordItemQueryServiceImpl.recordItemListExtFile(itemVOList, fileClient);
            repairVO.setItemVOList(itemVOList);
        }

        return repairVO;
    }

    /**
     * 巡检校验
     *
     * @param param taskIds 任务id，targetId 对象id
     *              任务id只能有一个
     *
     * @return 是否符合
     */
    @Override
    public Boolean checkSubmitRecord(InspectRecordSubmitCheckParam param) {
        List<InspectRecordAndTaskDTO> recordAndTaskIdList = this.baseMapper.getRecordAndTaskId(param.getRecordIdList());
        Assert.isConflict(CollectionUtils.isEmpty(recordAndTaskIdList), "记录或任务不存在");
        StringBuffer stringBuffer = new StringBuffer();
        recordAndTaskIdList.forEach(record -> {
            if (!InspectRecordStatusEnum.EXECUTING.equals(record.getStatus())) {
                stringBuffer.append(record.getTargetName()).append("已被巡检！");
            }
        });
        Assert.isConflict(!stringBuffer.isEmpty(), stringBuffer.toString());
        return true;
    }

    /**
     * 获取非未执行的记录总数
     *
     * @param period 时间
     *
     * @return 总数
     */
    private Long getTotalNotExecNumber(Period period, InspectionCommonParam param) {
        var recordIdSet = new ArrayList<Long>();
        if (StringUtils.isNotBlank(param.getItemName())) {
            var inspectRecordItemPOList = inspectRecordItemMapper.selectList(Wrappers.<InspectRecordItemPO>lambdaQuery()
                                                                                     .like(InspectRecordItemPO::getItemName, param.getItemName()));
            inspectRecordItemPOList.forEach(inspectRecordItemPO -> recordIdSet.add(inspectRecordItemPO.getRecordId()));
        }
        var totalInt = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getAreaName()), InspectRecordPO::getAreaName, param.getAreaName())
                .eq(StringUtils.isNotBlank(param.getObjectType()), InspectRecordPO::getTargetCategory, param.getObjectType())
                .ge(period != null &&
                    period.getBegin() != null, InspectRecordPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin))
                .le(period != null && period.getEnd() != null, InspectRecordPO::getCreateTime, ObjectUtils.ifNull(period, Period::getEnd))
                .in(CollectionUtils.isNotEmpty(recordIdSet), InspectRecordPO::getId, recordIdSet)
                .count();
        if (totalInt == null) {
            totalInt = 0L;
        }

        return totalInt;
    }

    /**
     * 获取记录的手动处理内容
     *
     * @param recordIdList 记录id集合
     *
     * @return 手动处理列表
     */
    private Map<Long, List<InspectRecordRepairVO>> getRecordRepairList(List<Long> recordIdList) {

        List<InspectRecordRepairPO> handledManuallyPOS = inspectRecordRepairMapper.selectList(Wrappers
                                                                                                      .<InspectRecordRepairPO>lambdaQuery()
                                                                                                      .in(InspectRecordRepairPO::getRecordId, recordIdList)
                                                                                                      .orderByDesc(InspectRecordRepairPO::getCreateTime));
        if (CollectionUtils.isNotEmpty(handledManuallyPOS)) {
            List<InspectRecordRepairVO> handledManuallyVOS = ObjectUtils.copying(handledManuallyPOS, InspectRecordRepairVO.class);
            List<Long> ids = handledManuallyVOS.stream().map(InspectRecordRepairVO::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ids)) {
                BusinessKeysParam businessKeysParam = new BusinessKeysParam();
                businessKeysParam.setBusinessKeys(ids);
                Map<Long, List<FileVO>> fileMap = fileClient.getByBusinessKeys(businessKeysParam);
                handledManuallyVOS.forEach(h -> h.setFileList(fileMap.get(h.getId())));
            }
            return handledManuallyVOS.stream()
                    .collect(Collectors.groupingBy(InspectRecordRepairVO::getRecordId, Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                list.sort(Comparator.comparing(InspectRecordRepairVO::getCreateTime).reversed());
                                return list;
                            }
                                                                                                                   )));
        }
        return new HashMap<>();
    }
}
