package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.param.AttStaffSequentPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttStaffSequentPageResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttStaffSequentialController {

    /**
     * 分页列表
     *
     * @param param 条件
     *
     * @return 分页对象
     */
    @GetMapping("/att/sequential/page")
    public Pagination<AttStaffSequentPageResult> getSequentPage(AttStaffSequentPageParam param) {
        return Pagination.empty();
    }
}
