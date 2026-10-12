package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.entity.MeetingEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface MeetingService extends IService<MeetingEntity> {

    /**
     * 分页查询会议申请列表
     */
    Pagination<MeetingPageVO> getPage(MeetingPageParam param);

    /**
     * 根据id查询详情
     *
     * @param id id
     */
    MeetingVO selectById(Long id);

    void insertOrUpdateMeeting(List<MeetingEntity> dataList);

    List<MeetingVO> getList(MeetingListParam param);
}
