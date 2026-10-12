package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.ProductApplication;

import java.io.Serializable;
import java.util.List;

public interface ProductApplicationService extends IService<ProductApplication> {

    Long save(Long productId, Long applicationId);

    void remove(Long productId, Long applicationId);

    void removeByProduct(Serializable productId);

    void removeByApplication(Serializable applicationId);

    List<ProductApplication> listByProduct(Long productId);

    List<ProductApplication> listByApplication(Long applicationId);

}
