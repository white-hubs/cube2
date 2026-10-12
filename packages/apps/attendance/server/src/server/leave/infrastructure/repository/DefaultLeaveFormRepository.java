package com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.repository;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.repository.LeaveFormRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.mapper.LeaveFormMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.po.LeaveFormPO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

import static com.alibaba.fastjson2.JSONWriter.Feature.WriteClassName;

/**
 * @author pengjian
 * @date 2022/11/18 19:57
 * @description
 */
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultLeaveFormRepository implements LeaveFormRepository {

    private final LeaveFormMapper leaveFormMapper;

    @Override
    public Long save(LeaveForm leaveForm) {
        LeaveFormPO leaveFormPO = LeaveFormPO.builder()
                .id(leaveForm.getId())
                .leaveRule(JSONObject.toJSONString(leaveForm.getLeaveRule(), WriteClassName))
                .leaveFormDetail(JSONObject.toJSONString(leaveForm))
                .status(leaveForm.getStatus())
                .leaveEndTime(leaveForm.getDetail().getLeaveTime().getPeriod().getEnd())
                .leaveStartTime(leaveForm.getDetail().getLeaveTime().getPeriod().getBegin())
                .build();
        LeaveFormPO existLeaveFormPO = leaveFormMapper.selectById(leaveForm.getId());
        if (Objects.nonNull(existLeaveFormPO)) {
            existLeaveFormPO.setLeaveFormDetail(leaveFormPO.getLeaveFormDetail());
            existLeaveFormPO.setLeaveRule(leaveFormPO.getLeaveRule());
            existLeaveFormPO.setStatus(leaveFormPO.getStatus());
            leaveFormMapper.updateById(existLeaveFormPO);
        } else {
            leaveFormMapper.insert(leaveFormPO);
        }
        return leaveForm.getId();
    }

    @Override
    public Optional<LeaveForm> getById(Long id) {
        LeaveFormPO leaveFormPO = leaveFormMapper.selectById(id);
        if (Objects.isNull(leaveFormPO)) {
            return Optional.empty();
        }

        LeaveForm leaveForm = JSONObject.parseObject(leaveFormPO.getLeaveFormDetail(), LeaveForm.class);
        LeaveRule leaveRule = JSONObject.parseObject(leaveFormPO.getLeaveRule(), LeaveRule.class);
        leaveForm.setLeaveRule(leaveRule);
        return Optional.of(leaveForm);
    }
}
