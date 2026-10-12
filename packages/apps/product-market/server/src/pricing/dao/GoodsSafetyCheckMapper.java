package com.guhecloud.cube.srv.gen.product.market.server.pricing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsSafetyCheck;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：食品安全检查 Repository接口
 *
 * @author Appet
 * @date 2023-12-04 10:08:36
 */
@Repository
public interface GoodsSafetyCheckMapper extends BaseMapper<GoodsSafetyCheck> {

    void insertOrUpdateBatch(List<GoodsSafetyCheck> dataList);
}