package com.guhecloud.cube.biz.work.center.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.work.center.server.entity.WorkPerson;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface WorkPersonService extends IService<WorkPerson> {

    void saveBatch(Long workTaskId, String businessKey, List<Long> userIds);

    void updateStatus(Set<Long> workTaskIds, Set<Long> userIds);

    void removeByWorkTaskIds(Collection<Long> workTaskIds);
}
