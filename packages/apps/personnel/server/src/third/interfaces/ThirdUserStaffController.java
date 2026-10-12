package com.guhecloud.cube.srv.gen.personnel.management.server.third.interfaces;

import com.guhecloud.cube.srv.gen.personnel.management.server.third.ThirdUserStaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 三方用户和员工关联 接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdUserStaffController {

    private final ThirdUserStaffService thirdUserStaffService;

    /**
     * 根据第三方用户ID获取员工信息
     *
     * @return
     */
    @GetMapping("/third-user-staff/{thirdUserId}")
    public Long getStaffByThirdUserId(@PathVariable("thirdUserId") String thirdUserId) {
        return thirdUserStaffService.getStaffByThirdUserId(thirdUserId);
    }

}
