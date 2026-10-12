package com.guhecloud.cube.srv.gen.safety.management.notify.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.notify.dto.request.AlarmNoticeModifyRequest;
import com.guhecloud.cube.srv.gen.safety.management.notify.dto.vo.AlarmNoticeVO;
import com.guhecloud.cube.srv.gen.safety.management.notify.po.AlarmNoticePO;

import java.util.List;

public interface AlarmNoticeService extends IService<AlarmNoticePO> {

    void save(AlarmNoticeModifyRequest request);

    List<AlarmNoticeVO> getAlarmNoticeConfigInfo(String businessType, Boolean enabled);

    List<AlarmNoticeVO> getEnabledAlarmNoticeConfig(String businessType);

    AlarmNoticeVO getEnabledAlarmNotice(String businessType, String alarmLevelCode);

    void deleteMessageConfig(String businessType);
}
