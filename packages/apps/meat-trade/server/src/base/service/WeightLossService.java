package com.guhecloud.cube.biz.meattrade.server.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.base.entity.WeightLoss;
import com.guhecloud.cube.biz.meattrade.server.base.param.WeightLossCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.WeightLossUpdateParam;
import com.guhecloud.platform.base.model.Paging;

public interface WeightLossService extends IService<WeightLoss> {

    /**
     * 重量损耗新增
     *
     * @param param 重量损耗新增参数
     */
    void save(WeightLossCreateParam param);

    /**
     * 重量损耗编辑
     *
     * @param param 重量损耗编辑参数
     */
    void update(WeightLossUpdateParam param);

    /**
     * 根据损耗时间获取重量损耗信息
     *
     * @param time 损耗时间
     *
     * @return 重量损耗信息
     */
    WeightLoss getByTime(long time);

    /**
     * 重量损耗分页
     *
     * @return 重量损耗分页信息
     */
    Page<WeightLoss> page(Paging paging);

    /**
     * 重量损耗删除
     *
     * @param id 重量损耗主键
     */
    void remove(Long id);

}
