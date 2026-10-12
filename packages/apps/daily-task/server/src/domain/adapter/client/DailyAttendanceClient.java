package com.guhecloud.cube.biz.daily.task.domain.adapter.client;

import com.guhecloud.cube.biz.daily.task.domain.adapter.param.ArrangeWorkParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 考勤
 */
@FeignClient("attendanceManagement")
public interface DailyAttendanceClient {
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
