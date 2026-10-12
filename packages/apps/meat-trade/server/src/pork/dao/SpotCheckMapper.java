package com.guhecloud.cube.biz.meattrade.server.pork.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheck;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("meattrade-spotCheckMapper")
public interface SpotCheckMapper extends BaseMapper<SpotCheck> {

    Page<SpotCheck> pageSpotCheck(Page<?> page, @Param("param") SpotCheckQueryParam queryParam);

    Page<SpotCheckVO> selectSpotCheck(Page<?> page, @Param("param") SpotCheckQueryParam queryParam);
}
