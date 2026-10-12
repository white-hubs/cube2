package com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupStaffCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceGroupValidator {

    private final AttendanceGroupMapper attendanceGroupMapper;
    private final AttendanceGroupStaffMapper staffMapper;

    /**
     * 验证考勤组名称是否相同
     *
     * @param id   考勤组ID
     * @param name 名称
     */
    public void verifyIsExistByName(Long id, String name) {
        AttendanceGroupPO po = attendanceGroupMapper.selectOne(Wrappers.<AttendanceGroupPO>lambdaQuery()
                                                                       .eq(AttendanceGroupPO::getName, name)
                                                                       .ne(null != id, AttendanceGroupPO::getId, id)
                                                              );
        Assert.isConflict(Objects.nonNull(po), "存在相同名称!");
    }

    /**
     * 验证考勤组人员是否重复
     * 1、如果有适用全员的考勤组，那么不能再建其他考勤组
     * 2、如果已经存在其他考勤组，那么不能再建全员考勤组
     * 2、如果适用部分人员，那么人员不能存在于两个考勤组
     *
     * @param command 考勤组
     */
    public void verifyIsExistByStaff(SubmitAttendanceGroupCommand command) {
        Long id = command.getId();
        // 1、
        Assert.isConflict(this.verifyHaveAllStaffGroup(command.getType(), id), "已经存在全员考勤组，不能再建其他考勤组");
        // 2、
        if (AttendanceGroupRangesEnum.All_STAFF.name().equals(command.getRanges())) {
            List<AttendanceGroupPO> groupPOS = attendanceGroupMapper.selectList(Wrappers.<AttendanceGroupPO>lambdaQuery()
                                                                                        .eq(AttendanceGroupPO::getType, command.getType())
                                                                                        .ne(null != id, AttendanceGroupPO::getId, id)
                                                                               );
            Assert.isConflict(CollectionUtils.isNotEmpty(groupPOS), "已经存在其他考勤组，不能再建全员考勤组");
        }
        // 3、
        List<SubmitAttendanceGroupStaffCommand> staffPOS = command.getStaffPOS();
        if (CollectionUtils.isNotEmpty(staffPOS)) {
            Set<Long> staffIds = staffPOS.stream().map(SubmitAttendanceGroupStaffCommand::getStaffId).collect(Collectors.toSet());
            int groupNum = staffMapper.theStaffIdsHaveManyGroup(command.getId(), command.getType(), staffIds);
            Assert.isConflict(groupNum > 0, "所选员工部分已经存在于其他考勤组，不能重复");
        }
    }

    /**
     * 是否有全员考勤组
     *
     * @param groupType 考勤组类型
     * @param groupId   考勤组ID
     *
     * @return
     */
    public boolean verifyHaveAllStaffGroup(String groupType, Long groupId) {
        AttendanceGroupPO groupPO = attendanceGroupMapper.selectOne(Wrappers.<AttendanceGroupPO>lambdaQuery()
                                                                            .eq(AttendanceGroupPO::getType, groupType)
                                                                            .eq(AttendanceGroupPO::getRanges, AttendanceGroupRangesEnum.All_STAFF.name())
                                                                            .ne(null != groupId, AttendanceGroupPO::getId, groupId)
                                                                   );
        return !Objects.isNull(groupPO);
    }

}
