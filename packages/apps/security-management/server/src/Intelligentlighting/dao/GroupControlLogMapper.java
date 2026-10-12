package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.GroupControlLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.GroupControlLogQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.CommonStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupControlLogMapper extends BaseMapper<GroupControlLog> {

    List<CommonStat> getGroupControlStat(GroupControlLogQueryParam param);
}
