package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.dao.MeetingApplyPersonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.dao.MeetingMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.entity.MeetingEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.enums.MeetingPersonTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyPersonParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.service.MeetingService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyPersonVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, MeetingEntity> implements MeetingService {

    private final MeetingMapper meetingMapper;
    private final MeetingApplyPersonMapper meetingApplyPersonMapper;

    @Override
    public Pagination<MeetingPageVO> getPage(MeetingPageParam param) {
        Page<MeetingPageVO> recordPage = meetingMapper.getPage(PagingUtils.convert(param), param);
        Pagination<MeetingPageVO> result = PagingUtils.convert(recordPage);
        List<Long> businessKeys = result.getRecords().stream().map(MeetingPageVO::getId).collect(Collectors.toList());
        //会见、随行、被见人员
        List<MeetingApplyPersonVO> meetingApplyPersons = meetingApplyPersonMapper.selectByMeetingBusinessKey(MeetingApplyPersonParam
                                                                                                                     .builder()
                                                                                                                     .businessKeys(businessKeys)
                                                                                                                     .meetingPersonType(MeetingPersonTypeEnum.MEETING_PERSON.getCode())
                                                                                                                     .build());
        List<MeetingApplyPersonVO> accompanyingPersons = meetingApplyPersonMapper.selectByMeetingBusinessKey(MeetingApplyPersonParam
                                                                                                                     .builder()
                                                                                                                     .businessKeys(businessKeys)
                                                                                                                     .meetingPersonType(MeetingPersonTypeEnum.ACCOMPANYING_PERSON.getCode())
                                                                                                                     .build());
        List<MeetingApplyPersonVO> visitedPersons = meetingApplyPersonMapper.selectByMeetingBusinessKey(MeetingApplyPersonParam.builder()
                                                                                                                .businessKeys(businessKeys)
                                                                                                                .meetingPersonType(MeetingPersonTypeEnum.VISITED_PERSON.getCode())
                                                                                                                .build());

        result.getRecords().forEach(record -> {
            List<MeetingApplyPersonVO> meetingApplyPersonVOList = meetingApplyPersons.stream()
                    .filter(vo -> vo.getBusinessKey().equals(record.getId())).collect(Collectors.toList());
            List<MeetingApplyPersonVO> accompanyingPersonVOList = accompanyingPersons.stream()
                    .filter(vo -> vo.getBusinessKey().equals(record.getId())).collect(Collectors.toList());
            List<MeetingApplyPersonVO> visiblePersonVOList = visitedPersons.stream()
                    .filter(vo -> vo.getBusinessKey().equals(record.getId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(meetingApplyPersonVOList)) {
                record.setMeetingPersons(meetingApplyPersonVOList.stream().map(MeetingApplyPersonVO::getMeetingPersonName)
                                                 .collect(Collectors.joining(",")));
            }
            if (CollectionUtils.isNotEmpty(accompanyingPersonVOList)) {
                record.setAccompanyingPersonCount(accompanyingPersonVOList.size());
            }
            if (CollectionUtils.isNotEmpty(visiblePersonVOList)) {
                record.setVisitedPersons(visiblePersonVOList.stream().map(visiblePersonVO -> {
                    return visiblePersonVO.getMeetingPersonName() + " " + visiblePersonVO.getMeetingPersonCode();
                }).collect(Collectors.joining(",")));
            }
        });
        return result;
    }

    @Override
    public MeetingVO selectById(Long id) {
        MeetingEntity meetingEntity = meetingMapper.selectById(id);
        MeetingVO meetingVO = convertEntity2VO(meetingEntity);
        meetingVO.setMeetingPersons(meetingApplyPersonMapper.selectByMeetingBusinessKey(MeetingApplyPersonParam.builder().businessType(2)
                                                                                                .businessKey(meetingEntity.getId())
                                                                                                .build()));
        return meetingVO;
    }

    private MeetingVO convertEntity2VO(MeetingEntity meetingEntity) {
        if (meetingEntity != null) {
            return MeetingVO.builder()
                    .id(meetingEntity.getId())
                    .meetingType(meetingEntity.getMeetingType())
                    .meetingTypeName(meetingEntity.getMeetingTypeName())
                    .meetingReason(meetingEntity.getMeetingReason())
                    .meetingStartTime(meetingEntity.getMeetingStartTime())
                    .meetingEndTime(meetingEntity.getMeetingEndTime())
                    .thirdRecordId(meetingEntity.getThirdRecordId())
                    .build();
        }
        return new MeetingVO();
    }

    @Override
    public void insertOrUpdateMeeting(List<MeetingEntity> dataList) {
        meetingMapper.insertOrUpdateMeeting(dataList);
    }

    @Override
    public List<MeetingVO> getList(MeetingListParam param) {
        return meetingMapper.getList(param);
    }
}
