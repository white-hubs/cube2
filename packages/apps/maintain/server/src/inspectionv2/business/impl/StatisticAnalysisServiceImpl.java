package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.ResourceEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.CommonUtil;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dao.FaultOrderMapper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration.FaultResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration.FaultStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultOrder;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerNewMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration.HiddenDangerResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration.HiddenDangerStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.StatisticAnalysisService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectTargetInfoQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordToBusinessMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.CountRatioParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DepartmentInspectionParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionNumParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionObjQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionResultParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.ResultRunInHoleParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskDetailParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskStandardAbnormalRateParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CheckTheMismatchDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepInspectionDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepInspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepPersonNumVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepartmentHiddenDangerStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepartmentInspectionNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DeviceInspectionVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.HiddenDangerDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordItemVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionItemResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionResultDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.StaffHiddenDangerStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.StaffInspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskStandardAbnormalRateVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TroubleListDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.utils.DateUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 统计分析
 * @date 2023/4/14 14:51
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatisticAnalysisServiceImpl implements StatisticAnalysisService {

    private final InspectTaskMapper inspectTaskMapper;
    private final InspectRecordToBusinessMapper inspectRecordToBusinessMapper;
    private final HiddenDangerNewMapper hiddenDangerMapper;
    private final InspectRecordMapper inspectRecordMapper;
    private final InspectRecordItemMapper inspectRecordItemMapper;
    private final UserClient userClient;
    private final InspectQuery inspectQuery;
    private final InspectTargetInfoQuery inspectTargetInfoQuery;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ExportExcelService exportExcelService;
    private final FaultOrderService faultOrderService;
    private final FaultOrderMapper faultOrderMapper;
    private static final String RECEIVE_TYPE = "1";
    /**
     * redis存放任务稽查结果信息
     */
    private final static String CHECK_TASK_RESULT = "checkTaskResult:";
    /**
     * 稽查不匹配项
     */
    private final static String UN_MATCH_ITEM = "unMatchItem";
    /**
     * 稽查结果匹配率
     */
    private final static String RATIO = "ratio";

    public Period getPeriod(Period paramPeriod, String dateType) {
        Period period = null;
        if (paramPeriod != null) {
            period = DateUtil.simpleDateTypeHandle(dateType, paramPeriod);
        }
        return period;
    }

    /**
     * @Author wql
     * @Description 巡检结果统计
     * @Date 14:54 2023/4/14
     **/
    @Override
    public Pagination<InspectionResultVO> statisticalResult(InspectionResultParam param) {
        Period period = getPeriod(param.getPeriod(), "");
        LambdaQueryWrapper<InspectTaskPO> wrapper = Wrappers.<InspectTaskPO>lambdaQuery()
                .eq(InspectTaskPO::getParentId, 0)
                .eq(InspectTaskPO::getCheckTaskId, 0)
                .eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.OVER)
                .eq(ObjectUtils.isNotEmpty(param.getResult()), InspectTaskPO::getResult, param.getResult())
                .eq(ObjectUtils.isNotEmpty(param.getTypeId()), InspectTaskPO::getTypeId, param.getTypeId())
                .like(ObjectUtils.isNotEmpty(param.getTaskName()), InspectTaskPO::getName, param.getTaskName())
                .between(period !=
                         null, InspectTaskPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .orderByDesc(InspectTaskPO::getEndTime);
        Page<InspectTaskPO> taskPage = inspectTaskMapper.selectPage(PagingUtils.convert(param), wrapper);
        List<InspectionResultVO> list = SetStatisticalResult(taskPage.getRecords());
        Page<InspectionResultVO> page = new Page<>(taskPage.getCurrent(), taskPage.getPages(), taskPage.getTotal());
        page.setRecords(list);
        return PagingUtils.convert(page);
    }

    /**
     * @Author wql
     * @Description 统计其他字段
     * @Date 9:57 2023/4/25
     **/
    private List<InspectionResultVO> SetStatisticalResult(List<InspectTaskPO> records) {
        List<InspectionResultVO> list = new ArrayList<>();
        Optional.ofNullable(records).orElse(Collections.emptyList()).forEach(vo -> {
            InspectionResultVO build = InspectionResultVO.builder().id(vo.getId()).name(vo.getName()).targetNum(vo.getTargetNum())
                    .typeName(vo.getTypeName()).isSplit(vo.getIsSplit()).checkResult(InspectRecordResultEnum.getDescription(vo.getResult()))
                    .beginTime(vo.getBeginTime()).endTime(vo.getEndTime()).build();
            //设置巡检人员
            build.setInspectionStaff(getTaskUserName(RECEIVE_TYPE, vo.getReceiveDeptIds(), vo.getReceivePostIds(),
                                                     vo.getReceiveRoleIds(), vo.getReceiveIds(), vo.getTenant()));
            //设置稽查信息
            setTaskCheckInfo(build);
            //设置报修总数
            build.setFaultNum(inspectRecordToBusinessMapper.countByTaskId(vo.getId(), MaintainApplicationEnum.FAULT.name()));
            //设置隐患总数
            build.setHiddenDangerNum(countHiddenDangerNum(TaskDetailParam.builder().taskId(vo.getId()).build()));
            //设置巡检任务耗时
            build.setElapsedTime(setUseTime(vo.getBeginTime(), vo.getEndTime()));
            list.add(build);
        });
        return list;
    }

    /**
     * @Author wql
     * @Description 设置任务责任人
     * @Date 11:12 2023/4/26
     **/
    private String getTaskUserName(String receiveType, String receiveDeptIds, String receivePostIds, String receiveRoleIds,
                                   String receiveIds, Integer tenant
                                  ) {
        if ("1".equals(receiveType)) {
            //个人
            return getUserNameByIds(receiveIds);
        } else {
            String str = "";
            //部门
            String depName = inspectQuery.extendDeptNames(tenant, receiveDeptIds);
            String postName = inspectQuery.extendPostNames(tenant, receivePostIds);
            String roleName = inspectQuery.extendRoleNames(tenant, receiveRoleIds);
            if (StringUtils.isNotBlank(depName)) {
                str = str + depName + "-";
            }
            if (StringUtils.isNotBlank(postName)) {
                str = str + postName + "-";
            }
            if (StringUtils.isNotBlank(roleName)) {
                str = str + roleName + "-";
            }
            if (StringUtils.isNotBlank(str)) {
                return str.substring(0, str.length() - 1);
            }
            return str;
        }
    }

    /**
     * @Author wql
     * @Description 巡检结果统计导出
     * @Date 9:49 2023/4/25
     **/
    @Override
    public ExcelWriter resultStatisticsExport(InspectionResultParam param) {
        Period period = getPeriod(param.getPeriod(), "");
        List<InspectTaskPO> taskPage = inspectTaskMapper.selectList(Wrappers.<InspectTaskPO>lambdaQuery()
                                                                            .eq(InspectTaskPO::getParentId, 0)
                                                                            .eq(InspectTaskPO::getCheckTaskId, 0)
                                                                            .eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.OVER)
                                                                            .eq(ObjectUtils.isNotEmpty(param.getResult()), InspectTaskPO::getResult, param.getResult())
                                                                            .eq(ObjectUtils.isNotEmpty(param.getTypeId()), InspectTaskPO::getTypeId, param.getTypeId())
                                                                            .like(ObjectUtils.isNotEmpty(param.getTaskName()), InspectTaskPO::getName, param.getTaskName())
                                                                            .between(period !=
                                                                                     null, InspectTaskPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                            .orderByDesc(InspectTaskPO::getEndTime)
                                                                   );
        List<InspectionResultVO> list = SetStatisticalResult(taskPage);
        return exportExcelService.resultStatisticsExport(list);
    }

    /**
     * @Author wql
     * @Description 计算巡检耗时
     * @Date 16:15 2023/4/14
     **/
    private String setUseTime(LocalDateTime createTime, LocalDateTime endTime) {
        if (createTime == null || endTime == null) {
            return "超时未完成！";
        } else {
            long between = DateUtils.between(createTime, endTime, ChronoUnit.MINUTES);
            return countTimeStr(between);
        }
    }

    /**
     * @Author wql
     * @Description 分钟时间转间隔时间
     * @Date 14:34 2023/4/17
     **/
    public String countTimeStr(Long minutes) {
        if (minutes == null) {
            return "0分钟";
        }
        if (minutes > 24 * 60L) {
            long day = minutes / (24 * 60L);
            long hour = (minutes % (24 * 60L)) / 60L;
            long minute = (minutes % (24 * 60L)) % 60L;
            return day + "天" + (hour == 0 ? "" : (hour + "小时")) + (minute == 0 ? "" : (minute + "分钟"));
        } else if (minutes > 60L) {
            long hour = minutes / 60L;
            long minute = minutes % 60L;
            return hour + "小时" + (minute == 0 ? "" : (minute + "分钟"));
        }
        return minutes + "分钟";
    }

    /**
     * @Author wql
     * @Description 设置稽查信息
     * @Date 16:12 2023/4/14
     **/
    private void setTaskCheckInfo(InspectionResultVO vo) {
        String ratio = (String) redisTemplate.opsForHash().get(CHECK_TASK_RESULT + vo.getId(), RATIO);
        List<String> unMatchItem = (List<String>) redisTemplate.opsForHash().get(CHECK_TASK_RESULT + vo.getId(), UN_MATCH_ITEM);
        InspectTaskPO checkTask = inspectTaskMapper.selectOne(Wrappers.<InspectTaskPO>lambdaQuery()
                                                                      .eq(InspectTaskPO::getCheckTaskId, vo.getId()));
        if (ObjectUtils.isEmpty(ratio) || ObjectUtils.isNull(unMatchItem)) {
            //分别统计每个巡检记录
            if (ObjectUtils.isNotEmpty(checkTask)) {
                vo.setCheckStaff(getTaskUserName(RECEIVE_TYPE, checkTask.getReceiveDeptIds(), checkTask.getReceivePostIds(),
                                                 checkTask.getReceiveRoleIds(), checkTask.getReceiveIds(), checkTask.getTenant()));
                vo.setCheckTaskId(checkTask.getId());
                List<InspectionTimeVO> checkRecord = getEachInspectionRecord(checkTask.getId(), false);
                //稽查结果不匹配项id
                List<String> ids = new ArrayList<>();
                //计算稽查结果匹配率
                List<InspectionTimeVO> inspectionRecord = getEachInspectionRecord(vo.getId(), vo.getIsSplit());
                String matchRatio = countMachRatio(inspectionRecord, checkRecord, ids);
                vo.setCheckMatchRatio(matchRatio);
                vo.setCheckNotMatchNum(ids.size());
                vo.setNotMatchIds(ids);
                redisTemplate.opsForHash().put(CHECK_TASK_RESULT + vo.getId(), RATIO, matchRatio);
                redisTemplate.opsForHash().put(CHECK_TASK_RESULT + vo.getId(), UN_MATCH_ITEM, ids);
            }
        } else {
            vo.setCheckStaff(getTaskUserName(RECEIVE_TYPE, checkTask.getReceiveDeptIds(), checkTask.getReceivePostIds(),
                                             checkTask.getReceiveRoleIds(), checkTask.getReceiveIds(), checkTask.getTenant()));
            vo.setCheckMatchRatio(ratio);
            vo.setCheckNotMatchNum(unMatchItem.size());
        }
    }

    /**
     * @Author wql
     * @Description 计算任务稽查匹配率
     * @Date 17:05 2023/4/19
     **/
    private String countTaskCheckRatio(Long taskId, Boolean isSplit) {
        List<InspectionTimeVO> inspectionRecord = getEachInspectionRecord(taskId, isSplit);
        InspectTaskPO checkTask = inspectTaskMapper.selectOne(Wrappers.<InspectTaskPO>lambdaQuery()
                                                                      .eq(InspectTaskPO::getCheckTaskId, taskId));
        if (ObjectUtils.isNotEmpty(checkTask)) {
            List<InspectionTimeVO> checkRecord = getEachInspectionRecord(checkTask.getId(), false);
            return countMachRatio(inspectionRecord, checkRecord, new ArrayList<>());
        }
        return null;
    }

    /**
     * @Author wql
     * @Description 计算稽查结果匹配率
     * @Date 14:52 2023/4/19
     **/
    public String countMachRatio(List<InspectionTimeVO> inspectionRecord, List<InspectionTimeVO> checkRecord, List<String> ids) {
        ConcurrentMap<Long, InspectionTimeVO> map = Maps.newConcurrentMap();
        Optional.ofNullable(inspectionRecord).orElse(Collections.emptyList()).parallelStream().forEach(vo -> {
            map.put(vo.getTargetId(), vo);
        });
        CountRatioParam param = new CountRatioParam(0L, 0L);
        for (InspectionTimeVO check : checkRecord) {
            countMach(check, map.get(check.getTargetId()), param, ids);
        }
        return CommonUtil.calculateProportionPercentageAsStringLegacy(param.getTotalItem(), param.getTotalItem() - param.getNotMatchItem());
    }

    /**
     * @Author wql
     * @Description 比较每个巡检项是否匹配
     * @Date 15:02 2023/4/19
     **/
    private void countMach(InspectionTimeVO check, InspectionTimeVO inspection, CountRatioParam param, List<String> ids) {
        Long totalItem = param.getTotalItem();
        Long notMatchItem = param.getNotMatchItem();
        if (ObjectUtils.isNotEmpty(check) && ObjectUtils.isNotEmpty(inspection)) {
            List<InspectionItemResultVO> checkItems = inspectRecordItemMapper.selectResultList(check.getRecordId());
            List<InspectionItemResultVO> inspectionItems = inspectRecordItemMapper.selectResultList(inspection.getRecordId());
            Map<Long, Integer> resultMap = inspectionItems.stream()
                    .collect(Collectors.toMap(InspectionItemResultVO::getItemId, InspectionItemResultVO::getResultFlag));
            Map<Long, Long> recordMap = inspectionItems.stream()
                    .collect(Collectors.toMap(InspectionItemResultVO::getItemId, InspectionItemResultVO::getId));
            for (InspectionItemResultVO item : checkItems) {
                totalItem += 1;
                if (!(Objects.equals(item.getResultFlag(), resultMap.get(item.getItemId())))) {
                    notMatchItem++;
                    ids.add(item.getId() + "," + recordMap.get(item.getItemId()));
                }
            }
            param.setTotalItem(totalItem);
            param.setNotMatchItem(notMatchItem);
        }
    }

    /**
     * @Author wql
     * @Description 分别统计每个巡检记录耗时
     * @Date 14:39 2023/4/19
     **/
    public List<InspectionTimeVO> getEachInspectionRecord(Long id, Boolean isSplit) {
        List<InspectionTimeVO> inspectionTimes = new ArrayList<>();
        if (!isSplit) {
            //非拆解任务直接查主任务id
            inspectionTimes = inspectRecordMapper.getResultAndCountTime(id);
        } else {
            //拆解任务需要先查出每个子任务
            List<InspectTaskPO> subTask = inspectTaskMapper.selectList(Wrappers.<InspectTaskPO>lambdaQuery()
                                                                               .eq(InspectTaskPO::getParentId, id));
            for (InspectTaskPO po : Optional.ofNullable(subTask).orElse(Collections.emptyList())) {
                inspectionTimes.addAll(inspectRecordMapper.getResultAndCountTime(po.getId()));
            }
        }
        return Optional.ofNullable(inspectionTimes).orElse(Collections.emptyList());
    }

    /**
     * @Author wql
     * @Description 稽查不匹配项明细
     * @Date 17:39 2023/4/14
     **/
    public Pagination<CheckTheMismatchDetailVO> checkDetail(ResultRunInHoleParam resultParam) {
        List<CheckTheMismatchDetailVO> details = new ArrayList<>();
        List<String> unMatchItem = (List<String>) redisTemplate.opsForHash()
                .get(CHECK_TASK_RESULT + resultParam.getTaskId(), UN_MATCH_ITEM);
        Page<CheckTheMismatchDetailVO> page = new Page<>();
        if (ObjectUtils.isNotEmpty(unMatchItem)) {
            Integer pageSize = resultParam.getPageSize();
            Integer pageNumber = resultParam.getPageNumber();
            int end = pageSize * pageNumber;
            int count = unMatchItem.size();
            int pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            page.setTotal(count);
            page.setPages(pages);
            page.setCurrent(pageNumber);
            List<String> subList = unMatchItem.subList((Math.min(end - pageSize, unMatchItem.size())), (Math.min(end, unMatchItem.size())));
            getCheckDetailList(details, subList);
        }
        page.setRecords(details);
        return PagingUtils.convert(page);
    }

    /**
     * @Author wql
     * @Description 查询不匹配项明细
     * @Date 16:05 2023/4/26
     **/
    private void getCheckDetailList(List<CheckTheMismatchDetailVO> details, List<String> list) {
        Optional.ofNullable(list).orElse(Collections.emptyList()).parallelStream().forEach(id -> {
            if (StringUtils.isNotBlank(id)) {
                String[] split = id.split(",");
                CheckTheMismatchDetailVO checkDetail = Optional.ofNullable(inspectRecordItemMapper.queryCheckDetail(Long.valueOf(split[0])))
                        .orElse(new CheckTheMismatchDetailVO());
                if (StringUtils.isNotBlank(split[1])) {
                    CheckTheMismatchDetailVO inspectionDetail = Optional
                            .ofNullable(inspectRecordItemMapper.queryInspectionDetail(Long.valueOf(split[1])))
                            .orElse(new CheckTheMismatchDetailVO());
                    checkDetail.setResult(inspectionDetail.getResult());
                    checkDetail.setReportResult(inspectionDetail.getReportResult());
                    checkDetail.setPrincipal(inspectionDetail.getPrincipal());
                    checkDetail.setContent(inspectionDetail.getContent());
                }
                details.add(checkDetail);
            }
        });
    }

    /**
     * @Author wql
     * @Description 巡检结果分析-隐患记录
     * @Date 8:49 2023/4/17
     **/
    @Override
    public Pagination<HiddenDangerDetailVO> getHiddenDangerDetail(TaskDetailParam detailParam) {
        Period period = getPeriod(detailParam.getPeriod(), "");
        detailParam.setPeriod(period);
        Page<HiddenDangerDetailVO> detail = hiddenDangerMapper.getHiddenDangerDetail(PagingUtils.convert(detailParam), detailParam);
        Optional.ofNullable(detail.getRecords()).orElse(Collections.emptyList()).parallelStream().forEach(vo -> {
            vo.setState(TicketStatusEnum.getDescription(vo.getState()));
            vo.setResult(vo.getResult());
        });
        return PagingUtils.convert(detail);
    }

    /**
     * @Author wql
     * @Description 巡检结果分析-隐患记录导出
     * @Date 9:50 2023/4/17
     **/
    @Override
    public ExcelWriter getAllHiddenDangerDetail(TaskDetailParam detailParam) {
        Period period = getPeriod(detailParam.getPeriod(), "");
        detailParam.setPeriod(period);
        Page<Object> page = PagingUtils.convert(detailParam);
        page.setPages(0L);
        Page<HiddenDangerDetailVO> detail = hiddenDangerMapper.getHiddenDangerDetail(page, detailParam);
        Optional.ofNullable(detail.getRecords()).orElse(Collections.emptyList()).parallelStream().forEach(vo -> {
            vo.setState(HiddenDangerStatusEnum.getDescription(vo.getState()));
            vo.setResult(HiddenDangerResultEnum.getDescription(vo.getResult()));
        });
        return exportExcelService.getAllHiddenDangerDetail(Optional.ofNullable(detail.getRecords()).orElse(Collections.emptyList()));
    }

    /**
     * @Author wql
     * @Description 巡检结果统计-报修记录明细
     * @Date 10:46 2023/4/17
     **/
    @Override
    public Pagination<TroubleListDetailVO> repairsDetail(ResultRunInHoleParam resultParam) {
        List<String> ids = inspectRecordToBusinessMapper.getProcInstanceIds(resultParam.getTaskId());
        if (ObjectUtils.isNotEmpty(ids)) {
            Page<FaultOrder> pagination = faultOrderService.pageQuery(resultParam, ids);
            return PagingUtils.convert(pagination, getTroubleListDetailVO(pagination));
        }
        return null;
    }

    private List<TroubleListDetailVO> getTroubleListDetailVO(Page<FaultOrder> pagination) {
        final List<TroubleListDetailVO> detailVOS = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(pagination.getRecords())) {
            pagination.getRecords().forEach(vo -> {
                detailVOS.add(TroubleListDetailVO.builder()
                                      .submitTime(DateUtils.format(vo.getSubmitTime()))
                                      .name(vo.getName())
                                      .equipment(vo.getObjName())
                                      .source(vo.getSource())
                                      .submitStaff(vo.getSubmitStaff())
                                      .state(TicketStatusEnum.getDescription(vo.getState()))
                                      .result(vo.getResult())
                                      .build());
            });
        }
        return detailVOS;
    }

    /**
     * @Author wql
     * @Description 报修记录明细导出
     * @Date 11:19 2023/4/17
     **/
    @Override
    public ExcelWriter repairsDetailExport(ResultRunInHoleParam resultParam) {
        List<String> ids = inspectRecordToBusinessMapper.getProcInstanceIds(resultParam.getTaskId());
        List<TroubleListDetailVO> detailVOS = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(ids)) {
            resultParam.setPageNumber(1);
            resultParam.setPageSize(ids.size());
            Page<FaultOrder> pagination = faultOrderService.pageQuery(resultParam, ids);
            detailVOS = getTroubleListDetailVO(pagination);
        }
        return exportExcelService.repairsDetailExport(detailVOS);
    }

    /**
     * @Author wql
     * @Description 部门巡检统计
     * @Date 11:38 2023/4/17
     **/
    @Override
    public Pagination<DepInspectionStatisticsVO> departmentStatistics(DepartmentInspectionParam depParam) {
        Period period = getPeriod(depParam.getPeriod(), "");
        Page<DepInspectionStatisticsVO> list = inspectRecordMapper.departmentStatistics(PagingUtils.convert(depParam), Wrappers.query()
                                                                                                .isNotNull("a.time")
                                                                                                .isNotNull("a.organization_id")
                                                                                                .like(ObjectUtils.isNotEmpty(depParam.getDepName()), "a.organization_name", depParam.getDepName())
                                                                                                .between(period != null, "a.time", ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                                                .groupBy("a.organization_id")
                                                                                       );
        disposeDepartmentStatistics(list.getRecords(), period);
        return PagingUtils.convert(list);
    }

    /**
     * @Author wql
     * @Description 统计其他字段
     * @Date 10:07 2023/4/25
     **/
    private void disposeDepartmentStatistics(List<DepInspectionStatisticsVO> list, Period period) {
        long objTotalNum = inspectTargetInfoQuery.getAllObjTotalNum();
        Map<Long, Integer> personNum = countPersonNum(period);
        List<Long> depIds = getDepartmentIds(list);
        Map<Long, DepartmentTaskStatistics> taskStatisticsMap = countDepartmentTaskStatistics(depIds, period);
        Map<Long, DepartmentHiddenDangerStatisticsVO> hiddenDangerStatisticsMap = countDepartmentHiddenDangerStatistics(depIds, period);
        Map<Long, String> checkMatchRatioMap = countDepartmentCheckMatchRatio(depIds);
        for (DepInspectionStatisticsVO vo : Optional.ofNullable(list).orElse(Collections.emptyList())) {
            vo.setCoverObjRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(vo.getCoverObjNum(), objTotalNum)));
            DepartmentTaskStatistics taskStatistics = taskStatisticsMap.getOrDefault(vo.getDepId(), new DepartmentTaskStatistics());
            vo.setTaskTotalNum(taskStatistics.getTaskTotalNum());
            vo.setCompleteTaskNum(taskStatistics.getCompleteTaskNum());
            vo.setExpireTaskNum(taskStatistics.getExpireTaskNum());
            vo.setTaskCompleteRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(vo.getCompleteTaskNum(), vo.getTaskTotalNum())));
            vo.setInspectionElapsedTimeStr(countTimeStr(vo.getInspectionElapsedTime()));
            vo.setCheckMatchRatio(checkMatchRatioMap.getOrDefault(vo.getDepId(), ""));
            DepartmentHiddenDangerStatisticsVO hiddenDangerStatistics = hiddenDangerStatisticsMap.getOrDefault(vo.getDepId(), new DepartmentHiddenDangerStatisticsVO());
            Integer total = hiddenDangerStatistics.getAbnormalHiddenDangerNum();
            if (total == null) {
                total = 0;
            }
            vo.setAbnormalHiddenDangerNum(total);
            Integer complete = hiddenDangerStatistics.getHaveDoneHiddenDangerNum();
            if (complete == null) {
                complete = 0;
            }
            vo.setHaveDoneHiddenDangerNum(complete);
            vo.setHiddenDangerDisposeRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(complete, total)));
            vo.setPersonNum(personNum.get(vo.getDepId()));
        }
    }

    /**
     * 统计部门参数巡检人数
     *
     * @param period 查询时间范围
     *
     * @return 人数
     */
    private Map<Long, Integer> countPersonNum(Period period) {
        List<DepPersonNumVO> vos = inspectRecordMapper.countPersonNum(period);
        if (ObjectUtils.isNotEmpty(vos)) {
            return vos.stream().collect(Collectors.toMap(DepPersonNumVO::getDepId, DepPersonNumVO::getPersonNum));
        }
        return new HashMap<>();
    }

    private List<Long> getDepartmentIds(List<DepInspectionStatisticsVO> list) {
        return Optional.ofNullable(list)
                .orElse(Collections.emptyList())
                .stream()
                .map(DepInspectionStatisticsVO::getDepId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Long> getStaffDepartmentIds(List<StaffInspectionStatisticsVO> list) {
        return Optional.ofNullable(list)
                .orElse(Collections.emptyList())
                .stream()
                .map(StaffInspectionStatisticsVO::getDepId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Long> getStaffIds(List<StaffInspectionStatisticsVO> list) {
        return Optional.ofNullable(list)
                .orElse(Collections.emptyList())
                .stream()
                .map(StaffInspectionStatisticsVO::getStaffId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    private Map<Long, DepartmentTaskStatistics> countDepartmentTaskStatistics(List<Long> depIds, Period period) {
        if (ObjectUtils.isEmpty(depIds)) {
            return Collections.emptyMap();
        }
        List<InspectTaskPO> taskList = queryDepartmentTaskList(depIds, period, null);
        if (ObjectUtils.isEmpty(taskList)) {
            return Collections.emptyMap();
        }
        Set<Long> depIdSet = new HashSet<>(depIds);
        Map<Long, DepartmentTaskStatistics> statisticsMap = new HashMap<>();
        for (InspectTaskPO task : taskList) {
            if (Boolean.TRUE.equals(task.getIsSplit())) {
                continue;
            }
            List<Long> taskDepIds = getMatchedTargetIds(task.getReceiveDeptIds(), depIdSet);
            for (Long depId : taskDepIds) {
                DepartmentTaskStatistics statistics = statisticsMap.computeIfAbsent(depId, key -> new DepartmentTaskStatistics());
                statistics.increaseTaskTotalNum();
                if (Objects.equals(TaskStatusEnum.OVER.name(), getTaskStatus(task))) {
                    statistics.increaseCompleteTaskNum();
                }
                if (Objects.equals(TaskStatusEnum.EXPIRE.name(), getTaskStatus(task))) {
                    statistics.increaseExpireTaskNum();
                }
            }
        }
        return statisticsMap;
    }

    private Map<Long, DepartmentHiddenDangerStatisticsVO> countDepartmentHiddenDangerStatistics(List<Long> depIds, Period period) {
        if (ObjectUtils.isEmpty(depIds)) {
            return Collections.emptyMap();
        }
        List<DepartmentHiddenDangerStatisticsVO> statisticsList = hiddenDangerMapper.countDepartmentHiddenDangerStatistics(depIds, period);
        if (ObjectUtils.isEmpty(statisticsList)) {
            return Collections.emptyMap();
        }
        return statisticsList.stream().collect(Collectors.toMap(DepartmentHiddenDangerStatisticsVO::getDepId, value -> value));
    }

    private Map<Long, String> countDepartmentCheckMatchRatio(List<Long> depIds) {
        if (ObjectUtils.isEmpty(depIds)) {
            return Collections.emptyMap();
        }
        List<InspectTaskPO> taskList = queryDepartmentTaskList(depIds, null, TaskStatusEnum.OVER.name());
        if (ObjectUtils.isEmpty(taskList)) {
            return Collections.emptyMap();
        }
        Set<Long> depIdSet = new HashSet<>(depIds);
        Map<Long, Long> checkTaskMap = getCheckTaskMap(taskList);
        Map<Long, List<Long>> subTaskMap = getSubTaskMap(taskList);
        Map<Long, List<InspectRecordPO>> recordMap = getTaskRecordMap(taskList, checkTaskMap, subTaskMap);
        Map<Long, List<InspectRecordItemVO>> recordItemMap = getRecordItemMap(recordMap);
        Map<Long, List<BigDecimal>> depRatioMap = new HashMap<>();
        for (InspectTaskPO task : taskList) {
            Long checkTaskId = checkTaskMap.get(task.getId());
            if (checkTaskId == null) {
                continue;
            }
            String ratio = countTaskCheckRatio(task, checkTaskId, subTaskMap, recordMap, recordItemMap);
            if (StringUtils.isBlank(ratio)) {
                continue;
            }
            BigDecimal ratioValue = new BigDecimal(ratio.replace("%", ""));
            List<Long> taskDepIds = getMatchedTargetIds(task.getReceiveDeptIds(), depIdSet);
            for (Long depId : taskDepIds) {
                depRatioMap.computeIfAbsent(depId, key -> new ArrayList<>()).add(ratioValue);
            }
        }
        Map<Long, String> result = new HashMap<>();
        for (Map.Entry<Long, List<BigDecimal>> entry : depRatioMap.entrySet()) {
            if (ObjectUtils.isEmpty(entry.getValue())) {
                continue;
            }
            BigDecimal total = BigDecimal.ZERO;
            for (BigDecimal ratio : entry.getValue()) {
                total = total.add(ratio);
            }
            result.put(entry.getKey(), total.divide(BigDecimal.valueOf(entry.getValue().size()), 2, RoundingMode.HALF_UP) + "%");
        }
        return result;
    }

    private List<InspectTaskPO> queryDepartmentTaskList(List<Long> depIds, Period period, String status) {
        if (ObjectUtils.isEmpty(depIds)) {
            return Collections.emptyList();
        }
        return inspectTaskMapper.selectList(Wrappers.<InspectTaskPO>query()
                                                    .select("id", "status", "is_split", "receive_dept_ids")
                                                    .eq(StringUtils.isNotBlank(status), "status", status)
                                                    .between(ObjectUtils.isNotNull(period), "create_time",
                                                             ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                    .and(query -> appendLikeCondition(query, depIds, "receive_dept_ids")));
    }

    private Map<Long, DepartmentTaskStatistics> countStaffTaskStatistics(List<Long> staffIds, Period period) {
        if (ObjectUtils.isEmpty(staffIds)) {
            return Collections.emptyMap();
        }
        List<InspectTaskPO> taskList = queryStaffTaskList(staffIds, period, null);
        if (ObjectUtils.isEmpty(taskList)) {
            return Collections.emptyMap();
        }
        Set<Long> staffIdSet = new HashSet<>(staffIds);
        Map<Long, DepartmentTaskStatistics> statisticsMap = new HashMap<>();
        for (InspectTaskPO task : taskList) {
            if (Boolean.TRUE.equals(task.getIsSplit())) {
                continue;
            }
            List<Long> taskStaffIds = getMatchedTargetIds(task.getReceiveIds(), staffIdSet);
            for (Long staffId : taskStaffIds) {
                DepartmentTaskStatistics statistics = statisticsMap.computeIfAbsent(staffId, key -> new DepartmentTaskStatistics());
                statistics.increaseTaskTotalNum();
                if (Objects.equals(TaskStatusEnum.OVER.name(), getTaskStatus(task))) {
                    statistics.increaseCompleteTaskNum();
                }
                if (Objects.equals(TaskStatusEnum.EXPIRE.name(), getTaskStatus(task))) {
                    statistics.increaseExpireTaskNum();
                }
            }
        }
        return statisticsMap;
    }

    private Map<Long, String> countStaffCheckMatchRatio(List<Long> staffIds) {
        if (ObjectUtils.isEmpty(staffIds)) {
            return Collections.emptyMap();
        }
        List<InspectTaskPO> taskList = queryStaffTaskList(staffIds, null, TaskStatusEnum.OVER.name());
        if (ObjectUtils.isEmpty(taskList)) {
            return Collections.emptyMap();
        }
        Set<Long> staffIdSet = new HashSet<>(staffIds);
        Map<Long, Long> checkTaskMap = getCheckTaskMap(taskList);
        Map<Long, List<Long>> subTaskMap = getSubTaskMap(taskList);
        Map<Long, List<InspectRecordPO>> recordMap = getTaskRecordMap(taskList, checkTaskMap, subTaskMap);
        Map<Long, List<InspectRecordItemVO>> recordItemMap = getRecordItemMap(recordMap);
        Map<Long, List<BigDecimal>> staffRatioMap = new HashMap<>();
        for (InspectTaskPO task : taskList) {
            Long checkTaskId = checkTaskMap.get(task.getId());
            if (checkTaskId == null) {
                continue;
            }
            String ratio = countTaskCheckRatio(task, checkTaskId, subTaskMap, recordMap, recordItemMap);
            if (StringUtils.isBlank(ratio)) {
                continue;
            }
            BigDecimal ratioValue = new BigDecimal(ratio.replace("%", ""));
            List<Long> taskStaffIds = getMatchedTargetIds(task.getReceiveIds(), staffIdSet);
            for (Long staffId : taskStaffIds) {
                staffRatioMap.computeIfAbsent(staffId, key -> new ArrayList<>()).add(ratioValue);
            }
        }
        Map<Long, String> result = new HashMap<>();
        for (Map.Entry<Long, List<BigDecimal>> entry : staffRatioMap.entrySet()) {
            if (ObjectUtils.isEmpty(entry.getValue())) {
                continue;
            }
            BigDecimal total = BigDecimal.ZERO;
            for (BigDecimal ratio : entry.getValue()) {
                total = total.add(ratio);
            }
            result.put(entry.getKey(), total.divide(BigDecimal.valueOf(entry.getValue().size()), 2, RoundingMode.HALF_UP) + "%");
        }
        return result;
    }

    private Map<Long, Long> countDepartmentInspectionNumStatistics(List<Long> depIds, Period period) {
        if (ObjectUtils.isEmpty(depIds)) {
            return Collections.emptyMap();
        }
        List<DepartmentInspectionNumStatisticsVO> statisticsList = inspectRecordMapper.countDepartmentInspectionNum(depIds, period);
        if (ObjectUtils.isEmpty(statisticsList)) {
            return Collections.emptyMap();
        }
        return statisticsList.stream().collect(Collectors.toMap(DepartmentInspectionNumStatisticsVO::getDepId,
                                                                DepartmentInspectionNumStatisticsVO::getInspectionNum));
    }

    private Map<Long, StaffHiddenDangerStatisticsVO> countStaffHiddenDangerStatistics(List<Long> staffIds, Period period) {
        if (ObjectUtils.isEmpty(staffIds)) {
            return Collections.emptyMap();
        }
        List<StaffHiddenDangerStatisticsVO> statisticsList = hiddenDangerMapper.countStaffHiddenDangerStatistics(staffIds, period);
        if (ObjectUtils.isEmpty(statisticsList)) {
            return Collections.emptyMap();
        }
        return statisticsList.stream().collect(Collectors.toMap(StaffHiddenDangerStatisticsVO::getStaffId, value -> value));
    }

    private List<InspectTaskPO> queryStaffTaskList(List<Long> staffIds, Period period, String status) {
        if (ObjectUtils.isEmpty(staffIds)) {
            return Collections.emptyList();
        }
        return inspectTaskMapper.selectList(Wrappers.<InspectTaskPO>query()
                                                    .select("id", "status", "is_split", "receive_ids")
                                                    .eq(StringUtils.isNotBlank(status), "status", status)
                                                    .between(ObjectUtils.isNotNull(period), "create_time",
                                                             ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                    .and(query -> appendLikeCondition(query, staffIds, "receive_ids")));
    }

    private Map<Long, Long> getCheckTaskMap(List<InspectTaskPO> taskList) {
        List<Long> taskIds = taskList.stream().map(InspectTaskPO::getId).filter(Objects::nonNull).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(taskIds)) {
            return Collections.emptyMap();
        }
        List<InspectTaskPO> checkTaskList = inspectTaskMapper.selectList(Wrappers.<InspectTaskPO>query()
                                                                                 .select("id", "check_task_id")
                                                                                 .in("check_task_id", taskIds));
        if (ObjectUtils.isEmpty(checkTaskList)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> checkTaskMap = new HashMap<>();
        for (InspectTaskPO task : checkTaskList) {
            if (task.getCheckTaskId() != null && task.getId() != null) {
                checkTaskMap.put(task.getCheckTaskId(), task.getId());
            }
        }
        return checkTaskMap;
    }

    private Map<Long, List<Long>> getSubTaskMap(List<InspectTaskPO> taskList) {
        List<Long> splitTaskIds = taskList.stream()
                .filter(task -> Boolean.TRUE.equals(task.getIsSplit()))
                .map(InspectTaskPO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (ObjectUtils.isEmpty(splitTaskIds)) {
            return Collections.emptyMap();
        }
        List<InspectTaskPO> subTaskList = inspectTaskMapper.selectList(Wrappers.<InspectTaskPO>query()
                                                                               .select("id", "parent_id")
                                                                               .in("parent_id", splitTaskIds));
        if (ObjectUtils.isEmpty(subTaskList)) {
            return Collections.emptyMap();
        }
        Map<Long, List<Long>> subTaskMap = new HashMap<>();
        for (InspectTaskPO task : subTaskList) {
            if (task.getParentId() == null || task.getId() == null) {
                continue;
            }
            subTaskMap.computeIfAbsent(task.getParentId(), key -> new ArrayList<>()).add(task.getId());
        }
        return subTaskMap;
    }

    private Map<Long, List<InspectRecordPO>> getTaskRecordMap(List<InspectTaskPO> taskList,
                                                              Map<Long, Long> checkTaskMap,
                                                              Map<Long, List<Long>> subTaskMap
                                                             ) {
        Set<Long> taskIds = new HashSet<>();
        for (InspectTaskPO task : taskList) {
            if (task.getId() == null) {
                continue;
            }
            if (Boolean.TRUE.equals(task.getIsSplit())) {
                taskIds.addAll(subTaskMap.getOrDefault(task.getId(), Collections.emptyList()));
            } else {
                taskIds.add(task.getId());
            }
            Long checkTaskId = checkTaskMap.get(task.getId());
            if (checkTaskId != null) {
                taskIds.add(checkTaskId);
            }
        }
        if (ObjectUtils.isEmpty(taskIds)) {
            return Collections.emptyMap();
        }
        List<InspectRecordPO> recordList = inspectRecordMapper.selectList(Wrappers.<InspectRecordPO>query()
                                                                                  .select("id", "task_id", "target_id")
                                                                                  .in("task_id", taskIds)
                                                                                  .eq("status", InspectRecordStatusEnum.OVER.name())
                                                                                  .orderByAsc("task_id", "result", "id"));
        if (ObjectUtils.isEmpty(recordList)) {
            return Collections.emptyMap();
        }
        Map<Long, List<InspectRecordPO>> recordMap = new HashMap<>();
        for (InspectRecordPO record : recordList) {
            if (record.getTaskId() == null) {
                continue;
            }
            recordMap.computeIfAbsent(record.getTaskId(), key -> new ArrayList<>()).add(record);
        }
        return recordMap;
    }

    private Map<Long, List<InspectRecordItemVO>> getRecordItemMap(Map<Long, List<InspectRecordPO>> recordMap) {
        if (ObjectUtils.isEmpty(recordMap)) {
            return Collections.emptyMap();
        }
        List<Long> recordIds = recordMap.values().stream()
                .flatMap(List::stream)
                .map(InspectRecordPO::getId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (ObjectUtils.isEmpty(recordIds)) {
            return Collections.emptyMap();
        }
        List<InspectRecordItemVO> recordItemList = inspectRecordItemMapper.getListByRecordIdList(recordIds, null);
        if (ObjectUtils.isEmpty(recordItemList)) {
            return Collections.emptyMap();
        }
        Map<Long, List<InspectRecordItemVO>> recordItemMap = new HashMap<>();
        for (InspectRecordItemVO recordItem : recordItemList) {
            if (recordItem.getRecordId() == null) {
                continue;
            }
            recordItemMap.computeIfAbsent(recordItem.getRecordId(), key -> new ArrayList<>()).add(recordItem);
        }
        return recordItemMap;
    }

    private String countTaskCheckRatio(InspectTaskPO task,
                                       Long checkTaskId,
                                       Map<Long, List<Long>> subTaskMap,
                                       Map<Long, List<InspectRecordPO>> recordMap,
                                       Map<Long, List<InspectRecordItemVO>> recordItemMap
                                      ) {
        List<InspectRecordPO> inspectionRecords = getInspectionRecords(task, subTaskMap, recordMap);
        List<InspectRecordPO> checkRecords = recordMap.getOrDefault(checkTaskId, Collections.emptyList());
        if (ObjectUtils.isEmpty(inspectionRecords) || ObjectUtils.isEmpty(checkRecords)) {
            return null;
        }
        return countMachRatio(inspectionRecords, checkRecords, recordItemMap);
    }

    private List<InspectRecordPO> getInspectionRecords(InspectTaskPO task,
                                                       Map<Long, List<Long>> subTaskMap,
                                                       Map<Long, List<InspectRecordPO>> recordMap
                                                      ) {
        if (task == null || task.getId() == null) {
            return Collections.emptyList();
        }
        if (!Boolean.TRUE.equals(task.getIsSplit())) {
            return recordMap.getOrDefault(task.getId(), Collections.emptyList());
        }
        List<InspectRecordPO> result = new ArrayList<>();
        for (Long subTaskId : subTaskMap.getOrDefault(task.getId(), Collections.emptyList())) {
            result.addAll(recordMap.getOrDefault(subTaskId, Collections.emptyList()));
        }
        return result;
    }

    private String countMachRatio(List<InspectRecordPO> inspectionRecords,
                                  List<InspectRecordPO> checkRecords,
                                  Map<Long, List<InspectRecordItemVO>> recordItemMap
                                 ) {
        Map<Long, InspectRecordPO> inspectionRecordMap = new HashMap<>();
        for (InspectRecordPO record : inspectionRecords) {
            if (record.getTargetId() != null) {
                inspectionRecordMap.put(record.getTargetId(), record);
            }
        }
        long totalItem = 0L;
        long notMatchItem = 0L;
        for (InspectRecordPO checkRecord : checkRecords) {
            InspectRecordPO inspectionRecord = inspectionRecordMap.get(checkRecord.getTargetId());
            if (inspectionRecord == null) {
                continue;
            }
            List<InspectRecordItemVO> checkItems = recordItemMap.getOrDefault(checkRecord.getId(), Collections.emptyList());
            List<InspectRecordItemVO> inspectionItems = recordItemMap.getOrDefault(inspectionRecord.getId(), Collections.emptyList());
            if (ObjectUtils.isEmpty(checkItems) || ObjectUtils.isEmpty(inspectionItems)) {
                continue;
            }
            Map<Long, Boolean> inspectionResultMap = new HashMap<>();
            for (InspectRecordItemVO inspectionItem : inspectionItems) {
                inspectionResultMap.put(inspectionItem.getItemId(), inspectionItem.getErrorValueFlag());
            }
            for (InspectRecordItemVO checkItem : checkItems) {
                totalItem += 1;
                if (!Objects.equals(checkItem.getErrorValueFlag(), inspectionResultMap.get(checkItem.getItemId()))) {
                    notMatchItem += 1;
                }
            }
        }
        if (totalItem == 0L) {
            return null;
        }
        return CommonUtil.calculateProportionPercentageAsStringLegacy(totalItem, totalItem - notMatchItem);
    }

    private List<Long> getMatchedTargetIds(String receiveIds, Set<Long> targetIdSet) {
        if (StringUtils.isBlank(receiveIds) || ObjectUtils.isEmpty(targetIdSet)) {
            return Collections.emptyList();
        }
        List<Long> result = new ArrayList<>();
        for (String targetId : StringUtils.splitAndIgnoreBlank(receiveIds)) {
            try {
                Long value = Long.valueOf(targetId);
                if (targetIdSet.contains(value) && !result.contains(value)) {
                    result.add(value);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return result;
    }

    private String getTaskStatus(InspectTaskPO task) {
        return task == null || task.getStatus() == null ? null : task.getStatus().name();
    }

    private void appendLikeCondition(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<InspectTaskPO> query,
                                     List<Long> ids,
                                     String column
                                    ) {
        if (ObjectUtils.isEmpty(ids)) {
            return;
        }
        for (int i = 0; i < ids.size(); i++) {
            if (i == 0) {
                query.like(column, ids.get(i));
            } else {
                query.or().like(column, ids.get(i));
            }
        }
    }

    private static class DepartmentTaskStatistics {
        private long taskTotalNum;
        private long completeTaskNum;
        private long expireTaskNum;

        public Long getTaskTotalNum() {
            return taskTotalNum;
        }

        public Long getCompleteTaskNum() {
            return completeTaskNum;
        }

        public Long getExpireTaskNum() {
            return expireTaskNum;
        }

        public void increaseTaskTotalNum() {
            this.taskTotalNum++;
        }

        public void increaseCompleteTaskNum() {
            this.completeTaskNum++;
        }

        public void increaseExpireTaskNum() {
            this.expireTaskNum++;
        }
    }

    /**
     * @Author wql
     * @Description 部门巡检统计导出
     * @Date 10:06 2023/4/25
     **/
    @Override
    public ExcelWriter departmentStatisticsExport(DepartmentInspectionParam depParam) {
        Period period = getPeriod(depParam.getPeriod(), "");
        List<DepInspectionStatisticsVO> list = inspectRecordMapper.departmentStatisticsExport(Wrappers.query()
                                                                                                      .isNotNull("a.time")
                                                                                                      .isNotNull("a.organization_id")
                                                                                                      .like(ObjectUtils.isNotEmpty(depParam.getDepName()), "a.organization_name", depParam.getDepName())
                                                                                                      .between(ObjectUtils.isNotNull(period), "a.time", period.getBegin(), period.getEnd())
                                                                                                      .groupBy("a.organization_id")
                                                                                             );
        disposeDepartmentStatistics(list, period);
        return exportExcelService.departmentStatisticsExport(list);
    }

    /**
     * @Author wql
     * @Description 计算部门/人员稽查平均匹配率
     * @Date 16:58 2023/4/19
     **/
    private String countDepCheckMatchRatio(Long depId, Long staffId) {
        List<InspectTaskPO> list = inspectTaskMapper.selectList(Wrappers.<InspectTaskPO>lambdaQuery()
                                                                        .eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.OVER)
                                                                        .like(ObjectUtils.isNotEmpty(depId), InspectTaskPO::getReceiveDeptIds, depId)
                                                                        .like(ObjectUtils.isNotEmpty(staffId), InspectTaskPO::getReceiveIds, staffId)
                                                                        .select(InspectTaskPO::getId, InspectTaskPO::getIsSplit));
        List<String> matchRatios = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            for (InspectTaskPO po : list) {
                String ratio = countTaskCheckRatio(po.getId(), po.getIsSplit());
                if (StringUtils.isNotBlank(ratio)) {
                    matchRatios.add(ratio);
                }
            }
        }
        if (matchRatios.size() > 0) {
            BigDecimal decimal = new BigDecimal("0");
            for (String ratio : matchRatios) {
                if (ObjectUtils.isNotEmpty(ratio)) {
                    decimal = decimal.add(new BigDecimal(ratio.replace("%", "")));
                }
            }
            return decimal.divide(new BigDecimal(matchRatios.size()), 2, RoundingMode.HALF_UP) + "%";
        }
        return "";
    }

    /**
     * @Author wql
     * @Description 任务明细
     * @Date 16:06 2023/4/17
     **/
    @Override
    public Pagination<InspectionResultDetailVO> taskDetail(TaskDetailParam detailParam) {
        return PagingUtils.convert(queryTaskDetail(PagingUtils.convert(detailParam), detailParam));
    }

    /**
     * @Author wql
     * @Description 任务明细导出
     * @Date 17:15 2023/4/17
     **/
    @Override
    public ExcelWriter taskDetailExport(TaskDetailParam detailParam) {
        Page<TaskDetailParam> page = PagingUtils.convert(detailParam);
        page.setPages(0L);
        return exportExcelService.taskDetailExport(queryTaskDetail(page, detailParam).getRecords());
    }

    /**
     * @Author wql
     * @Description 人员巡检统计
     * @Date 18:52 2023/4/17
     **/
    @Override
    public Pagination<StaffInspectionStatisticsVO> staffStatistics(DepartmentInspectionParam depParam) {
        Period period = getPeriod(depParam.getPeriod(), "");
        Page<StaffInspectionStatisticsVO> list = inspectRecordMapper.staffStatistics(PagingUtils.convert(depParam),
                                                                                     Wrappers.query()
                                                                                             .eq(StringUtils.isNotBlank(depParam.getDepName()), "a.organization_name", depParam.getDepName())
                                                                                             .isNotNull("a.username")
                                                                                             .isNotNull("a.time")
                                                                                             .between(ObjectUtils.isNotNull(period), "a.time", period.getBegin(), period.getEnd())
                                                                                             .and(StringUtils.isNotBlank(depParam.getName()), q -> {
                                                                                                 q
                                                                                                         .like("a.organization_name", depParam.getName())
                                                                                                         .or()
                                                                                                         .like("a.nickname", depParam.getName());
                                                                                             })
                                                                                             .groupBy("a.username")
                                                                                    );
        //处理其他统计信息
        disposeStaffStatistics(list.getRecords(), period);
        return PagingUtils.convert(list);
    }

    /**
     * @Author wql
     * @Description 处理其他统计信息
     * @Date 10:18 2023/4/25
     **/
    private void disposeStaffStatistics(List<StaffInspectionStatisticsVO> list, Period period) {
        List<Long> depIds = getStaffDepartmentIds(list);
        List<Long> staffIds = getStaffIds(list);
        Map<Long, DepartmentTaskStatistics> departmentTaskStatisticsMap = countDepartmentTaskStatistics(depIds, period);
        Map<Long, DepartmentTaskStatistics> staffTaskStatisticsMap = countStaffTaskStatistics(staffIds, period);
        Map<Long, String> staffCheckRatioMap = countStaffCheckMatchRatio(staffIds);
        Map<Long, Long> departmentInspectionNumMap = countDepartmentInspectionNumStatistics(depIds, period);
        Map<Long, StaffHiddenDangerStatisticsVO> staffHiddenDangerStatisticsMap = countStaffHiddenDangerStatistics(staffIds, period);
        for (StaffInspectionStatisticsVO vo : Optional.ofNullable(list).orElse(Collections.emptyList())) {
            DepartmentTaskStatistics departmentTaskStatistics = departmentTaskStatisticsMap.getOrDefault(vo.getDepId(), new DepartmentTaskStatistics());
            DepartmentTaskStatistics staffTaskStatistics = staffTaskStatisticsMap.getOrDefault(vo.getStaffId(), new DepartmentTaskStatistics());
            vo.setDepTaskNum(departmentTaskStatistics.getTaskTotalNum());
            vo.setPersonageTaskNum(staffTaskStatistics.getTaskTotalNum());
            vo.setCompleteTaskNum(staffTaskStatistics.getCompleteTaskNum());
            vo.setExpireTaskNum(staffTaskStatistics.getExpireTaskNum());
            vo.setTaskProportion(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(vo.getPersonageTaskNum(), vo.getDepTaskNum())));
            vo.setTaskCompleteRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(vo.getCompleteTaskNum(), vo.getPersonageTaskNum())));
            vo.setCheckMatchingRatio(staffCheckRatioMap.getOrDefault(vo.getStaffId(), ""));
            vo.setDepInspectionNum(departmentInspectionNumMap.getOrDefault(vo.getDepId(), 0L));
            vo.setPersonageNumRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(vo.getDepInspectionNum(), vo.getPersonageInspectionNum())));
            vo.setInspectionElapsedTimeStr(countTimeStr(vo.getInspectionElapsedTime()));
            StaffHiddenDangerStatisticsVO staffHiddenDangerStatistics = staffHiddenDangerStatisticsMap.getOrDefault(vo.getStaffId(), new StaffHiddenDangerStatisticsVO());
            Integer total = staffHiddenDangerStatistics.getAbnormalHiddenDangerNum();
            if (total == null) {
                total = 0;
            }
            vo.setAbnormalHiddenDangerNum(total);
            Integer complete = staffHiddenDangerStatistics.getHaveDoneHiddenDangerNum();
            if (complete == null) {
                complete = 0;
            }
            vo.setHaveDoneHiddenDangerNum(complete);
            vo.setHiddenDangerDisposeRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(complete, total)));
        }
    }

    /**
     * @Author wql
     * @Description 人员巡检统计导出
     * @Date 10:16 2023/4/25
     **/
    public ExcelWriter staffStatisticsExport(DepartmentInspectionParam depParam) {
        Period period = getPeriod(depParam.getPeriod(), "");
        List<StaffInspectionStatisticsVO> list = inspectRecordMapper.staffStatisticsExport(Wrappers.query()
                                                                                                   .isNotNull("a.time")
                                                                                                   .eq(StringUtils.isNotBlank(depParam.getDepName()), "a.organization_name", depParam.getDepName())
                                                                                                   .between(ObjectUtils.isNotNull(period), "a.time", period.getBegin(), period.getEnd())
                                                                                                   .and(StringUtils.isNotBlank(depParam.getName()), q -> {
                                                                                                       q
                                                                                                               .like("a.organization_name", depParam.getName())
                                                                                                               .or()
                                                                                                               .like("a.nickname", depParam.getName());
                                                                                                   })
                                                                                                   .groupBy("a.username")
                                                                                          );
        //处理其他统计信息
        disposeStaffStatistics(list, period);
        return exportExcelService.staffStatisticsExport(list);
    }

    /**
     * @Author wql
     * @Description 巡检记录明细
     * @Date 20:06 2023/4/17
     **/
    @Override
    public Pagination<DepInspectionDetailVO> inspectionDetail(InspectionNumParam param) {
        return PagingUtils.convert(queryInspectionDetail(PagingUtils.convert(param), param));
    }

    public Page<DepInspectionDetailVO> queryInspectionDetail(Page<InspectionNumParam> page, InspectionNumParam param) {
        Period period = getPeriod(param.getPeriod(), "");
        Page<DepInspectionDetailVO> list = inspectRecordMapper.inspectionDetail(page, Wrappers.query()
                                                                                        .eq(ObjectUtils.isNotEmpty(param.getDepId()), "a.organization_id", param.getDepId())
                                                                                        .eq(ObjectUtils.isNotEmpty(param.getStaffId()), "a.user_id", param.getStaffId())
                                                                                        .eq(ObjectUtils.isNotEmpty(param.getObjId()), "a.target_id", param.getObjId())
                                                                                        .between(period !=
                                                                                                 null, "a.create_time", ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                               );
        Optional.ofNullable(list.getRecords()).orElse(Collections.emptyList()).forEach(vo -> {
            vo.setState(TaskStatusEnum.getDescription(vo.getState()));
            vo.setResult(InspectRecordResultEnum.getDescription(vo.getResult()));
            InspectTaskPO taskPO = inspectTaskMapper.selectOne(Wrappers.<InspectTaskPO>lambdaQuery()
                                                                       .and(q -> q.eq(InspectTaskPO::getCheckTaskId, vo.getTaskId()).or()
                                                                               .eq(ObjectUtils.isNotEmpty(vo.getParentId()) &&
                                                                                   vo.getParentId() !=
                                                                                   0L, InspectTaskPO::getCheckTaskId, vo.getParentId())));
            if (ObjectUtils.isNotEmpty(taskPO)) {
                InspectRecordPO record = inspectRecordMapper.selectOne(Wrappers.<InspectRecordPO>lambdaQuery()
                                                                               .eq(InspectRecordPO::getTargetId, vo.getObjId())
                                                                               .eq(InspectRecordPO::getTaskId, taskPO.getId())
                                                                      );
                if (ObjectUtils.isNotEmpty(record)) {
                    vo.setCheckStaff(record.getNickname());
                }
            }
        });
        return list;
    }

    /**
     * @Author wql
     * @Description 巡检记录明细导出
     * @Date 20:26 2023/4/17
     **/
    @Override
    public ExcelWriter inspectionDetailExport(InspectionNumParam param) {
        Page<InspectionNumParam> page = PagingUtils.convert(param);
        page.setPages(0L);
        Page<DepInspectionDetailVO> list = queryInspectionDetail(page, param);
        List<DepInspectionDetailVO> voList = Optional.ofNullable(list.getRecords()).orElse(Collections.emptyList());
        return exportExcelService.inspectionDetailExport(voList);
    }

    /**
     * @Author wql
     * @Description 巡检对象统计
     * @Date 9:04 2023/4/18
     **/
    @Override
    public Pagination<InspectionObjStatisticsVO> objStatistics(InspectionObjQueryParam param) {
        Period period = getPeriod(param.getPeriod(), "");
        Page<InspectionObjStatisticsVO> list = inspectRecordMapper.objStatistics(PagingUtils.convert(param), Wrappers.query()
                                                                                         .like(ObjectUtils.isNotEmpty(param.getArea()), "a.area_id", param.getArea())
                                                                                         .eq(ObjectUtils.isNotEmpty(param.getObjType()), "a.target_category", param.getObjType())
                                                                                         .like(ObjectUtils.isNotEmpty(param.getObjName()), "a.target_name", param.getObjName())
                                                                                         .isNotNull("a.time")
                                                                                         .between(period != null, "a.time", ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                                         .groupBy("a.target_id")
                                                                                );
        //处理巡检对象其他统计字段
        disposeObjStatistics(list.getRecords(), period);
        return PagingUtils.convert(list);
    }

    /**
     * @Author wql
     * @Description 处理巡检对象其他统计字段
     * @Date 10:26 2023/4/25
     **/
    private void disposeObjStatistics(List<InspectionObjStatisticsVO> list, Period period) {
        Optional.ofNullable(list).orElse(Collections.emptyList()).parallelStream().forEach(vo -> {
            TaskDetailParam detailParam = TaskDetailParam.builder().period(period).objId(vo.getObjId()).build();
            vo.setObjType(InspectTargetCategoryEnum.getDescription(vo.getObjType()));
            vo.setTaskNum(countTaskNum(detailParam));
            Integer total = countHiddenDangerNum(detailParam);
            vo.setAbnormalHiddenDangerNum(total);
            detailParam.setState("COMPLETE");
            Integer complete = countHiddenDangerNum(detailParam);
            vo.setProcessedHiddenDangerNum(complete);
            detailParam.setState("UNTREATED");
            Integer untreated = countHiddenDangerNum(detailParam);
            vo.setUntreatedHiddenDangerNum(untreated);
            vo.setHiddenDangerDisposeRatio(MathUtils.simplePercentageFormat(MathUtils.calculateProportionPercentage(complete, total)));
            List<TroubleListDetailVO> details = getAllObjRepairsDetail(TaskDetailParam.builder().period(period).objId(vo.getObjId())
                                                                               .build());
            vo.setRepairsNum(details.size());
            vo.setInspectionAveTime(countObjInspectionAveTime(vo.getObjId(), period));
        });
    }

    /**
     * @Author wql
     * @Description 巡检对象统计导出
     * @Date 10:24 2023/4/25
     **/
    public ExcelWriter objStatisticsExport(InspectionObjQueryParam param) {
        Period period = getPeriod(param.getPeriod(), "");
        List<InspectionObjStatisticsVO> list = inspectRecordMapper.objStatisticsExport(Wrappers.query()
                                                                                               .like(ObjectUtils.isNotEmpty(param.getArea()), "a.area_id", param.getArea())
                                                                                               .eq(ObjectUtils.isNotEmpty(param.getObjType()), "a.target_category", param.getObjType())
                                                                                               .like(ObjectUtils.isNotEmpty(param.getObjName()), "a.target_name", param.getObjName())
                                                                                               .isNotNull("a.time")
                                                                                               .between(period !=
                                                                                                        null, "a.time", ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                                               .groupBy("a.target_id")
                                                                                      );
        //处理巡检对象其他统计字段
        disposeObjStatistics(list, period);
        return exportExcelService.objStatisticsExport(list);
    }

    /**
     * @Author wql
     * @Description 稽查不匹配项
     * @Date 14:48 2023/4/25
     **/
    @Override
    public ExcelWriter checkDetailExport(ResultRunInHoleParam resultParam) {
        List<String> unMatchItem = (List<String>) redisTemplate.opsForHash()
                .get(CHECK_TASK_RESULT + resultParam.getTaskId(), UN_MATCH_ITEM);
        List<CheckTheMismatchDetailVO> list = new ArrayList<>();
        getCheckDetailList(list, unMatchItem);
        return exportExcelService.checkDetailExport(list);
    }

    /**
     * @Author wql
     * @Description 设备巡检信息统计
     * @Date 10:55 2023/8/9
     **/
    @Override
    public DeviceInspectionVO deviceInspectionInfo(Long deviceId) {
        List<InspectRecordPO> list = inspectRecordMapper.selectList(new LambdaQueryWrapper<InspectRecordPO>()
                                                                            .eq(InspectRecordPO::getTargetId, deviceId)
                                                                            .eq(InspectRecordPO::getTargetCategory, InspectTargetCategoryEnum.DEVICE.name())
                                                                            .isNotNull(InspectRecordPO::getTaskId)
                                                                            .select(InspectRecordPO::getTaskId));
        //任务总数
        List<Long> taskIds = list.parallelStream().map(InspectRecordPO::getTaskId).distinct().collect(Collectors.toList());
        if (ObjectUtils.isEmpty(taskIds)) {
            return DeviceInspectionVO.builder().totalTask(0).abnormalTask(0).normalTask(0).build();
        }
        int totalNum = taskIds.size();
        //异常任务数
        Integer abnormalNum = inspectTaskMapper.countAbnormalTaskNum(Wrappers.query().in("a.id", taskIds)
                                                                             .eq("b.result", InspectRecordResultEnum.ABNORMAL.name()));
        return DeviceInspectionVO.builder().totalTask(totalNum).abnormalTask(abnormalNum).normalTask(totalNum - abnormalNum).build();
    }

    @Override
    public Pagination<TaskStandardAbnormalRateVO> taskStandardAbnormalRate(TaskStandardAbnormalRateParam param) {
        return PagingUtils.convert(inspectRecordMapper.taskStandardAbnormalRate(PagingUtils.convert(param), param));
    }

    @Override
    public List<TaskStandardAbnormalRateVO> taskStandardAbnormalRateExport(TaskStandardAbnormalRateParam param) {
        return inspectRecordMapper.taskStandardAbnormalRateExport(param);
    }

    /**
     * @Author wql
     * @Description 统计对象平均巡检耗时
     * @Date 20:45 2023/4/19
     **/
    private String countObjInspectionAveTime(Long objId, Period period) {
        List<Integer> timeList = inspectRecordMapper.objInspectionTime(Wrappers.query()
                                                                               .isNotNull("a.time")
                                                                               .isNotNull("b.begin_time")
                                                                               .eq("a.`status`", InspectRecordStatusEnum.OVER)
                                                                               .eq(ObjectUtils.isNotEmpty(objId), "a.target_id", objId)
                                                                               .between(ObjectUtils.isNotNull(period), "a.create_time", period.getBegin(), period.getEnd()));
        int sum = 0;
        if (ObjectUtils.isNotEmpty(timeList)) {
            for (Integer i : timeList) {
                sum += (i == null ? 0 : i);
            }
        }
        return countTimeStr(ObjectUtils.isNotEmpty(timeList) ? (sum / timeList.size()) : 0L);
    }

    /**
     * @Author wql
     * @Description 查询对象的报修次数明细
     * @Date 20:28 2023/4/19
     **/
    private List<TroubleListDetailVO> getAllObjRepairsDetail(TaskDetailParam param) {
        return faultOrderMapper.getRepairsDetail(param);
    }

    /**
     * @Author wql
     * @Description 巡检对象分析-报修明细
     * @Date 21:07 2023/4/19
     **/
    @Override
    public Pagination<TroubleListDetailVO> objStatisticsRepairsDetail(TaskDetailParam param) {
        return PagingUtils.convert(faultOrderMapper.getPageRepairsDetail(PagingUtils.convert(param), param));
    }

    /**
     * @Author wql
     * @Description 巡检对象分析-报修明细导出
     * @Date 21:11 2023/4/19
     **/
    @Override
    public ExcelWriter objStatisticsRepairsDetailExport(TaskDetailParam queryParam) {
        List<TroubleListDetailVO> list = Optional.ofNullable(getAllObjRepairsDetail(queryParam)).orElse(Collections.emptyList());
        list.parallelStream().forEach(vo -> {
            vo.setSource(ResourceEnum.getDescription(vo.getSource()));
            vo.setState(FaultStatusEnum.getDescription(vo.getState()));
            vo.setResult(FaultResultEnum.getDescription(vo.getResult()));
        });
        return exportExcelService.repairsDetailExport(list);
    }

    /**
     * @Author wql
     * @Description 统计巡检次数
     * @Date 19:34 2023/4/17
     **/
    public Long countInspectionNum(InspectionNumParam param) {
        Period period = param.getPeriod();
        return inspectRecordMapper.countInspectionNum(Wrappers.query()
                                                              .eq("a.status", "OVER")
                                                              .eq(ObjectUtils.isNotEmpty(param.getDepId()), "a.organization_id", param.getDepId())
                                                              .eq(ObjectUtils.isNotEmpty(param.getStaffId()), "a.user_id", param.getStaffId())
                                                              .eq(ObjectUtils.isNotEmpty(param.getObjId()), "a.target_id", param.getObjId())
                                                              .between(ObjectUtils.isNotNull(period), "a.time", period.getBegin(), period.getEnd())
                                                     );
    }

    /**
     * @Author wql
     * @Description 查询任务明细
     * @Date 16:08 2023/4/18
     **/
    private Page<InspectionResultDetailVO> queryTaskDetail(Page<TaskDetailParam> page, TaskDetailParam param) {
        Period period = getPeriod(param.getPeriod(), "");
        Page<InspectionResultDetailVO> list = inspectTaskMapper.getTaskDetail(page, Wrappers.query()
                                                                                      .eq("a.is_split", 0)
                                                                                      .like(ObjectUtils.isNotEmpty(param.getStaffId()), "a.receive_ids", param.getStaffId())
                                                                                      .eq(ObjectUtils.isNotEmpty(param.getState()), "a.`status`", param.getState())
                                                                                      .like(ObjectUtils.isNotEmpty(param.getObjId()), "a.target_ids", param.getObjId())
                                                                                      .like(ObjectUtils.isNotEmpty(param.getDepId()), "a.receive_dept_ids", param.getDepId())
                                                                                      .between(period !=
                                                                                               null, "a.create_time", ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                             );
        Optional.ofNullable(list.getRecords()).orElse(Collections.emptyList()).parallelStream().forEach(vo -> {
            vo.setState(TaskStatusEnum.getDescription(vo.getState()));
            vo.setTaskType(0 == vo.getCheckTaskId() ? "巡查" : "稽查");
            vo.setDutyOfficer(getTaskUserName(vo.getReceiveType(), vo.getReceiveDeptIds(), vo.getReceivePostIds(),
                                              vo.getReceiveRoleIds(), vo.getReceiveIds(), vo.getTenant()));
            vo.setTaskTimeLimitStr(countTimeStr(vo.getTaskTimeLimit()));
            //统计巡检任务已完成巡检的设备数量
            Long completeNum = inspectRecordMapper.countCompleteObjNum(TaskStatusEnum.OVER.name(), vo.getTaskId());
            vo.setProgress(CommonUtil.calculateProportionPercentageAsStringLegacy(vo.getObjNum(), completeNum));
        });
        return list;
    }

    /**
     * @Author wql
     * @Description 获取用户名称
     * @Date 13:54 2023/4/24
     **/
    private String getUserNameByIds(String receiveIds) {
        if (StringUtils.isNotBlank(receiveIds)) {
            List<Long> collect = Lists.newArrayList(receiveIds.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
            List<UserDTO> users = Optional.ofNullable(userClient.listUserByIds(collect)).orElse(Collections.emptyList());
            return users.stream().map(UserDTO::getName).collect(Collectors.joining(","));
        }
        return "";
    }

    /**
     * @Author wql
     * @Description 按条件统计隐患单总数
     * @Date 15:44 2023/4/17
     **/
    private Integer countHiddenDangerNum(TaskDetailParam taskDetailParam) {
        return hiddenDangerMapper.countHiddenDangerNum(taskDetailParam);
    }

    /**
     * @Author wql
     * @Description 按条件统计任务总数
     * @Date 15:10 2023/4/17
     **/
    public Long countTaskNum(TaskDetailParam param) {
        Period period = param.getPeriod();
        return inspectTaskMapper.countByCondition(Wrappers.query()
                                                          .eq("a.is_split", 0)
                                                          .eq(ObjectUtils.isNotEmpty(param.getParentId()), "a.parent_id", param.getParentId())
                                                          .eq(ObjectUtils.isNotEmpty(param.getState()), "a.`status`", param.getState())
                                                          .eq(StringUtils.isNotBlank(param.getResult()), "a.result", param.getResult())
                                                          .like(ObjectUtils.isNotEmpty(param.getStaffId()), "a.receive_ids", param.getStaffId())
                                                          .like(ObjectUtils.isNotEmpty(param.getObjId()), "a.target_ids", param.getObjId())
                                                          .like(ObjectUtils.isNotEmpty(param.getDepId()), "a.receive_dept_ids", param.getDepId())
                                                          .between(ObjectUtils.isNotNull(period), "a.create_time", period.getBegin(), period.getEnd())
                                                 );
    }

    /**
     * 权限限制
     *
     * @param param
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "inspect:inspectTask")
    public Long countTaskNumPermission(TaskDetailParam param) {
        return this.countTaskNum(param);
    }

    public Long countTaskNum() {
        return inspectTaskMapper.countByCondition(Wrappers.query()
                                                          .eq("a.is_split", 0));
    }

    @DataPermissionSwitch(dataModel = "inspect:inspectTask")
    public Integer countAbnormalTaskNumPermission(BaseDateStatisticsParam param) {
        //异常任务数
        return inspectTaskMapper.countAbnormalTaskNum(Wrappers.query().
                                                              eq("b.result", InspectRecordResultEnum.ABNORMAL.name())
                                                              .between(ObjectUtils.isNotNull(param.getPeriod()), "a.create_time",
                                                                       ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd)));
    }
}
