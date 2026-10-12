package com.guhecloud.cube.srv.gen.conferencemanage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceGoods;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceGoodsParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceGoodsQueryParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @author 15561
 * @description 针对表【conference_goods(会议物品表)】的数据库操作Service
 * @createDate 2024-04-16 14:43:42
 */
public interface ConferenceGoodsService extends IService<ConferenceGoods> {
    /**
     * 分页查询
     *
     * @param param
     *
     * @return
     */
    Pagination<ConferenceGoods> getPage(ConferenceGoodsQueryParam param);

    /**
     * 增加物品
     *
     * @param param
     */
    void addConferenceGoods(ConferenceGoodsParam param);

    /**
     * 更新物品
     *
     * @param id
     * @param param
     */
    void updateConferenceGoods(Long id, ConferenceGoodsParam param);

    /**
     * 删除物品
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelete(List<Long> ids);

    List<ConferenceGoods> getGoodsList(ConferenceGoodsQueryParam param);
}
