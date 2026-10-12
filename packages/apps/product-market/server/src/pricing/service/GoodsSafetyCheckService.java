package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsSafetyCheck;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsSafetyCheckParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsSafetyCheckQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsSafetyCheckVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：食品安全检查服务层
 *
 * @author Appet
 * @date 2023-12-04 10:08:36
 */
public interface GoodsSafetyCheckService extends IService<GoodsSafetyCheck> {

    Long add(GoodsSafetyCheckParam param);

    void update(Long id, GoodsSafetyCheckParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsSafetyCheckVO getById(Long id);

    Pagination<GoodsSafetyCheckVO> getPage(GoodsSafetyCheckQueryParam param);

    void insertOrUpdateBatch(List<GoodsSafetyCheck> dataList);
}