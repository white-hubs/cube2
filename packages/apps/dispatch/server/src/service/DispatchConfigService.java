package com.guhecloud.cube.biz.dispatch.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchConfig;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchConfigParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchConfigQueryParam;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchConfigVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：调度台配置服务层
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
public interface DispatchConfigService extends IService<DispatchConfig> {

    Long add(DispatchConfigParam param);

    void update(Long id, DispatchConfigParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    DispatchConfigVO getById(Long id);

    Pagination<DispatchConfigVO> getPage(DispatchConfigQueryParam param);

}