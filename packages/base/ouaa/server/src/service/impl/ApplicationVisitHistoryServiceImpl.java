package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.ApplicationVisitHistoryMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationVisitHistory;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationVisitHistoryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 应用访问历史服务实现
 *
 * @author lanchun
 * @date 2022/8/9 10:25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationVisitHistoryServiceImpl extends ServiceImpl<ApplicationVisitHistoryMapper, ApplicationVisitHistory> implements ApplicationVisitHistoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull String applicationCode, @NonNull String applicationName) {
        ApplicationVisitHistory applicationVisitHistory = ApplicationVisitHistory.builder()
                .applicationCode(applicationCode)
                .applicationName(applicationName)
                .userId(SecurityContextHolder.getPrincipal().getId())
                .terminal(SessionContextHolder.getRequester().getTerminal())
                .visitTime(LocalDateTime.now())
                .build();
        this.baseMapper.insert(applicationVisitHistory);
        return applicationVisitHistory.getId();
    }

    @Override
    public List<Application> getUserApplicationVisitHistory(@NonNull Long userId, @NonNull String terminal) {
        return this.baseMapper.selectUserApplicationVisitHistory(userId, terminal, null);
    }

    @Override
    public List<Application> getUserApplicationVisitHistory(@NonNull Long userId, @NonNull String terminal,
                                                            @NonNull Collection<Long> applicationIds
                                                           ) {
        return this.baseMapper.selectUserApplicationVisitHistory(userId, terminal, applicationIds);
    }

    @Override
    public List<ApplicationVisitHistory> getUserApplicationVisitHistories(@NonNull Long userId, @NonNull String terminal,
                                                                          @NonNull Collection<Long> applicationIds
                                                                         ) {
        return this.baseMapper.selectUserApplicationVisitHistories(userId, terminal, applicationIds);
    }
}
