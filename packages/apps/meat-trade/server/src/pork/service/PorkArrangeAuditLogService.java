package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAuditLog;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzy
 * @date 2021/9/11
 */
public interface PorkArrangeAuditLogService extends IService<PorkArrangeAuditLog> {

    List<PorkArrangeAuditLog> listByPorkArrangeAuditId(Serializable id);

}
