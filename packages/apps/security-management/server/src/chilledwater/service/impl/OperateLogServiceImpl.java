package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.OperateLogMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.OperateLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.enumeration.LogTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.OperateLogParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.OperateLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.OperateLogVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {
    private final UserClient userClient;

    private final OperateLogMapper operateLogMapper;

    @Override
    public void saveBusinessLog(String remark) {
        //业务日志
        OperateLog operateLog = new OperateLog();
        operateLog.setLogTime(LocalDateTime.now());
        operateLog.setLogType(LogTypeEnum.BUSINESS_LOG.name());
        Principal principal = SecurityContextHolder.getPrincipal();
        operateLog.setName(principal.getName());
        operateLog.setRemark(remark);
        UserDTO user = userClient.getUser(principal.getId());
        operateLog.setDepartment(user.getOrganization().getName());
        this.save(operateLog);
    }

    @Override
    public Pagination<OperateLogVo> getOperateLogs(OperateLogParam operateLogParam) {
        Page<OperateLog> page = this.page(PagingUtils.convert(operateLogParam), Wrappers.<OperateLog>lambdaQuery()
                .eq(StringUtils.isNotBlank(operateLogParam.getKeyword()), OperateLog::getName, operateLogParam.getKeyword())
                .eq(OperateLog::getLogType, operateLogParam.getLogTypeEnum())
                .eq(Objects.nonNull(operateLogParam.getDeviceId()), OperateLog::getDeviceId, operateLogParam.getDeviceId())
                .between(Objects.nonNull(operateLogParam.getPeriod()), OperateLog::getLogTime, operateLogParam.getPeriod()
                        .getBegin(), operateLogParam.getPeriod().getEnd()));
        return PagingUtils.convert(page, OperateLogVo.class);
    }

    @Override
    public Pagination<OperateLogVo> getOperateHardwareLogs(OperateLogParam operateLogParam) {
        Page<OperateLogVo> operateHardwareLogs = operateLogMapper.getOperateHardwareLogs(PagingUtils.convert(operateLogParam), operateLogParam);
        return PagingUtils.convert(operateHardwareLogs);
    }
}
