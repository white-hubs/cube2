package com.guhecloud.cube.biz.daily.task.configuration.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardPO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

public interface DailyStandardService extends IService<DailyStandardPO> {
    /**
     * 新增执行标准
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    Long insertDailyStandard(DailyStandardInsertParam param);

    /**
     * 删除执行标准
     *
     * @param id 主键ID
     *
     * @return 是否成功
     */
    Boolean deleteDailyStandard(Long id);

    /**
     * 修改执行标准
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    Boolean updateDailyStandard(DailyStandardUpdateParam param);

    /**
     * 查询执行标准分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    Pagination<DailyStandardPageResponse> queryDailyStandardPage(DailyStandardPageParam param);

    /**
     * 根据ID查询执行标准详情
     *
     * @param id 主键ID
     *
     * @return 执行标准详情
     */
    DailyStandardDetailResponse getDailyStandardById(Long id);

    /**
     * 修改执行标准状态
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    Boolean updateDailyStandardStatus(DailyStandardUpdateStatusParam param);

    Map<String, List<DailyStandardByTypeResponse>> getStandardByTypeIds(DailyStandardByTypesParam param);

    /**
     * 根据对象ID及typeId集合查询哪些对象已关联标准
     *
     * @param param 对象及类型集合
     *
     * @return 关联标准的对象ID列表
     */
    KeepTargetResponse getTargetIdsByTypeIds(DailyStandardTargetRelationParam param);

    List<DailyStandardByTypeResponse> getDailyStandardByTaskTypeId(Long id);
}
