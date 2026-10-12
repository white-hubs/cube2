package com.guhecloud.cube.biz.daily.task.configuration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyItemResultRelationDetailResponse;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyItemResultRelationPageResponse;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemPO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DailyItemService extends IService<DailyItemPO> {
    /**
     * 新增执行项的结果关联
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    Long insertDailyItemResultRelation(DailyItemResultRelationInsertParam param);

    /**
     * 删除执行项的结果关联
     *
     * @param itemId 主键ID
     *
     * @return 是否成功
     */
    Boolean deleteDailyItemResultRelation(Long itemId);

    /**
     * 修改执行项的结果关联
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    Boolean updateDailyItemResultRelation(DailyItemResultRelationUpdateParam param);

    /**
     * 查询执行项的结果关联分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    Pagination<DailyItemResultRelationPageResponse> queryDailyItemResultRelationPage(DailyItemResultRelationPageParam param);

    DailyItemResultRelationDetailResponse getDailyItemResultRelation(Long itemId);

    Map<Long, DailyItemResultDTO> getItemInfoList(Set<Long> itemIds);

    void saveBatch(List<DailyItemResultRelationInsertParam> noExistItemList);

    void removeTempItem(List<Long> taskItemIdList);

}
