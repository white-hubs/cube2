package com.guhecloud.cube.srv.gen.attendance.management.client;

import com.guhecloud.cube.srv.gen.attendance.management.client.param.ArrangeWorkParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("attendanceManagement")
public interface AttSchedulingClient {

    /**
     * 筛选当班人员，参数传人员ID数组，筛选出当前时间有班次的人员
     *
     * @param userIds 需要筛选的人员ID数组
     *
     * @return
     */
    @PostMapping("/att/getOnDutyPersonnel")
    List<Long> getOnDutyPersonnel(@RequestBody ArrangeWorkParam param);

    /**
     * 筛选当班人员，参数传人员ID数组，筛选出当前时间有班次的人员
     *
     * @param param 需要筛选的人员ID数组 和时间
     *
     * @return 在岗人员ID列表
     */
    @PostMapping("/att/postOnDutyPersonnel")
    List<Long> postOnDutyPersonnel(@RequestBody ArrangeWorkParam param);

}
