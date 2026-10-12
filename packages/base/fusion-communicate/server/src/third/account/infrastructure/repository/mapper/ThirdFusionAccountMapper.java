package com.guhecloud.cube.biz.fusion.communicate.server.third.account.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionAccountPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ThirdFusionAccountMapper extends BaseMapper<ThirdFusionAccountPO> {

    void insertOrUpdateBatch(List<ThirdFusionAccountPO> thirdFusionAccountPOS);
}
