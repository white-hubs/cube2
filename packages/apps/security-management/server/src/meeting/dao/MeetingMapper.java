package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.entity.MeetingEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingMapper extends BaseMapper<MeetingEntity> {

    Page<MeetingPageVO> getPage(Page page, @Param("param") MeetingPageParam param);

    void insertOrUpdateMeeting(List<MeetingEntity> dataList);

    List<MeetingVO> getList(@Param("param") MeetingListParam param);
}
