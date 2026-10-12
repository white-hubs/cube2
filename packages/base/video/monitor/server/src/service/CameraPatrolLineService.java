package com.guhecloud.cube.biz.vmonitor.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolLine;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineQueryParam;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public interface CameraPatrolLineService extends IService<CameraPatrolLine> {

    Long save(CameraPatrolLineModifyParam param);

    void update(Long id, CameraPatrolLineModifyParam param);

    void updateLastPatrol(Long id, LocalDateTime patrolTime);

    void updateEnabled(Long id, Boolean enabled);

    void remove(Long id);

    void removeByIds(List<Long> ids);

    CameraPatrolLine getById(Serializable id);

    Page<CameraPatrolLine> page(CameraPatrolLineQueryParam param);

    Page<CameraPatrolLine> listAuthCameraPatrolLines(CameraPatrolLineQueryParam param);

    List<CameraPatrolLine> getList(CameraPatrolLineQueryParam param);

}