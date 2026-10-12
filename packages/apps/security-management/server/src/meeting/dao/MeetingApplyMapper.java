package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.entity.MeetingApplyEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingApplyMapper extends BaseMapper<MeetingApplyEntity> {

    Page<MeetingApplyPageVO> getPage(Page page, @Param("param") MeetingApplyPageParam param);

    void insertOrUpdateMeetingApply(List<MeetingApplyEntity> dataList);

    List<MeetingApplyVO> getList(@Param("param") MeetingApplyListParam param);
}
