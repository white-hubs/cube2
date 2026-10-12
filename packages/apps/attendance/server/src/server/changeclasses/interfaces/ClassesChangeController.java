package com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.interfaces;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway.ClassesChangeSituationCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway.command.ClassesChangeCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.po.ClassesChangeSituationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.querygateway.ClassesChangeQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.vo.params.ChangeClassesParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.FormFieldVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.util.CustBeanUtil;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.AttendanceGroupQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.StaffQuery;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 换班申请 接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassesChangeController {
    private final ClassesChangeSituationCommandService classesChangeSituationCommandService;
    private final AttendanceGroupQueryService attendanceGroupQueryService;
    private final ClassesChangeQueryService classesChangeQueryService;
    private final FlowClient flowClient;
    private final StaffClient staffClient;

    /**
     * 查询换班人 相同考勤组的人
     *
     * @return
     */
    @GetMapping("/work/usersV0")
    public List<FormFieldVO> getWorkClasses() {
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        List<AttendanceGroupStaffPO> users = attendanceGroupQueryService.getAllUsersInMyAttendanceGroup(currentUserId);
        if (!CollectionUtils.isEmpty(users)) {
            return users.stream().map(u -> FormFieldVO.builder().id(u.getStaffId().toString()).name(u.getStaffName()).build())
                    .collect(Collectors.toList());
        } else {
            //查询是否有所有人的考勤组
            List<AttendanceGroupPO> allGroups = attendanceGroupQueryService.list();
            AttendanceGroupPO allStaffGroup = allGroups.stream()
                    .filter(g -> AttendanceGroupRangesEnum.All_STAFF.name().equals(g.getRanges())).findFirst().orElse(null);
            if (allStaffGroup != null) {
                List<StaffDetailDTO> allStaff = staffClient.getList(StaffQuery.builder().build());
                return allStaff.stream().map(u -> FormFieldVO.builder().id(u.getId().toString()).name(u.getName()).build())
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    /**
     * 获取换班表单 配置字段
     *
     * @return
     */
    @GetMapping("/work/classes/form/field")
    public List<FormFieldVO> getClassesChangeFormField() {
        return CustBeanUtil.getFormField(ClassesChangeSituationPO.class);
    }

    /**
     * 查询换班人
     * 2023-07-21 跨考勤组、部门换班
     * 且排班的考勤组也需要换班
     *
     * @return
     */
    @GetMapping("/work/users")
    public List<FormFieldVO> getWorkUsers(ChangeClassesParam param) {
        return classesChangeQueryService.getWorkUsers(param);
    }

    /**
     * 查询人员班次 专用于换班申请
     * 支持 1.班休互换，2.自己和自己换
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/work/classes")
    public List<FormFieldVO> getWorkClasses(ChangeClassesParam param) {
        return classesChangeQueryService.getWorkClasses(param);
    }

    /**
     * 检查调换班次 是否符合要求
     *
     * @return
     */
    @PostMapping("/work/classes/change/check")
    public Boolean classesChangeCheck(@RequestBody FormDataParam param) {
        String biz = flowClient.formParse(param);
        ClassesChangeCommand command = JSONObject.parseObject(biz, ClassesChangeCommand.class);
        return classesChangeQueryService.checkClassesChange(command);
    }

    /**
     * 添加换班记录并执行换班
     *
     * @param command
     */
    @PostMapping("/work/classes/change")
    public void addClassesChangeSituationAndChangeClasses(@RequestBody ClassesChangeCommand command) {
        classesChangeSituationCommandService.addClassesChangeSituationAndChangeClasses(command);
    }

}
