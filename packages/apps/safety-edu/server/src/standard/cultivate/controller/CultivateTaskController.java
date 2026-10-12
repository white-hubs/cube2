package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.controller;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateCommonStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateMyTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskDetailVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskWithStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateUserStatVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * @author Appet
 * @Description 培训任务
 * @date 2023-04-18 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CultivateTaskController {
    private final CultivateTaskService cultivateTaskService;

    /**
     * 功能描述： 培训任务添加
     */
    @PostMapping(value = "/cultivate-task")
    public Long add(@Valid @RequestBody CultivateTaskParam param) {
        return cultivateTaskService.add(param);
    }

    /**
     * 功能描述： 培训任务修改
     */
    @PutMapping(value = "/cultivate-task/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody CultivateTaskParam param) {
        cultivateTaskService.update(id, param);
    }

    /**
     * 功能描述： 培训任务删除
     */
    @DeleteMapping(value = "/cultivate-task/{id}")
    public void delete(@PathVariable Long id) {
        cultivateTaskService.delete(id);
    }

    /**
     * 功能描述： 培训任务批量删除
     */
    @DeleteMapping(value = "/cultivate-task/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        cultivateTaskService.batchDelete(ids);
    }

    /**
     * 功能描述： 培训任务分页
     */
    @GetMapping(value = "/cultivate-task/page")
    public Pagination<CultivateTaskVO> getPage(CultivateTaskQueryParam param) {
        //处理时间
        if (param.getPeriod() != null && param.getPeriod().getEnd() == null) {
            Period period = Period.of(param.getPeriod().getBegin(), DateUtils.endOfDay(param.getPeriod().getBegin()));
            param.setPeriod(period);
        }
        return cultivateTaskService.getPage(param);
    }

    @GetMapping(value = "/app/cultivate-task/page")
    public Pagination<CultivateMyTaskVO> getMyTaskPage(CultivateTaskQueryParam param) {
        return cultivateTaskService.getMyTaskPage(param);
    }

    /**
     * 分页扩展统计信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/cultivate-task/page/extendStat")
    public Pagination<CultivateTaskWithStatVO> getPageExtendStat(CultivateTaskQueryParam param) {
        //处理时间
        if (param.getPeriod() != null && param.getPeriod().getEnd() == null) {
            Period period = Period.of(param.getPeriod().getBegin(), DateUtils.endOfDay(param.getPeriod().getBegin()));
            param.setPeriod(period);
        }
        //默认只查询 已结束 的培训
        param.setCultivateTaskStatus(TrainStatusEnum.OVER.name());
        return cultivateTaskService.getPageExtendStat(param);
    }

    /**
     * 导出培训信息
     *
     * @param param
     * @param request
     * @param response
     */
    @PostMapping(value = "/cultivate-task/extendStat/export")
    public void exportCultivateExtendStat(@RequestBody CultivateTaskQueryParam param,
                                          HttpServletRequest request,
                                          HttpServletResponse response
                                         ) {
        //处理时间
        if (param.getPeriod() != null && param.getPeriod().getEnd() == null) {
            Period period = Period.of(param.getPeriod().getBegin(), DateUtils.endOfDay(param.getPeriod().getBegin()));
            param.setPeriod(period);
        }
        //默认只查询 已结束 的培训
        param.setCultivateTaskStatus(TrainStatusEnum.OVER.name());
        cultivateTaskService.exportCultivateExtendStat(param, request, response);
    }

    /**
     * 功能描述： 培训任务详情
     */
    @GetMapping(value = "/cultivate-task/{id}")
    public CultivateTaskDetailVO getById(@PathVariable Long id) {
        return cultivateTaskService.getById(id);
    }

    /**
     * 培训签到
     *
     * @param id
     */
    @PutMapping(value = "/cultivate-task/{id}/sign")
    public void cultivateSign(@PathVariable Long id) {
        cultivateTaskService.cultivateSign(id);
    }

    /**
     * 培训状态变更：用于发布、撤回等状态变更
     *
     * @param id
     * @param status
     */
    @PutMapping(value = "/cultivate-task/{id}/update/{status}")
    public void cultivateStatusUpdate(@PathVariable Long id, @PathVariable String status) {
        cultivateTaskService.updateStatus(id, status);
    }

    /**
     * 培训趋势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/cultivate-task/trend/stat")
    public List<CultivateCommonStatVO> getCultivateTrendStat(CultivateTaskQueryParam param) {
        return cultivateTaskService.getCultivateTrendStat(param);
    }

    /**
     * 培训任务统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/cultivate-task/stat")
    public CultivateTaskStatVO getCultivateTaskStat(CultivateTaskQueryParam param) {
        return cultivateTaskService.getCultivateTaskStat(param);
    }

    /**
     * 培训任务统计
     *
     * @param param
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "safetyEducation:cultivateTaskUser")
    @GetMapping(value = "/cultivate-task/permissions/stat")
    public CultivateTaskStatVO getCultivateTaskPermissionStat(CultivateTaskQueryParam param) {
        return cultivateTaskService.getCultivateTaskPermissionStat(param);
    }

    /**
     * 培训主题统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/cultivate-task/theme/stat")
    public List<CultivateCommonStatVO> getCultivateThemeStat(CultivateTaskQueryParam param) {
        return cultivateTaskService.getCultivateThemeStat(param);
    }

    /**
     * 培训对象统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/cultivate-task/target/stat")
    public List<CultivateCommonStatVO> getCultivateTargetStat(CultivateTaskQueryParam param) {
        return cultivateTaskService.getCultivateTargetStat(param);
    }

    /**
     * 用户培训情况统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/cultivate-task/trainUser/stat")
    public CultivateUserStatVO getTrainUserStat(CultivateTaskQueryParam param) {
        return cultivateTaskService.getTrainUserStat(param);
    }

    /**
     * 用户培训签到情况统计
     * 以培训为维度
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/cultivate-task/userSign/stat")
    public List<CultivateUserStatVO> getUserSignStatByTask(CultivateTaskQueryParam param) {
        return cultivateTaskService.getUserSignStatByTask(param);
    }

    /**
     * 培训任务标准列表接口
     *
     * @param param
     *
     * @return
     */
    @PreAuthorize("hasScope('safety_education')")
    @GetMapping(value = "/cultivate-task/standard/list")
    public List<CultivateTaskPageVO> getCultivateStandardList(CultivateTaskQueryParam param) {
        return cultivateTaskService.getCultivateStandardList(param);
    }
}