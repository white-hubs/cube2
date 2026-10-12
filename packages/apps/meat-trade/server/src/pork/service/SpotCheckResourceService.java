package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckResource;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckResourceQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckResourcePageVO;

import java.io.Serializable;
import java.util.List;

public interface SpotCheckResourceService extends IService<SpotCheckResource> {

    SpotCheckResource getById(Serializable id);

    List<SpotCheckResource> listByOrder(Serializable orderId, Integer tenant);

    Page<SpotCheckResourcePageVO> pageSCR(SpotCheckResourceQueryParam queryParam);

}
