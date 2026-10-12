package com.guhecloud.cube.biz.daily.task.configuration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskCategoryPO;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyTaskCategoryInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyTaskCategoryPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyTaskCategoryUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyTaskCategoryResponse;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Map;
import java.util.Set;

public interface DailyTaskCategoryService extends IService<DailyTaskCategoryPO> {
    /**
     * 新增任务类型
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    Long insertDailyTaskCategory(DailyTaskCategoryInsertParam param);

    /**
     * 删除任务类型
     *
     * @param id 主键ID
     *
     * @return 是否成功
     */
    Boolean deleteDailyTaskCategory(Long id);

    /**
     * 修改任务类型
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    Boolean updateDailyTaskCategory(DailyTaskCategoryUpdateParam param);

    /**
     * 查询任务类型分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    Pagination<DailyTaskCategoryResponse> queryDailyTaskCategoryPage(DailyTaskCategoryPageParam param);

    Map<Long, DailyTaskCategoryPO> getCategoryByIds(Set<Long> taskCategoryIds);
}
