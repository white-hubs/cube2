package com.guhecloud.cube.biz.meattrade.server.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.base.entity.TallyOperationLog;
import com.guhecloud.cube.biz.meattrade.server.base.param.TallyOperationLogCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.TallyOperationLogQueryParam;

/**
 * @author lzy
 * @date 2021/9/9
 */
public interface TallyOperationLogService extends IService<TallyOperationLog> {

    /**
     * 理货机运行日志新增
     *
     * @param param 新增参数
     */
    void save(TallyOperationLogCreateParam param);

    /**
     * 理货机运行日志分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 理货机运行分页查询
     */
    Page<TallyOperationLog> page(TallyOperationLogQueryParam queryParam);

}
