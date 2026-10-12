package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationVisitHistory;

import java.util.Collection;
import java.util.List;

public interface ApplicationVisitHistoryService extends IService<ApplicationVisitHistory> {

    Long save(String applicationCode, String applicationName);

    List<Application> getUserApplicationVisitHistory(Long userId, String terminal);

    List<Application> getUserApplicationVisitHistory(Long userId, String terminal, Collection<Long> applicationIds);

    List<ApplicationVisitHistory> getUserApplicationVisitHistories(Long userId, String terminal, Collection<Long> applicationIds);
}
