package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationExtend;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationExtendInfo;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/5 10:40
 * @Description: 应用扩展信息服务
 */
public interface ApplicationExtendService extends IService<ApplicationExtend> {

    /**
     * 保存或更新应用扩展信息
     *
     * @param id
     * @param extendParam
     */
    void saveOrUpdate(Long id, ApplicationExtendInfo extendParam);
}
