package com.guhecloud.cube.srv.gen.loose.works.service;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectStatisticsQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectApplyListVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectSignContractPO;
import com.guhecloud.platform.base.model.Pagination;
import instance.event.ProcessCompletedMessage;
import instance.event.ProcessStartedMessage;

import java.util.List;

/**
 * @author wql
 * @describe 工程签约
 * @date 2023/9/14 18:44
 */
public interface ProjectSignContractService {
    /**
     * @Author wql
     * @Description 统计签约中的数量
     * @Date 10:00 2023/9/15
     **/
    Long getUnderContractNum(ProjectStatisticsQuery query);

    ProjectDetailVO.ApplyDetail getContractByApply(Long id);

    Pagination<ProjectApplyListVO> projectSignContractList(ProcessInstancePageQuery processInstancePageQuery);

    ProjectSignContractPO addSignContract(ProcessStartedMessage message);

    void signContractCompleted(ProcessCompletedMessage message);

    void deleteProjectSignContractBatch(List<String> processInstanceIds);
}
