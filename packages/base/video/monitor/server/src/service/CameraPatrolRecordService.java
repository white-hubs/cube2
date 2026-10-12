package com.guhecloud.cube.biz.vmonitor.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecord;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolSnapCapture;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordUserVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolSnapCaptureVO;

import java.io.Serializable;
import java.util.List;

public interface CameraPatrolRecordService extends IService<CameraPatrolRecord> {

    Long save(CameraPatrolRecordModifyParam param);

    CameraPatrolRecord getById(Serializable id);

    Page<CameraPatrolRecord> page(CameraPatrolRecordQueryParam param);

    List<CameraPatrolRecord> getList(CameraPatrolRecordQueryParam param);

    List<String> getPatrolUserNamesByLineId(Long cameraPatrolLineId);

    List<CameraPatrolRecordUserVO> getPatrolUserNameByLineIds(List<Long> cameraPatrolLineIds);

    void remove(Long id);

    CameraPatrolSnapCaptureVO snapCapture(CameraPatrolSnapCapture param);
}