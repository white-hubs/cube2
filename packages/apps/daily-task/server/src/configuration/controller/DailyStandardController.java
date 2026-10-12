package com.guhecloud.cube.biz.daily.task.configuration.controller;

import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardByTypesParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardTargetRelationParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardUpdateStatusParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyStandardByTypeResponse;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyStandardDetailResponse;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyStandardPageResponse;
import com.guhecloud.cube.biz.daily.task.configuration.response.KeepTargetResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 执行标准配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyStandardController {
    private final DailyStandardService dailyStandardService;

    /**
     * 新增执行标准
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    @PostMapping("/configuration/insertDailyStandard")
    public Long insertDailyStandard(@RequestBody @Valid DailyStandardInsertParam param) {
        return dailyStandardService.insertDailyStandard(param);
    }

    /**
     * 删除执行标准
     *
     * @param id 主键ID
     *
     * @return 是否成功
     */
    @DeleteMapping("/configuration/deleteDailyStandard/{id}")
    public Boolean deleteDailyStandard(@PathVariable Long id) {
        return dailyStandardService.deleteDailyStandard(id);
    }

    /**
     * 修改执行标准
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/configuration/updateDailyStandard")
    public Boolean updateDailyStandard(@RequestBody @Valid DailyStandardUpdateParam param) {
        return dailyStandardService.updateDailyStandard(param);
    }

    /**
     * 查询执行标准分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    @GetMapping("/configuration/queryDailyStandardPage")
    public Pagination<DailyStandardPageResponse> queryDailyStandardPage(DailyStandardPageParam param) {
        return dailyStandardService.queryDailyStandardPage(param);
    }

    /**
     * 根据ID查询执行标准详情
     *
     * @param id 主键ID
     *
     * @return 执行标准详情
     */
    @GetMapping("/configuration/getDailyStandard/{id}")
    public DailyStandardDetailResponse getDailyStandard(@PathVariable Long id) {
        return dailyStandardService.getDailyStandardById(id);
    }

    /**
     * 修改执行标准状态
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/configuration/updateDailyStandardStatus")
    public Boolean updateDailyStandardStatus(@RequestBody @Valid DailyStandardUpdateStatusParam param) {
        return dailyStandardService.updateDailyStandardStatus(param);
    }

    /**
     * 根据类型ID列表获取标准数据
     *
     * @param param 包含类型ID列表的查询参数对象
     *
     * @return 按类型ID分组的标准数据映射，key为类型ID，value为该类型下的标准数据列表
     */
    @PostMapping("/configuration/getStandardByTypeIds")
    public Map<String, List<DailyStandardByTypeResponse>> getStandardByTypeIds(@RequestBody DailyStandardByTypesParam param) {
        return dailyStandardService.getStandardByTypeIds(param);
    }

    /**
     * 查询已关联的对象id列表
     *
     * @param param 对象ID集合
     *
     * @return 标准ID列表
     */
    @PostMapping("/configuration/getTargetIdsByTypeIds")
    public KeepTargetResponse getTargetIdsByTypeIds(@RequestBody @Valid DailyStandardTargetRelationParam param) {
        return dailyStandardService.getTargetIdsByTypeIds(param);
    }

    /**
     * 根据任务类型获取任务标准
     */
    @GetMapping("/configuration/getDailyStandard/taskTypeId/{id}")
    public List<DailyStandardByTypeResponse> getDailyStandardByTaskTypeId(@PathVariable Long id) {
        return dailyStandardService.getDailyStandardByTaskTypeId(id);
    }

}

