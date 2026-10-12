package com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.interfaces;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.commandgateway.commond.LeavePostCommand;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 离岗申请 接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LeavePostController {
    private final FlowClient flowClient;

    /**
     * 检查离岗参数 是否符合要求
     *
     * @return
     */
    @PostMapping("/leave/post/check")
    public Boolean classesChangeCheck(@RequestBody FormDataParam param) {
        String biz = flowClient.formParse(param);
        LeavePostCommand command = JSONObject.parseObject(biz, LeavePostCommand.class);
        LocalDateTime startTime = DateUtils.newLocalDateTime(command.getStartTime());
        LocalDateTime endTime = DateUtils.newLocalDateTime(command.getEndTime());
        //开始时间大于当前时间
        Assert.isNotAcceptable(startTime.isBefore(LocalDateTime.now()), "离岗开始时间必须大于当前时间");
        Assert.isNotAcceptable(endTime.isBefore(startTime), "返岗时间必须大于离岗时间");
        return true;
    }

}
