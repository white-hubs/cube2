package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckTaskParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskEvolveVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedExcelVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 盘点计划;数据库访问层
 *
 * @author : lijie
 * @date : 2023-3-17
 */
@Mapper
public interface PropertyCheckTaskMapper extends BaseMapper<PropertyCheckTask> {
    Page<PropertyDetailedVO> queryByPlanIdPage(@Param("page") Page page, @Param("planId") Long planId);

    List<PropertyDetailedExcelVO> queryByPlanIdExcel(@Param("planId") String planId);

    Page<PropertyCheckTaskPageVO> queryTaskByUserIdPage(@Param("page") Page page, @Param("param") PropertyCheckPlanPageParam param,
                                                        @Param("checkUserId") Long checkUserId
                                                       );

    PropertyCheckTaskEvolveVO getCheckTaskEvolve(@Param("planId") Long planId);

    Page<PropertyCheckTaskPageVO> queryCheckTaskDetailed(@Param("taskId") Long taskId);

    Page<PropertyDetailedVO> queryAllByTaskIdPage(@Param("page") Page page, @Param("taskId") Long taskId,
                                                  @Param("taskStatus") String taskStatus
                                                 );

    PropertyCheckTaskEvolveVO queryByPalan(@Param("planId") Long planId);

    Boolean getChooseType(@Param("taskId") Long taskId);

    List<PropertyCheckTaskPageVO> queryTaskByPlan(@Param("planId") Long planId);

    Page<PropertyCheckTaskPageVO> queryTaskByPropertyId(@Param("page") Page page, @Param("param") PropertyCheckTaskParam param);

}
