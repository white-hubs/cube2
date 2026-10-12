package com.guhecloud.cube.iot.mgt.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.iot.mgt.base.model.CommandRecord;
import com.guhecloud.cube.iot.mgt.device.param.CommandHistoryPageParam;
import com.guhecloud.cube.iot.mgt.device.vo.CommandHistoryVo;
import com.guhecloud.cube.iot.mgt.device.vo.CommandLogVo;
import com.guhecloud.cube.iot.mgt.north.param.MethodInvokeHistoryParam;
import java.util.List;
import com.guhecloud.cube.iot.mgt.north.vo.MethodInvokeHistoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommandRecordMapper extends BaseMapper<CommandRecord> {

    /**
     * 查询方法调用状态
     *
     * @param instanceId 实例ID
     * @return 方法调用状态详情
     */
    MethodInvokeHistoryVo getMethodInvokeStatus(@Param("instanceId") String instanceId, @Param("downCmd") String downCmd);

    /**
     * 分页查询命令下发历史（主干）
     *
     * @param page 分页参数
     * @param param 过滤参数
     */
    Page<CommandHistoryVo> selectCommandHistoryPage(Page<CommandHistoryVo> page, @Param("param") CommandHistoryPageParam param);

    /**
     * 查询针对某一实例的流转细则日志
     *
     * @param instanceId 实例ID
     */
    List<CommandLogVo> selectCommandLogs(@Param("instanceId") String instanceId);
}