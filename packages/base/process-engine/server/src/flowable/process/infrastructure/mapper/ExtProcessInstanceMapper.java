package com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.dto.ProcessInstanceNumberOfGroupByResultDTO;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.ProcessInstanceHistoricalQuery;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.ProcessInstancePageQuery;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.NumberOfApplicationDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.ProcessInstanceDTO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/9/5 16:44
 * @description
 */
@Mapper
public interface ExtProcessInstanceMapper extends BaseMapper<ExtProcessInstancePO> {
    @Select("select application_id,count(id) count from process_instance_running where create_time between #{period.begin} and #{period.end} and application_code is not null group by application_code ")
    List<NumberOfApplicationDTO> selectNumberOfApplication(@Param("period") Period period);

    @Select("select instance_name from process_instance_running where id = #{id} ")
    String getInstanceNameByProcInstanceId(@Param("id") String id);

    @Update("update process_instance_running set start_user_info = #{userInfo} where id = #{id} ")
    void updateStartUserInfo(@Param("id") String id, @Param("userInfo") String userInfo);

    List<ProcessInstanceNumberOfGroupByResultDTO> selectNumberGroupByResult(@Param("appCodes") List<String> appCodes);

    Page<ProcessInstanceDTO> selectTaskPage(@Param("page") Page page, @Param("query") ProcessInstancePageQuery query,
                                            @Param("userId") String userId
                                           );

    Page<ProcessInstanceDTO> selectInitiatedPage(@Param("page") Page page, @Param("query") ProcessInstancePageQuery query);

    List<ProcessInstanceDTO> selectInitiatedPage(@Param("query") ProcessInstanceHistoricalQuery query);

    Page<String> getProcessInstanceIdsByUser(@Param("page") Page page, @Param("query") ProcessInstancePageQuery query);

    /**
     * 与 getProcessInstanceIdsByUser 条件一致，不分页返回全部流程实例 id（个人导出使用）
     */
    List<String> listProcessInstanceIdsByUser(@Param("query") ProcessInstancePageQuery query);

    List<String> getProcessInstanceIds(@Param("query") ProcessInstanceHistoricalQuery query);

    String selectHistoryUserLastSign(@Param("assigneeId") Long assigneeId);

    String selectLastedFinishTask(@Param("procInstId") String procInstId);

    ProcessInstanceDTO selectInitiatedPage(@Param("query") ProcessInstancePageQuery query);
}
