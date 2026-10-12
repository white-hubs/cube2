package com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.param.MedicalDeviceParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.service.MedicalAlarmStatisticsService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.vo.MedicalAlarmVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Requester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备报表
 *
 * @Author: lijie
 * @CreateTime: 2024-04-01  13:59
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicaAlarmStatisticsController {
    private final MedicalAlarmStatisticsService medicalAlarmStatisticsService;

    /**
     * 根据租户查询报警日报表
     */
    @GetMapping("/getMedicalAlarmPage")
    public Pagination<MedicalAlarmVO> getMedicalAlarmPage(MedicalDeviceParam param) {
        SessionContextHolder.setRequester(new Requester(SessionContextHolder.getRequester().getUri(), SessionContextHolder.getRequester()
                .getHost(), SessionContextHolder.getRequester().getDomain(), SessionContextHolder.getRequester()
                                                                .getAuthorization(), param.getTenant(), SessionContextHolder.getRequester()
                                                                .getTerminal(), SessionContextHolder.getRequester()
                                                                .getVersion(), SessionContextHolder.getRequester()
                                                                .getSignature(), SessionContextHolder.getRequester().getPermission()));
        Pagination<MedicalAlarmVO> pagination = medicalAlarmStatisticsService.getMedicalAlarmPage(param);
        SessionContextHolder.clearSessionContext();
        return pagination;
    }

}
