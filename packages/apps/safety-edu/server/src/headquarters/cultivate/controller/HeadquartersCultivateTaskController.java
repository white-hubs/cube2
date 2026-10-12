package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.controller;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateTaskDetailVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateTaskVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 总部培训任务接口管理
 *
 * @author lanchun
 * @date 2024/4/10 15:06
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersCultivateTaskController {
    private final HeadquartersCultivateTaskService headquartersCultivateTaskService;

    /**
     * 功能描述： 培训任务添加
     */
    @PostMapping(value = "/headquarters/cultivate-task")
    public Long add(@Valid @RequestBody HeadquartersCultivateTaskParam param) {
        return headquartersCultivateTaskService.add(param);
    }

    /**
     * 功能描述： 培训任务修改
     */
    @PutMapping(value = "/headquarters/cultivate-task/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody HeadquartersCultivateTaskParam param) {
        headquartersCultivateTaskService.update(id, param);
    }

    /**
     * 功能描述： 培训任务删除
     */
    @DeleteMapping(value = "/headquarters/cultivate-task/{id}")
    public void delete(@PathVariable Long id) {
        headquartersCultivateTaskService.delete(id);
    }

    /**
     * 功能描述： 培训任务批量删除
     */
    @DeleteMapping(value = "/headquarters/cultivate-task/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        headquartersCultivateTaskService.batchDelete(ids);
    }

    /**
     * 功能描述： 培训任务分页
     */
    @GetMapping(value = "/headquarters/cultivate-task/page")
    public Pagination<HeadquartersCultivateTaskVO> getPage(HeadquartersCultivateTaskQueryParam param) {
        //处理时间
        if (param.getPeriod() != null && param.getPeriod().getEnd() == null) {
            Period period = Period.of(param.getPeriod().getBegin(), DateUtils.endOfDay(param.getPeriod().getBegin()));
            param.setPeriod(period);
        }
        return headquartersCultivateTaskService.getPage(param);
    }

    /**
     * 功能描述： 培训任务详情
     */
    @GetMapping(value = "/headquarters/cultivate-task/{id}")
    public HeadquartersCultivateTaskDetailVO getById(@PathVariable Long id) {
        return headquartersCultivateTaskService.getById(id);
    }

    /**
     * 培训状态变更：用于发布、撤回等状态变更
     *
     * @param id
     * @param status
     */
    @PutMapping(value = "/headquarters/cultivate-task/{id}/update/{status}")
    public void cultivateStatusUpdate(@PathVariable Long id, @PathVariable String status) {
        headquartersCultivateTaskService.updateStatus(id, status);
    }

}