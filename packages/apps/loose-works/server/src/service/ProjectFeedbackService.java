package com.guhecloud.cube.srv.gen.loose.works.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.AbnormalProjectReportVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectFeedbackVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectFeedbackPO;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

/**
 * @author wql
 * @describe 工程反馈
 * @date 2023/9/14 18:41
 */
public interface ProjectFeedbackService extends IService<ProjectFeedbackPO> {
    List<ProjectDetailVO.FeedbackDetail> getFeedbackDetailList(Long scheduleId, String feedbackType);

    List<ProjectDetailVO.FeedbackDetail> getFeedbackDetailAll(Long scheduleId, String feedbackType);

    List<ProjectFeedbackPO> getFeedbackList(Long scheduleId, String feedbackType);

    ProjectFeedbackVO getFeedBackById(Long id);

    void saveFeedback(ProjectFeedbackPO po);

    List<ProjectFeedbackPO> getCheckAcceptList(Long id, String name);

    Long abnormalProjectNum(Period period);

    List<AbnormalProjectReportVO> selectAbnormalProject(Period period);
}
