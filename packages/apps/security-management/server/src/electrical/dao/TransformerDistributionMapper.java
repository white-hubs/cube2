package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.TransformerDistribution;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.TransformerDistributionVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransformerDistributionMapper extends BaseMapper<TransformerDistribution> {
    List<TransformerDistributionVo> getTransformerDistributionV0();
}
