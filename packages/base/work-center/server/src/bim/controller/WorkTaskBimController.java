package com.guhecloud.cube.biz.work.center.server.bim.controller;

import com.guhecloud.cube.biz.work.center.server.entity.WorkTag;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTagReferBusinessType;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagQueryParam;
import com.guhecloud.cube.biz.work.center.server.service.WorkTagService;
import com.guhecloud.cube.biz.work.center.server.service.WorkTaskStatisticsService;
import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 工作三维统计
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkTaskBimController {

    private final WorkTaskStatisticsService workTaskStatisticsService;

    private final WorkTagService workTagService;

    /**
     * 工作数量统计
     *
     * @return
     */
    @GetMapping("/work-task-bim-statistics")
    public Long workNumTotal() {
        LocalDateTime dateTime = DateUtils.endOfDay(DateUtils.now());
        LocalDateTime startTime = DateUtils.beginOfDay(DateUtils.offset(dateTime, ChronoUnit.DAYS, -6));
        Period period = new Period(startTime, dateTime);
        return this.workTaskStatisticsService.allWorkNumTotal(period);
    }

    /**
     * 类型统计工作数量
     *
     * @return
     */
    @GetMapping("/work-task-bim-statistics/type")
    public List<WorkBusinessTypeStatisticsVO> workBusinessTypeWorkStatistics() {
        LocalDateTime dateTime = DateUtils.endOfDay(DateUtils.now());
        LocalDateTime startTime = DateUtils.beginOfDay(DateUtils.offset(dateTime, ChronoUnit.DAYS, -6));
        Period period = new Period(startTime, dateTime);
        return this.workTaskStatisticsService.allWorkBusinessTypeWorkStatistics(period);
    }

    /**
     * 标签统计工作数量
     *
     * @return
     */
    @GetMapping("/work-task-bim-statistics/tag")
    public List<WorkBusinessTypeStatisticsVO> workTagWorkStatistics() {
        LocalDateTime dateTime = DateUtils.endOfDay(DateUtils.now());
        LocalDateTime startTime = DateUtils.beginOfDay(DateUtils.offset(dateTime, ChronoUnit.DAYS, -7));
        Period period = new Period(startTime, dateTime);
        List<WorkBusinessTypeStatisticsVO> workBusinessTypeStatisticsVOS = this.workTaskStatisticsService.allWorkBusinessTypeWorkStatistics(period);
        return getWorkBusinessTypeStatisticsVOS(workBusinessTypeStatisticsVOS);
    }

    private List<WorkBusinessTypeStatisticsVO> getWorkBusinessTypeStatisticsVOS(
            List<WorkBusinessTypeStatisticsVO> workBusinessTypeStatisticsVOS
                                                                               ) {
        //获取待办标签
        List<WorkTag> workTags = this.workTagService.listWorkTags(new WorkTagQueryParam());
        if (CollectionUtils.isEmpty(workTags)) {
            //不存在标签时,不聚合直接返回
            return workBusinessTypeStatisticsVOS;
        }
        List<WorkBusinessTypeStatisticsVO> workTagBusinessTypeStatisticsVO = new ArrayList<>();
        Set<String> workTagCodes = workTags.stream().map(WorkTag::getCode).collect(Collectors.toSet());
        //获取待办标签关联的业务类型
        Map<String, List<WorkTagReferBusinessType>> workTaskReferBusinessTypeMaps = this.workTagService
                .listWorkTagReferBusinessTypesByCodes(workTagCodes).stream()
                .collect(Collectors.groupingBy(WorkTagReferBusinessType::getWorkTagCode));
        //存在标签的业务类型
        Set<String> tagHasBusinessTypes = new LinkedHashSet<>();
        workTags.forEach(workTag -> {
            List<WorkTagReferBusinessType> workTagReferBusinessTypes = workTaskReferBusinessTypeMaps.get(workTag.getCode());
            long workTagReferBusinessCount = 0L;
            if (!CollectionUtils.isEmpty(workTagReferBusinessTypes)) {
                Set<String> businessTypes = workTagReferBusinessTypes.stream().map(WorkTagReferBusinessType::getBusinessType)
                        .collect(Collectors.toSet());
                workTagReferBusinessCount = workBusinessTypeStatisticsVOS.stream()
                        .filter(workBusinessTypeStatisticsVO -> businessTypes.contains(workBusinessTypeStatisticsVO.getBusinessType()))
                        .mapToLong(WorkBusinessTypeStatisticsVO::getTotal).sum();
                tagHasBusinessTypes.addAll(businessTypes);
            }
            workTagBusinessTypeStatisticsVO.add(WorkBusinessTypeStatisticsVO.builder()
                                                        .businessType(workTag.getCode())
                                                        .businessTypeName(workTag.getName())
                                                        .total(workTagReferBusinessCount)
                                                        .build());
        });
        //不包含在标签里的业务类型
        workTagBusinessTypeStatisticsVO.addAll(workBusinessTypeStatisticsVOS.stream()
                                                       .filter(workBusinessTypeStatisticsVO -> !tagHasBusinessTypes.contains(workBusinessTypeStatisticsVO.getBusinessType()))
                                                       .toList());
        return workTagBusinessTypeStatisticsVO;
    }
}
