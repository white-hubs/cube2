package com.guhecloud.cube.sn.meattrade.plugin.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.sn.meattrade.plugin.common.param.PorkArrangeQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.common.po.PorkArrangePushDataPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Set;

@Mapper
public interface PorkArrangePushDataMapper extends BaseMapper<PorkArrangePushDataPO> {

    Set<String> selectTallIds(@Param("param") PorkArrangeQueryParam param);

    Set<String> selectFailPushTallIds(@Param("tallIds") Collection<String> tallIds);

    Set<String> selectStayPushTallIds(@Param("tallIds") Collection<String> tallIds);
}
