package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.RuleNorm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 指标设置(rule_norm)表数据库访问层
 * @author : lijie
 * @date : 2024-3-20
 */
@Mapper
public interface RuleNormMapper  extends BaseMapper<RuleNorm> {
    List<RuleNorm> getRuleNormsByDeviceId(@Param("deviceId") Long deviceId);
}