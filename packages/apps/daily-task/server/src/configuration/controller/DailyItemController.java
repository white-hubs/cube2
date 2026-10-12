package com.guhecloud.cube.biz.daily.task.configuration.controller;

import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyItemResultRelationDetailResponse;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyItemResultRelationPageResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemService;
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

/**
 * 执行项的结果关联配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyItemController {
    private final DailyItemService dailyItemService;

    /**
     * 新增执行项的结果关联
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    @PostMapping("/configuration/insertDailyItemResultRelation")
    public Long insertDailyItemResultRelation(@RequestBody @Valid DailyItemResultRelationInsertParam param) {
        return dailyItemService.insertDailyItemResultRelation(param);
    }

    /**
     * 删除执行项的结果关联
     *
     * @param itemId 主键ID
     *
     * @return 是否成功
     */
    @DeleteMapping("/configuration/deleteDailyItemResultRelation/{itemId}")
    public Boolean deleteDailyItemResultRelation(@PathVariable Long itemId) {
        return dailyItemService.deleteDailyItemResultRelation(itemId);
    }

    /**
     * 修改执行项的结果关联
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/configuration/updateDailyItemResultRelation")
    public Boolean updateDailyItemResultRelation(@RequestBody @Valid DailyItemResultRelationUpdateParam param) {
        return dailyItemService.updateDailyItemResultRelation(param);
    }

    /**
     * 查询执行项的结果关联分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    @GetMapping("/configuration/queryDailyItemResultRelationPage")
    public Pagination<DailyItemResultRelationPageResponse> queryDailyItemResultRelationPage(DailyItemResultRelationPageParam param) {
        return dailyItemService.queryDailyItemResultRelationPage(param);
    }

    /**
     * 获取执行项的结果关联
     *
     * @param itemId 执行项ID
     *
     * @return 执行项的结果关联
     */

    @GetMapping("/configuration/getDailyItemResultRelation/{itemId}")
    public DailyItemResultRelationDetailResponse getDailyItemResultRelation(@PathVariable Long itemId) {
        return dailyItemService.getDailyItemResultRelation(itemId);
    }
}

