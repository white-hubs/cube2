package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlInfo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.TimeControlInfoParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.TimeControlInfoQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.TimeControlInfoVo;
import com.guhecloud.platform.base.model.Pagination;

public interface TimeControlInfoService extends IService<TimeControlInfo> {
    void addTimeControl(TimeControlInfoParam param);

    void updateTimeControl(Long id, TimeControlInfoParam param);

    void deleteTimeControl(Long id);

    TimeControlInfoVo getTimeControlInfo(Long id);

    Pagination<TimeControlInfoVo> getTimeControls(TimeControlInfoQueryParam param);
}
