package com.guhecloud.cube.biz.meattrade.server.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.base.dto.AbattoirBimStatDTO;
import com.guhecloud.cube.biz.meattrade.server.base.dto.AbattoirStatDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AbattoirMapper extends BaseMapper<Abattoir> {

    Page<AbattoirStatDTO> statPage(Page<?> page, @Param("param") AbattoirQueryParam param);

    Page<AbattoirStatDTO> selectStatGoodsCategoryPage(Page<?> page, @Param("param") AbattoirQueryParam param);

    Page<AbattoirBimStatDTO> bimStatPage(Page<?> page, @Param("param") AbattoirQueryParam param);

    List<AbattoirBimStatDTO> bimStatList(@Param("param") AbattoirQueryParam param);
}
