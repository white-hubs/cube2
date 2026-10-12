package com.guhecloud.cube.gm.meattrade.plugin.couple.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.po.CouplePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CoupleMapper extends BaseMapper<CouplePO> {

    void insertOrUpdateCouples(@Param("couples") List<CouplePO> couples);
}
