package com.guhecloud.cube.biz.work.center.server.controller;

import com.guhecloud.cube.biz.work.center.server.param.WorkHomePageBatchParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkHomePageParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskQueryParam;
import com.guhecloud.cube.biz.work.center.server.service.WorkCenterService;
import com.guhecloud.cube.biz.work.center.server.vo.WorkHomePageVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工作中心接口
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/4 11:48
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/work/center")
public class WorkCenterController {

    private final WorkCenterService workCenterService;

    /**
     * 首页-看板
     */
    @GetMapping("/home/page/num")
    @DataPermissionSwitch(dataModel = "work:workPerson")
    public WorkHomePageVO getHomePageNum(WorkHomePageParam param) {
        return workCenterService.getHomePageNum(param);
    }

    /**
     * 任务统计
     * (一般是内部调用)
     */
    @PostMapping("/task/num/post")
    public WorkHomePageVO taskNumPost(@RequestBody WorkHomePageParam param) {
        return workCenterService.getHomePageNum(param);
    }

    /**
     * 批量获取任务统计
     */
    @PostMapping("/task/num/batch/post")
    public Map<Long, WorkHomePageVO> taskNumBatchPost(@RequestBody WorkHomePageBatchParam param) {
        return workCenterService.getHomePageNumBatch(param);
    }

    /**
     * 工作中心-任务查询
     */
    @GetMapping("/task/query")
    @DataPermissionSwitch(dataModel = "work:workPerson")
    public Pagination<WorkTaskPageVO> taskQuery(WorkTaskQueryParam param) {
        return workCenterService.taskQuery(param);
    }

    /**
     * 任务查询
     * (一般是内部调用)
     */
    @PostMapping("/task/query/post")
    public Pagination<WorkTaskPageVO> taskQueryPost(@RequestBody WorkTaskQueryParam param) {
        return workCenterService.taskQuery(param);
    }

    @PostMapping("/task/hand/user")
    public List<Long> getHandTaskUser(@RequestBody Set<Long> userIds) {
        return workCenterService.getHandTaskUser(userIds);
    }
}
