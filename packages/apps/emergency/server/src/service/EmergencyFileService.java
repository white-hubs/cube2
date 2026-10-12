package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFile;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileDataCollectVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileNoticeVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFilePageVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileSafetyReportVO;
import com.guhecloud.platform.base.model.Period;

import java.io.Serializable;
import java.util.List;

public interface EmergencyFileService extends IService<EmergencyFile> {

    Long save(EmergencyFileModifyParam param);

    void update(Long id, EmergencyFileModifyParam param);

    List<EmergencyFile> getExpirationEmergencyFiles();

    Page<EmergencyFilePageVO> page(EmergencyFileQueryParam param);

    EmergencyFile getById(Serializable id);

    void remove(Long id);

    List<EmergencyFileDataCollectVO> safetyManagementDataCollect(Period period);

    EmergencyFileSafetyReportVO getEmergencyFileForSafetyReport(Period period);

    void expirationStatus(EmergencyFile emergencyFile);

    void msgSend(EmergencyFile emergencyFile);

    void msgSend(EmergencyFileNoticeVO emergencyFileNotice, EmergencyFile emergencyFile);
}
