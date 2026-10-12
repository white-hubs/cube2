package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.CommandDispatchRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.query.CommandRecordPageQuery;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.CommandRecordPageVo;
import com.guhecloud.platform.base.model.Pagination;

public interface CommandDispatchRecordService extends IService<CommandDispatchRecordEntity> {

    Pagination<CommandRecordPageVo> getCommandDispatchRecordPage(CommandRecordPageQuery param);

    CommandRecordPageVo getCommandDispatchRecordInfo(Long id);
}
