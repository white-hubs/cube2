package com.guhecloud.cube.biz.work.center.server.controller;

import com.guhecloud.cube.biz.work.center.server.entity.WorkTag;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTagReferBusinessType;
import com.guhecloud.cube.biz.work.center.server.service.WorkTagService;
import com.guhecloud.cube.biz.work.center.server.service.WorkTaskStatisticsService;
import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeStatisticsVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTagVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 工作任务标签统计管理接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkTaskTagStatisticsController {

    private final WorkTaskStatisticsService workTaskStatisticsService;

    private final WorkTagService workTagService;

    /**
     * 工作标签统计
     *
     * @return
     */
    @GetMapping("/work-task-tag-statistics")
    public List<WorkBusinessTypeStatisticsVO> workTagBusinessTypeWorkStatistics() {
        //待办业务类型分组统计结果
        List<WorkBusinessTypeStatisticsVO> workBusinessTypeStatisticsVOS = this.workTaskStatisticsService.workBusinessTypeWorkStatistics();
        Set<String> businessTypes = workBusinessTypeStatisticsVOS.stream().map(WorkBusinessTypeStatisticsVO::getBusinessType)
                .collect(Collectors.toSet());
        //获取待办标签
        List<WorkTagVO> workTags = this.getWorkTagVOS(businessTypes);
        if (CollectionUtils.isEmpty(workTags)) {
            //不存在标签时,直接返回
            return Collections.emptyList();
        }
        List<WorkBusinessTypeStatisticsVO> workTagBusinessTypeStatisticsVO = new ArrayList<>(workTags.size());
        Set<String> workTagCodes = workTags.stream().map(WorkTagVO::getCode).collect(Collectors.toSet());
        //获取待办标签关联的业务类型
        Map<String, List<WorkTagReferBusinessType>> workTaskReferBusinessTypeMaps = this.workTagService
                .listWorkTagReferBusinessTypesByCodes(workTagCodes).stream()
                .collect(Collectors.groupingBy(WorkTagReferBusinessType::getWorkTagCode));
        workTags.forEach(workTag -> {
            List<WorkTagReferBusinessType> workTagReferBusinessTypes = workTaskReferBusinessTypeMaps.get(workTag.getCode());
            long workTagReferBusinessCount = 0L;
            if (!CollectionUtils.isEmpty(workTagReferBusinessTypes)) {
                Set<String> tagBusinessTypes = workTagReferBusinessTypes.stream().map(WorkTagReferBusinessType::getBusinessType)
                        .collect(Collectors.toSet());
                workTagReferBusinessCount = workBusinessTypeStatisticsVOS.stream()
                        .filter(workBusinessTypeStatisticsVO -> tagBusinessTypes.contains(workBusinessTypeStatisticsVO.getBusinessType()))
                        .mapToLong(WorkBusinessTypeStatisticsVO::getTotal).sum();
            }
            workTagBusinessTypeStatisticsVO.add(WorkBusinessTypeStatisticsVO.builder()
                                                        .businessType(workTag.getCode())
                                                        .businessTypeName(workTag.getName())
                                                        .total(workTagReferBusinessCount)
                                                        .build());
        });
        return workTagBusinessTypeStatisticsVO;
    }

    /**
     * 待办标签列表
     *
     * @return
     */
    @GetMapping("/work-task-tags")
    @DataPermissionSwitch(dataModel = "work:workPerson")
    public List<WorkTagVO> workTags() {
        //待办业务类型列表
        List<WorkBusinessTypeVO> workBusinessTypeVOS = this.workTaskStatisticsService.workBusinessTypes();
        Set<String> businessTypes = workBusinessTypeVOS.stream().map(WorkBusinessTypeVO::getBusinessType).collect(Collectors.toSet());
        return getWorkTagVOS(businessTypes);
    }

    /**
     * 已办标签列表
     *
     * @return
     */
    @GetMapping("/work-done-task-tags")
    public List<WorkTagVO> workDoneTags() {
        //已办业务类型列表
        List<WorkBusinessTypeVO> workBusinessTypeVOS = this.workTaskStatisticsService.workDoneBusinessTypes();
        Set<String> businessTypes = workBusinessTypeVOS.stream().map(WorkBusinessTypeVO::getBusinessType).collect(Collectors.toSet());
        return getWorkTagVOS(businessTypes);
    }

    private List<WorkTagVO> getWorkTagVOS(Set<String> businessType) {
        if (CollectionUtils.isEmpty(businessType)) {
            return Collections.emptyList();
        }
        //获取业务类型关联的标签
        Set<String> workTagCodes = this.workTagService.listWorkTagReferBusinessTypes(businessType).stream()
                .map(WorkTagReferBusinessType::getWorkTagCode).collect(Collectors.toSet());
        //获取自定义标签
        List<WorkTag> workTags = this.workTagService.listWorkTagsByCodes(workTagCodes);
        return ObjectUtils.copying(workTags, WorkTagVO.class);
    }

}
