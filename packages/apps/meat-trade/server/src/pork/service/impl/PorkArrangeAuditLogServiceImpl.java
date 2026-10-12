package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkArrangeAuditLogMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAuditLog;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditLogService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzy
 * @date 2021/9/11
 */
@Slf4j
@Service("meattrade-porkArrangeAuditLogService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkArrangeAuditLogServiceImpl extends ServiceImpl<PorkArrangeAuditLogMapper, PorkArrangeAuditLog> implements PorkArrangeAuditLogService {

    @Override
    public List<PorkArrangeAuditLog> listByPorkArrangeAuditId(@NonNull Serializable id) {
        return this.lambdaQuery()
                .eq(PorkArrangeAuditLog::getPorkArrangeAuditId, id)
                .orderByDesc(PorkArrangeAuditLog::getTime)
                .list();
    }
}
