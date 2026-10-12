package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.dao.MeetingApplyMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.dao.MeetingApplyPersonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.entity.MeetingApplyEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.enums.MeetingPersonTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyPersonParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.service.MeetingApplyService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyPersonVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeetingApplyServiceImpl extends ServiceImpl<MeetingApplyMapper, MeetingApplyEntity> implements MeetingApplyService {

    private final MeetingApplyMapper meetingApplyMapper;
    private final MeetingApplyPersonMapper meetingApplyPersonMapper;

    @Override
    public Pagination<MeetingApplyPageVO> getPage(MeetingApplyPageParam param) {
        Page<MeetingApplyPageVO> recordPage = meetingApplyMapper.getPage(PagingUtils.convert(param), param);
        Pagination<MeetingApplyPageVO> result = PagingUtils.convert(recordPage);
        List<Long> businessKeys = result.getRecords().stream().map(MeetingApplyPageVO::getId).collect(Collectors.toList());
        //会见、随行、被见人员
        List<MeetingApplyPersonVO> meetingApplyPersons = meetingApplyPersonMapper.selectByMeetingBusinessKey(MeetingApplyPersonParam
                                                                                                                     .builder()
                                                                                                                     .businessKeys(businessKeys)
                                                                                                                     .businessType(1)
                                                                                                                     .meetingPersonType(MeetingPersonTypeEnum.MEETING_PERSON.getCode())
                                                                                                                     .build());
        List<MeetingApplyPersonVO> accompanyingPersons = meetingApplyPersonMapper.selectByMeetingBusinessKey(MeetingApplyPersonParam
                                                                                                                     .builder()
                                                                                                                     .businessKeys(businessKeys)
                                                                                                                     .businessType(1)
                                                                                                                     .meetingPersonType(MeetingPersonTypeEnum.ACCOMPANYING_PERSON.getCode())
                                                                                                                     .build());
        List<MeetingApplyPersonVO> visitedPersons = meetingApplyPersonMapper.selectByMeetingBusinessKey(MeetingApplyPersonParam.builder()
                                                                                                                .businessKeys(businessKeys)
                                                                                                                .businessType(1)
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
    public MeetingApplyVO selectById(Long id) {
        MeetingApplyEntity meetingApplyEntity = meetingApplyMapper.selectById(id);
        MeetingApplyVO meetingApplyVO = convertEntity2VO(meetingApplyEntity);
        meetingApplyVO.setMeetingPersons(meetingApplyPersonMapper.selectByMeetingBusinessKey(MeetingApplyPersonParam.builder()
                                                                                                     .businessType(1)
                                                                                                     .businessKey(meetingApplyEntity.getId())
                                                                                                     .build()));
        return meetingApplyVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOrUpdateMeetingApply(List<MeetingApplyEntity> dataList) {
        meetingApplyMapper.insertOrUpdateMeetingApply(dataList);
    }

    @Override
    public List<MeetingApplyVO> getList(MeetingApplyListParam param) {
        return meetingApplyMapper.getList(param);
    }

    private MeetingApplyVO convertEntity2VO(MeetingApplyEntity meetingEntity) {
        if (meetingEntity != null) {
            return MeetingApplyVO.builder()
                    .id(meetingEntity.getId())
                    .applyTime(meetingEntity.getApplyTime())
                    .applyPersonId(meetingEntity.getApplyPersonId())
                    .applyPersonName(meetingEntity.getApplyPersonName())
                    .meetingType(meetingEntity.getMeetingType())
                    .meetingTypeName(meetingEntity.getMeetingTypeName())
                    .meetingReason(meetingEntity.getMeetingReason())
                    .meetingStartTime(meetingEntity.getMeetingStartTime())
                    .meetingEndTime(meetingEntity.getMeetingEndTime())
                    .approvalResult(meetingEntity.getApprovalResult())
                    .approvalResultReason(meetingEntity.getApprovalResultReason())
                    .thirdRecordId(meetingEntity.getThirdRecordId())
                    .build();
        }
        return new MeetingApplyVO();
    }
}
