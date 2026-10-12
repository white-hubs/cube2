package com.guhecloud.cube.srv.gen.securitymanagement.server.information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.PlayRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.PlayRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.PlayRecordPageVo;
import com.guhecloud.platform.base.model.Pagination;

public interface PlayRecordService extends IService<PlayRecord> {
    Pagination<PlayRecordPageVo> getPlayRecordPage(PlayRecordParam param);

    Long getPlayRecordCount(PlayRecordParam param);
}
