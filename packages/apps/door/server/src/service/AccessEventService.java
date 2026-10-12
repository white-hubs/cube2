package com.guhecloud.cube.biz.door.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.door.server.entity.AccessEvent;

public interface AccessEventService extends IService<AccessEvent> {
    void insertOrUpdate(AccessEvent event);
}
