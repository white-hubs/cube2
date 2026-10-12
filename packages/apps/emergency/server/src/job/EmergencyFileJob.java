package com.guhecloud.cube.biz.emergency.server.job;

import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileNoticeService;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileNoticeVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileJob {

    private final EmergencyFileService emergencyFileService;

    private final EmergencyFileNoticeService emergencyFileNoticeService;

    @JobAnnotation(name = "emergencyFileExpirationStatus", cron = "0 0 0 * * ?", desc = "预案管理-预案文件-状态定时检测")
    public void updateEmergencyFileExpirationStatus(String param) {
        SessionContextHolder.clearSessionContext();
        EmergencyFileNoticeVO emergencyFileNotice = this.emergencyFileNoticeService.getEmergencyFileNotice();
        var expirationEmergencyFiles = this.emergencyFileService.getExpirationEmergencyFiles();
        for (var emergencyFile : expirationEmergencyFiles) {
            emergencyFileService.expirationStatus(emergencyFile);
            SessionContextHolder.requestTenant(emergencyFile.getTenant());
            this.emergencyFileService.updateById(emergencyFile);
            //消息发送
            this.emergencyFileService.msgSend(emergencyFileNotice, emergencyFile);
        }
    }
}
