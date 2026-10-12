package com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po.ProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.ProcessInstancePageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wql
 * @describe 流程实例视图操作：all_process_instance
 * @date 2023/5/23 17:42
 */
@Mapper
public interface ProcessInstanceMapper extends BaseMapper<ProcessInstancePO> {

    Page<ProcessInstancePO> handlePage(@Param("page") Page page, @Param("param") ProcessInstancePageQuery param,
                                       @Param("tenant") Integer tenant
                                      );

}
