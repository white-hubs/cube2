package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.querygateway.ArrangeAndDutyWorkQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.querygateway.query.ArrangeAndDutyWorkQueryParam;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArrangeAndDutyWorkController {

    private final ArrangeAndDutyWorkQueryService arrangeAndDutyWorkQueryService;

    @GetMapping("/arrange-duty/staff/page1")
    public Pagination<StaffDetailDTO> getStaffPage1(ArrangeAndDutyWorkQueryParam query) {
        return arrangeAndDutyWorkQueryService.getStaffPage(query);
    }

    @GetMapping("/arrange-duty/staff/page")
    public Pagination<StaffDetailDTO> getStaffPage(ArrangeAndDutyWorkQueryParam query) {
        return arrangeAndDutyWorkQueryService.getStaffDTOPage(query);
    }

}
