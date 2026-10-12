package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.job;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.application.dumu.UserManageApplication;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-01  16:46
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttUpdateFacialPhotos {
    private final UserManageApplication userManageApplication;

    /**
     * @param param
     *
     * @return
     */
    public void execute(String param) {
        SessionContextHolder.clearSessionContext();
        log.info("att:开始批量同步人脸照片：{}", param);
        var tenants = param.split(",");
        for (String tenant : tenants) {
            SessionContextHolder.requestTenant(Integer.parseInt(tenant));
            userManageApplication.addAllUser();
        }
        SessionContextHolder.clearSessionContext();
    }
}
