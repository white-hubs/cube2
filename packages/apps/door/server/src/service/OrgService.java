package com.guhecloud.cube.biz.door.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.door.server.entity.Org;

public interface OrgService extends IService<Org> {
    void insertOrUpdate(Org org);
}
