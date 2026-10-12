package com.guhecloud.cube.biz.work.center.server.coverts;

import com.guhecloud.cube.biz.work.center.server.entity.WorkTask;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskPageVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/31 10:23
 * @Description:
 */
public class WorkTaskCovertToVO {

    public WorkTaskCovertToVO() {

    }

    public static List<WorkTaskPageVO> toWorkTaskPageVOList(List<WorkTask> workTasks) {
        if (CollectionUtils.isEmpty(workTasks)) {
            return new ArrayList<>();
        }
        List<WorkTaskPageVO> workTaskPageVOList = new ArrayList<>();
        for (WorkTask workTask : workTasks) {
            WorkTaskPageVO workTaskPageVO = new WorkTaskPageVO();
            workTaskPageVO.setId(workTask.getId());
            workTaskPageVO.setTaskName(workTask.getTaskName());
            workTaskPageVO.setTaskTime(workTask.getTaskTime());
            workTaskPageVO.setBusinessType(workTask.getBusinessType());
            workTaskPageVO.setBusinessTypeName(workTask.getBusinessTypeName());
            workTaskPageVO.setBusinessStatus(workTask.getBusinessStatus());
            workTaskPageVO.setBusinessStatusName(workTask.getBusinessStatusName());
            workTaskPageVOList.add(workTaskPageVO);
        }
        return workTaskPageVOList;
    }
}
