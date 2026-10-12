package com.guhecloud.cube.srv.gen.property.management.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.property.management.server.common.entity.Storehouse;
import com.guhecloud.cube.srv.gen.property.management.server.common.param.StorehouseParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.param.StorehouseQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.vo.StorehouseVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：仓库管理服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
public interface StorehouseService extends IService<Storehouse> {

    Long add(StorehouseParam param);

    void update(Long id, StorehouseParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    StorehouseVO getById(Long id);

    Pagination<StorehouseVO> getPage(StorehouseQueryParam param);

    List<StorehouseVO> getList(StorehouseQueryParam param);

}