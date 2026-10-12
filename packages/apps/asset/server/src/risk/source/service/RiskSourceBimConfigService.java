package com.guhecloud.cube.srv.gen.property.management.server.risk.source.service;

import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigDeleteParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigPageParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceBimConfigVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  13:54
 * @Description:
 * @Version: 1.0
 */
public interface RiskSourceBimConfigService {
    Pagination<RiskSourceLedgerVO> queryledgerByBimSceneTypePage(RiskSourceLedgerPageParam param);

    /**
     * 分页查询
     *
     * @param param: 筛选条件
     *
     * @return 查询结果
     *
     * @author : lijie
     */
    Pagination<RiskSourceBimConfigVO> queryByPage(RiskSourceBimConfigPageParam param);

    /**
     * 新增数据
     *
     * @param param: 新增数据
     *
     * @return 新增结果
     *
     * @author : lijie
     */
    Boolean insert(RiskSourceBimConfigParam param);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 删除结果
     *
     * @author : lijie
     */
    Boolean deleteById(RiskSourceBimConfigDeleteParam ids);
}
