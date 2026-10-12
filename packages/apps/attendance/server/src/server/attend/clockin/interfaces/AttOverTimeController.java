package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.WorkOmitParam;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-13  10:26
 * @Description: 加班
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttOverTimeController {
    /**
     * 加班统计
     */
    @GetMapping("/getOverTimeByPage")
    public Pagination getOverTimeByPage(WorkOmitParam param) {
        return null;
    }

    /**
     * 加班明细
     */
    @GetMapping("/getOverTimeDetails")
    public Pagination getOverTimeDetails(WorkOmitParam param) {
        return null;
    }
}
