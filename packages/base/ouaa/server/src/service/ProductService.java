package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.param.ProductModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ProductQueryParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface ProductService extends IService<Product> {

    Long save(ProductModifyParam param);

    void update(Long id, ProductModifyParam param);

    void remove(Long id);

    Product getById(Serializable id);

    List<Product> listByIds(Collection<? extends Serializable> idList);

    Page<Product> listByPage(ProductQueryParam param);

}
