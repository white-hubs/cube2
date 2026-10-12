package com.guhecloud.cube.srv.gen.visitor.management.server.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitAreaDetailPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface VisitAreaDetailMapper extends BaseMapper<VisitAreaDetailPO> {

    /**
     * 逻辑删除
     *
     * @param longTime 删除时间
     * @param wrapper  条件
     *
     * @return 修改数量
     */
    @Update(" update `visit_area_detail` set `deleted` = #{newTime} ${ew.customSqlSegment} ")
    Integer removeDetail(@Param("longTime") Long longTime, @Param(Constants.WRAPPER) Wrapper<VisitAreaDetailPO> wrapper);

}
