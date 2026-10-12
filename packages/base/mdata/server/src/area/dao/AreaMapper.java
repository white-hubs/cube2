package com.guhecloud.cube.biz.mdata.server.area.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.EnergyAreaVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AreaMapper extends BaseMapper<Area> {

    List<Area> getBySequences(@Param("sequences") List<String> sequences);

    List<Area> getListBySequences(@Param("sequence") String sequence);

    String getAreaNames(@Param("list") List<String> list);

    @InterceptorIgnore(tenantLine = "on")
    @Select("SELECT\n" +
            "\t`name` \n" +
            "FROM\n" +
            "\tarea \n" +
            "WHERE\n" +
            "\tid = #{id} and tenant =#{tenant}"
    )
    String getAreaNameById(@Param("id") Long id, @Param("tenant") Integer tenant);

    @InterceptorIgnore(tenantLine = "on")
    @Select("SELECT\n" +
            "\t`sequence` \n" +
            "FROM\n" +
            "\tarea \n" +
            "WHERE\n" +
            "\tsequence like concat('%',#{sequence},'-') limit 1"
    )
    Area getAreaNameBySequence(@Param("sequence") String sequence);

    List<EnergyAreaVo> innerList();

    void updateAreaSort(@Param("areaMap") Map<Long, Integer> areaMap);

    List<Area> getImportArea(@Param("queryParam") AreaQueryParam queryParam);

    /**
     * 获取导出的空间数据
     *
     * @param queryParam 条件
     *
     * @return 集合
     */
    List<AreaVO> getSpaceExportList(@Param("queryParam") AreaQueryParam queryParam);

}
