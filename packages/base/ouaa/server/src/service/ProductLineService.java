package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.ProductLine;
import com.guhecloud.cube.biz.ouaa.server.param.ProductLineModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ProductLineQueryParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface ProductLineService extends IService<ProductLine> {

    Long save(ProductLineModifyParam param);

    void update(Long id, ProductLineModifyParam param);

    void remove(Long id);

    ProductLine getById(Serializable id);

    List<ProductLine> listByIds(Collection<? extends Serializable> idList);

    Page<ProductLine> listByPage(ProductLineQueryParam param);

}
