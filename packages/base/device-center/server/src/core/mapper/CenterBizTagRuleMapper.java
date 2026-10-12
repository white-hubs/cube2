package com.guhecloud.cube.base.device.center.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.device.center.core.model.BizTagRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CenterBizTagRuleMapper extends BaseMapper<BizTagRule> {

    BizTagRule getCenterBizTagRule(@Param("tagCode") String tagCode);
}
