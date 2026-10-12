package com.guhecloud.cube.biz.metric.server.metric.result.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.metric.server.metric.result.repository.po.MetricResultPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MetricResultMapper extends BaseMapper<MetricResultPO> {
}
