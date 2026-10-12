package com.guhecloud.cube.srv.gen.disinfection.server.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberScheduledTask;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ScheduledTaskCreateParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ScheduledTaskQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ScheduledTaskUpdateParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ScheduledTaskDetailResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ScheduledTaskPageResult;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 定时任务服务
 */
public interface DisinfectionMemberScheduledTaskService extends IService<MemberScheduledTask> {

    /**
     * 创建定时任务（含关联设备与计划列表）
     *
     * @param param 任务名称、定时任务模式、关联设备id列表、定时任务计划列表
     * @return 定时任务主键 id
     */
    Long createScheduledTask(ScheduledTaskCreateParam param);

    /**
     * 修改定时任务（先删关联设备与计划，再按新数据插入）
     */
    Boolean updateScheduledTask(ScheduledTaskUpdateParam param);

    /**
     * 删除定时任务（含关联设备与计划）
     */
    Boolean deleteScheduledTask(Long id);

    /**
     * 获取定时任务详情
     */
    ScheduledTaskDetailResult getScheduledTaskDetail(Long id);

    /**
     * 定时任务分页列表
     */
    Pagination<ScheduledTaskPageResult> scheduledTaskPage(ScheduledTaskQueryParam param);
}
