package com.guhecloud.cube.biz.work.center.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.work.center.server.entity.HandoverTaskRef;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/29 16:20
 * @Description:
 */
public interface HandoverTaskRefService extends IService<HandoverTaskRef> {

    void addHandoverTaskRef(Long id, List<Long> handoverTaskList);
}
