package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRule;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RulePageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleNormVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RulePageVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-20  15:44
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface MedicalRuleMapper extends BaseMapper<MedicalRule> {
    Page<RulePageVO> queryByPage(@Param("page") Page page, @Param("param") RulePageParam param);

    List<RuleVO> queryByDeviceIds(@Param("deviceId")String deviceId);

    Set<Long> queryRuleIds();
    @InterceptorIgnore(tenantLine = "true")
    List<RuleNormVO> getAllRule(@Param("display")Integer display,@Param("tenant")Integer tenant,@Param("deviceId")Long deviceId);
}
