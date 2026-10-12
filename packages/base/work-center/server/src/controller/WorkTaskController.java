package com.guhecloud.cube.biz.work.center.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.work.center.server.entity.WorkBusinessUrl;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTask;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskQueryParam;
import com.guhecloud.cube.biz.work.center.server.service.WorkBusinessUrlService;
import com.guhecloud.cube.biz.work.center.server.service.WorkTaskService;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskPageVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 工作任务管理接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkTaskController {

    private final WorkTaskService workTaskService;

    private final WorkBusinessUrlService workBusinessUrlService;

    /**
     * 工作分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/work-tasks")
    public Pagination<WorkTaskPageVO> workPage(WorkTaskQueryParam param) {
        Page<WorkTask> page = this.workTaskService.workPage(param);
        List<WorkTaskPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), WorkTaskPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return Pagination.empty();
        }
        this.extend(pageVOS);
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 已办分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/work-tasks-done")
    public Pagination<WorkTaskPageVO> donePage(WorkTaskQueryParam param) {
        Page<WorkTask> page = this.workTaskService.donePage(param);
        List<WorkTaskPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), WorkTaskPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return Pagination.empty();
        }
        this.extend(pageVOS);
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 根据业务主键批量删除待办任务
     *
     * @param businessIds 业务主键集合
     */
    @DeleteMapping("/work-task/business/batch")
    public void removeBatchByBusinessIds(@RequestBody Set<String> businessIds) {
        this.workTaskService.removeBatchByBusinessIds(businessIds);
    }

    private void extend(List<WorkTaskPageVO> pageVOS) {
        Set<String> businessTypes = pageVOS.stream().map(WorkTaskPageVO::getBusinessType).collect(Collectors.toSet());
        Map<String, WorkBusinessUrl> workBusinessUrlMap = this.workBusinessUrlService
                .listByBusinessTypes(businessTypes, SessionContextHolder.getRequester().getTerminal()).stream()
                .collect(Collectors.toMap(WorkBusinessUrl::getBusinessType, workBusinessUrl -> workBusinessUrl, (o, n) -> n));
        pageVOS.forEach(workTaskPageVO -> {
            WorkBusinessUrl workBusinessUrl = workBusinessUrlMap.get(workTaskPageVO.getBusinessType());
            if (Objects.nonNull(workBusinessUrl)) {
                workTaskPageVO.setUrl(workBusinessUrl.getUrl());
            }
        });
    }
}
