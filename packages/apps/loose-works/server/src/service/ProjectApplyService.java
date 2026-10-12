package com.guhecloud.cube.srv.gen.loose.works.service;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.BaseQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectStatisticsQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectApplyListVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectNodeInfo;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.projectNameListVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectApplyPO;
import com.guhecloud.platform.base.model.Pagination;
import instance.event.ProcessCompletedMessage;
import instance.event.ProcessStartedMessage;

import java.util.List;

/**
 * @author wql
 * @describe 工程申请
 * @date 2023/9/14 18:39
 */
public interface ProjectApplyService {
    ProjectStatisticsVO projectStatistics(ProjectStatisticsQuery query);

    ProjectDetailVO projectDetail(Long id);

    Pagination<ProjectApplyListVO> projectApplyList(ProcessInstancePageQuery processInstancePageQuery);

    ProjectApplyPO addProjectApply(ProcessStartedMessage message);

    void approveCompleted(ProcessCompletedMessage message);

    List<projectNameListVO> projectNameList();

    Pagination<ProjectNodeInfo> projectNodeInfoList(BaseQuery query);

    void deleteProjectApplyBatch(List<String> processInstanceIds);
}
