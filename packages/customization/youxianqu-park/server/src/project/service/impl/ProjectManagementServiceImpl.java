package com.guhecloud.cube.customized.youxianqu.park.management.server.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.youxianqu.park.management.server.project.mapper.ProjectManagementMapper;
import com.guhecloud.cube.customized.youxianqu.park.management.server.project.param.ProjectManagementQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.project.po.ProjectManagementPO;
import com.guhecloud.cube.customized.youxianqu.park.management.server.project.result.ProjectManagementResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.project.service.ProjectManagementService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 项目管理实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectManagementServiceImpl extends ServiceImpl<ProjectManagementMapper, ProjectManagementPO> implements ProjectManagementService {
    private final ProjectManagementMapper projectManagementMapper;

    /**
     * 分页列表
     *
     * @param param 条件
     * @return 分页
     */
    @Override
    public Pagination<ProjectManagementResult> getPage(ProjectManagementQueryParam param) {
        Period period = param.getPeriod();
        boolean periodBool = period != null && period.getBegin() != null && period.getEnd() != null;
        Page<ProjectManagementPO> page = projectManagementMapper.selectPage(PagingUtils.convert(param), Wrappers.<ProjectManagementPO>lambdaQuery()
                .eq(param.getFoundStatus() != null, ProjectManagementPO::getFoundStatus, param.getFoundStatus())
                .and(
                        periodBool, i -> i.ge(ProjectManagementPO::getStartDate, ObjectUtils.ifNull(period, Period::getBegin))
                                .le(ProjectManagementPO::getEndDate, ObjectUtils.ifNull(period, Period::getBegin))
                ).orderByDesc(ProjectManagementPO::getApplyDate)
        );

        return PagingUtils.convert(page, ProjectManagementResult.class);
    }
}
