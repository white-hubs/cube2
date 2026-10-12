package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.entity.MeetingApplyPersonEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyPersonParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyPersonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingApplyPersonMapper extends BaseMapper<MeetingApplyPersonEntity> {
    List<MeetingApplyPersonVO> selectByMeetingBusinessKey(@Param("param") MeetingApplyPersonParam param);

    void insertOrUpdateMeetingApplyPerson(List<MeetingApplyPersonEntity> dataList);

}
