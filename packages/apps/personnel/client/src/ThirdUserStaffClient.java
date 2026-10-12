package com.guhecloud.cube.srv.gen.personnel.management.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(ServiceRoute.TAG)
public interface ThirdUserStaffClient {

    /**
     * 根据第三方用户ID获取员工信息
     *
     * @return
     */
    @GetMapping("/third-user-staff/{thirdUserId}")
    public Long getStaffByThirdUserId(@PathVariable("thirdUserId") String thirdUserId);

}
