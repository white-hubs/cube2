package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.po.InspectOfflineSubmitOperationPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 离线提交幂等操作表访问接口。
 */
@Mapper
public interface InspectOfflineSubmitOperationMapper extends BaseMapper<InspectOfflineSubmitOperationPO> {
}
