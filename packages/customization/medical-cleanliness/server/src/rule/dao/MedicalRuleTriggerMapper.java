package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleTrigger;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleTriggerVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MedicalRuleTriggerMapper extends BaseMapper<MedicalRuleTrigger> {
    @InterceptorIgnore(tenantLine = "true")
    RuleTriggerVO getAllById(Long id);
}
