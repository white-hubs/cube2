package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.PlanRelationDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionItemVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionStandardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface PlanRelationMapper extends BaseMapper<PlanRelationPO> {

    /**
     * 查询计划下的巡检对象
     *
     * @param planId
     *
     * @return
     */
    @Select("select distinct target_id, target_category from inspect_plan_relation where plan_id = #{planId}")
    List<PlanRelationPO> selectTargetIds(@Param("planId") Long planId);

    List<InspectionStandardVO> getObjStandardList(@Param("standardIdList") Set<Long> standardIdList);

    List<InspectionItemVO> getStandardItem(@Param("itemIds") Set<Long> itemIds);

    List<InspectionItemVO> getTemplateItem(@Param("itemIds") Set<Long> itemIds);

    /**
     * 查询对象的巡检计划(原始计划项)
     *
     * @return
     */

    List<PlanRelationDTO> selectRelationListRaw(@Param("plainId") Long plainId, @Param("targetId") Long targetId);

    /**
     * 查询对象的巡检计划(新加计划项)
     *
     * @return
     */
    List<PlanRelationDTO> selectRelationListAdd(@Param("plainId") Long plainId, @Param("targetId") Long targetId);

    /**
     * 获取巡检计划的巡检对象
     *
     * @param plainId
     *
     * @return
     */
    Page<PlanRelationPO> selectRelationTarget(@Param("page") Page<PlanRelationPO> page, @Param("plainId") Long plainId,
                                              @Param("targetCategory") String targetCategory, @Param("orderBy") String orderBy
                                             );
}
