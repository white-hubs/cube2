package com.guhecloud.cube.srv.gen.property.management.server.risk.source.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigPageParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceBimConfigVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceBimConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  13:58
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface RiskSourceBimConfigMapper extends BaseMapper<RiskSourceBimConfig> {
    Page<RiskSourceBimConfigVO> queryByPage(@Param("page") Page page, @Param("param") RiskSourceBimConfigPageParam param);

    List<RiskSourceLedgerVO> queryBySceneTypeAll(@Param("param") RiskSourceBimConfigParam param);

    Page<RiskSourceLedgerVO> queryBySceneTypePage(@Param("page") Page page, @Param("param") RiskSourceLedgerPageParam param);

}
