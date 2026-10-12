package com.guhecloud.cube.biz.daily.task.configuration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyResultPO;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyResultInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyResultPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyResultUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyResultResponse;
import com.guhecloud.platform.base.model.Pagination;

public interface DailyResultService extends IService<DailyResultPO> {
    /**
     * 新增执行结果项
     *
     * @param param 入参
     * @return 主键ID
     */
    Long insertDailyResult(DailyResultInsertParam param);

    /**
     * 删除执行结果项
     *
     * @param id 主键ID
     * @return 是否成功
     */
    Boolean deleteDailyResult(Long id);

    /**
     * 修改执行结果项
     *
     * @param param 入参
     * @return 是否成功
     */
    Boolean updateDailyResult(DailyResultUpdateParam param);

    /**
     * 查询执行结果项分页
     *
     * @param param 查询条件
     * @return 分页列表
     */
    Pagination<DailyResultResponse> queryDailyResultPage(DailyResultPageParam param);
}
