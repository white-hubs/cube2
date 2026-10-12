package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.GroupControlLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.GroupControlLogQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.GroupControlLogVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.HlInGroupStatVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface GroupControlLogService extends IService<GroupControlLog> {

    Pagination<GroupControlLogVo> getGroupControlLogs(Long id, GroupControlLogQueryParam param);

    List<HlInGroupStatVo> getGroupControlStat(GroupControlLogQueryParam param);
}
