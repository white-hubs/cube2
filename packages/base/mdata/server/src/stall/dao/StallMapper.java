package com.guhecloud.cube.biz.mdata.server.stall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.scene.param.StallSceneConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.param.StallSceneQueryParam;
import com.guhecloud.cube.biz.mdata.server.stall.dto.StallStatDTO;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.cube.biz.mdata.server.stat.param.AreaStatQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface StallMapper extends BaseMapper<Stall> {

    int updateBatchLeased(@Param("ids") Set<Long> ids, @Param("leased") Boolean leased, @Param("leaseType") Boolean leaseType);

    @Select("select substring_index(s.`area`,',', 1) as areaId,a.`name` as areaName,count(*) as stallNum from stall s " +
            "INNER JOIN `area` a on substring_index(s.`area`,',', 1) = a.`id` " +
            "GROUP BY `areaId`"
    )
    List<StallStatDTO> getStallNumByArea();

    Page<Stall> pageByAreas(Page<?> page, @Param("param") AreaStatQueryParam queryParam);

    List<Stall> listByAreas(@Param("areas") Collection<String> areas);

    Page<Stall> selectSceneStalls(Page<Stall> page, @Param("param") StallSceneQueryParam param);

    Page<Stall> selectSceneNotBindStalls(Page<Stall> page, @Param("param") StallSceneQueryParam param);

    Set<Long> selectConditionFilterStallsBySceneConfig(@Param("param") StallSceneConfigModifyParam param);
}
