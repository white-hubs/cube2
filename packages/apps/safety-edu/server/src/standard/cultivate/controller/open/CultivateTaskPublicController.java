package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.controller.open;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateSignPublicParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskPublicQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateMyTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskDetailVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Appet
 * @Description 培训任务
 * @date 2023-04-18 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CultivateTaskPublicController {
    private final CultivateTaskService cultivateTaskService;

    /**
     * 功能描述： 培训任务分页
     */
    @GetMapping(value = "/public/cultivate-task/page")
    public Pagination<CultivateTaskVO> getPage(CultivateTaskQueryParam param) {
        SessionContextHolder.requestTenant(param.getTenant());
        //处理时间
        if (param.getPeriod() != null && param.getPeriod().getEnd() == null) {
            Period period = Period.of(param.getPeriod().getBegin(), DateUtils.endOfDay(param.getPeriod().getBegin()));
            param.setPeriod(period);
        }
        return cultivateTaskService.getPage(param);
    }

    @GetMapping(value = "/public/app/cultivate-task/page")
    public Pagination<CultivateMyTaskVO> getPublicMyTaskPage(CultivateTaskPublicQueryParam param) {
        return cultivateTaskService.getPublicMyTaskPage(param);
    }

    /**
     * 功能描述： 培训任务详情
     */
    @GetMapping(value = "/public/cultivate-task/{id}")
    public CultivateTaskDetailVO getById(@PathVariable Long id) {
        return cultivateTaskService.getById(id);
    }

    /**
     * 培训签到
     *
     * @param id
     */
    @PutMapping(value = "/public/cultivate-task/{id}/sign")
    public void cultivateSign(@PathVariable Long id, @RequestBody @Valid CultivateSignPublicParam cultivateSignPublicParam) {
        cultivateTaskService.cultivatePublicSign(id, cultivateSignPublicParam);
    }

    /**
     * 培训状态变更：用于发布、撤回等状态变更
     *
     * @param id
     * @param status
     */
    @PutMapping(value = "/public/cultivate-task/{id}/update/{status}")
    public void cultivateStatusUpdate(@PathVariable Long id, @PathVariable String status) {
        cultivateTaskService.updatePublicStatus(id, status);
    }

}