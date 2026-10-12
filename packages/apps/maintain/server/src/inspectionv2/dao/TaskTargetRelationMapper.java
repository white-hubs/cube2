package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.TaskTargetRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectGeneratedTaskTargetVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskTargetRelationMapper extends BaseMapper<TaskTargetRelationPO> {

    /**
     * 查询任务下的巡检对象
     *
     * @param taskId
     *
     * @return
     */
    @Select("select distinct target_id, target_category from inspect_task_target_relation where task_id = #{taskId} order by create_time asc")
    List<TaskTargetRelationPO> selectTargetIds(@Param("taskId") Long taskId);

    List<TaskTargetRelationPO> selectOrderBy(@Param("taskId") Long taskId);

    /**
     * 查询需要处理的数据
     *
     * @return
     */
    List<TaskTargetRelationPO> selectHandleList();

    /**
     * 巡检对象分页（关联最新巡检记录），支持对象类型/对象名称/状态筛选
     *
     * @param page  分页对象
     * @param param 条件
     *
     * @return 巡检对象列表
     */
    Page<InspectionObjVO> selectInspectionObjPage(@Param("page") Page<?> page, @Param("param") TaskPageParam param);

    /**
     * 查询最新一次巡检结果为异常的对象列表。
     *
     * @param targetCategory 对象类型
     *
     * @return 对象列表
     */
    List<InspectGeneratedTaskTargetVO> selectLatestAbnormalTargets(@Param("targetCategory") String targetCategory);
}
