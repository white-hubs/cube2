package com.guhecloud.cube.srv.gen.loose.works.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectReformQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.BaseQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectReformAnalysisVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectReformVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectReformPO;
import com.guhecloud.platform.base.model.Pagination;
import instance.event.ProcessCompletedMessage;
import instance.event.ProcessStartedMessage;

import jakarta.validation.Valid;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/12 11:52
 * @Description: 工程整改业务服务
 */
public interface ProjectReformService extends IService<ProjectReformPO> {

    /**
     * 获取整改单列表
     *
     * @param processInstancePageQuery
     *
     * @return
     */
    Pagination<ProjectReformVO> projectReformList(@Valid ProjectReformQueryParam processInstancePageQuery);

    /**
     * 发起整改
     *
     * @param message
     * @param instanceName
     */
    ProjectReformPO addProjectReform(ProcessStartedMessage message, String instanceName);

    /**
     * 整改完成
     *
     * @param message
     */
    void reformCompleted(ProcessCompletedMessage message);

    /**
     * 工程整改记录统计
     *
     * @param query
     *
     * @return
     */
    ProjectReformAnalysisVO getProjectReformAnalysis(BaseQuery query);

    Boolean deleteProjectReformBatch(List<String> processInstanceIds);
}
