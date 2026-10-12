package com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.ProcessInstancePageQuery;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.flowable.task.application.dto.CopyLogDTO;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.CopyLogPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/25 19:13
 * @description
 */
@Mapper
public interface CopyLogMapper extends BaseMapper<CopyLogPO> {

    @Select("select * from copy_log where find_in_set(#{userId},user_ids)")
    List<CopyLogDTO> selectListByUserId(@Param("userId") Long userId);

    Page<ProcessInstanceDTO> selectListByUserIdPage(@Param("page") Page page, @Param("query") ProcessInstancePageQuery query,
                                                    @Param("userId") String userId
                                                   );

}
