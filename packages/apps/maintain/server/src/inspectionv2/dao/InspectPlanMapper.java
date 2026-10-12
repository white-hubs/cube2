package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.FireinspPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ProportionOfPlanTypesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 巡检计划 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Mapper
public interface InspectPlanMapper extends BaseMapper<InspectPlanPO> {
    @Select("SELECT count(id) as planNum,type_name as planType FROM inspect_plan ${ew.customSqlSegment}")
    List<ProportionOfPlanTypesVO> groupingStatistics(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT * FROM fireinsp_plan")
    List<FireinspPlanPO> getFireinspPlan();
}
