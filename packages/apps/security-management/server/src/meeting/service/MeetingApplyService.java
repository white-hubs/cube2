package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.entity.MeetingApplyEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface MeetingApplyService extends IService<MeetingApplyEntity> {

    /**
     * 分页查询会议申请列表
     */
    Pagination<MeetingApplyPageVO> getPage(MeetingApplyPageParam param);

    /**
     * 根据id查询详情
     *
     * @param id id
     */
    MeetingApplyVO selectById(Long id);

    void insertOrUpdateMeetingApply(List<MeetingApplyEntity> dataList);

    List<MeetingApplyVO> getList(MeetingApplyListParam param);
}
