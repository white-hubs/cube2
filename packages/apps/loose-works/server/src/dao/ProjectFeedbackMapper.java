package com.guhecloud.cube.srv.gen.loose.works.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.AbnormalProjectReportVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectFeedbackPO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 工程反馈
 * @date 2023/9/14 18:34
 */
@Mapper
public interface ProjectFeedbackMapper extends BaseMapper<ProjectFeedbackPO> {
    Long abnormalProjectNum(@Param("period") Period period);

    List<AbnormalProjectReportVO> selectAbnormalProject(@Param("period") Period period);
}
