package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考勤组人员
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttGroupStaffCommandService extends ServiceImpl<AttGroupStaffMapper, AttGroupStaffPO> {
    private final UserClient userClient;
    private final AttGroupStaffMapper attGroupStaffMapper;

    /**
     * 更新考勤组人员信息
     *
     * @param groupPO           考勤组信息
     * @param staffPOList       添加的人员
     * @param removeStaffIdList 移除的人员
     */
    public void renewGroupStaff(AttGroupPO groupPO, List<AttGroupStaffParam> staffPOList, List<Long> removeStaffIdList) {
        if (groupPO == null) {
            return;
        }
        /* 移除 */
        if (removeStaffIdList != null && !removeStaffIdList.isEmpty()) {
            attGroupStaffMapper.update(null, Wrappers.<AttGroupStaffPO>lambdaUpdate()
                                               .in(AttGroupStaffPO::getStaffId, removeStaffIdList)
                                               .set(AttGroupStaffPO::getDeleted, DateUtils.toTimestamp(LocalDateTime.now()))
                                      );
        }
        /* 更新规则id */
        attGroupStaffMapper.update(null, Wrappers.<AttGroupStaffPO>lambdaUpdate()
                                           .set(AttGroupStaffPO::getSchedulingRuleId, groupPO.getSchedulingRuleId())
                                           .set(AttGroupStaffPO::getClockRuleId, groupPO.getClockRuleId())
                                           .eq(AttGroupStaffPO::getGroupId, groupPO.getId())
                                  );

        /* 添加 */
        if (!AttGroupRangesEnum.ALL.name().equals(groupPO.getRanges()) && (staffPOList == null || staffPOList.isEmpty())) {
            return;
        }
        List<AttGroupStaffPO> inserList = new ArrayList<>();
        if (AttGroupRangesEnum.ALL.name().equals(groupPO.getRanges())) {
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setPageNumber(1);
            userQueryParam.setPageSize(999);
            userQueryParam.setEnabled(true);
            Pagination<UserDTO> pagination = userClient.listUserByTenant(SessionContextHolder.getRequester().getTenant(), userQueryParam);
            if (pagination.getTotal() > AttConstants.zeroInt) {
                for (UserDTO user : pagination.getRecords()) {
                    AttGroupStaffPO po = new AttGroupStaffPO();
                    po.setId(IdWorker.getId());
                    po.setGroupId(groupPO.getId());
                    po.setGroupName(groupPO.getName());
                    po.setStaffId(user.getId());
                    po.setName(user.getName());
                    po.setMobile(user.getMobile());
                    po.setTenant(groupPO.getTenant());
                    po.setSchedulingRuleId(groupPO.getSchedulingRuleId());
                    po.setClockRuleId(groupPO.getClockRuleId());
                    po.setEntryDate(user.getEntryDate());
                    po.setDeleted(false);
                    UserDTO.Organization organization = user.getOrganization();
                    if (organization != null) {
                        po.setDeptId(organization.getId());
                        po.setDeptName(organization.getName());
                    }
                    inserList.add(po);
                }
            }
        } else if (staffPOList != null && !staffPOList.isEmpty()) {
            Map<Long, UserDTO> userDTOMap = new HashMap<>();
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setPageNumber(1);
            userQueryParam.setPageSize(999);
            userQueryParam.setEnabled(true);
            Pagination<UserDTO> pagination = userClient.listUserByTenant(SessionContextHolder.getRequester().getTenant(), userQueryParam);
            if (pagination.getTotal() > AttConstants.zeroLong) {
                for (UserDTO userDTO : pagination.getRecords()) {
                    userDTOMap.put(userDTO.getId(), userDTO);
                }
            }
            for (AttGroupStaffParam attGroupStaffParam : staffPOList) {
                UserDTO u = userDTOMap.get(attGroupStaffParam.getStaffId());
                AttGroupStaffPO po = new AttGroupStaffPO();
                BeanUtils.copyProperties(attGroupStaffParam, po);
                AttPoAttributeSet.initAddBasePoAttribute(po);
                po.setGroupName(groupPO.getName());
                po.setId(IdWorker.getId());
                po.setGroupId(groupPO.getId());
                po.setSchedulingRuleId(groupPO.getSchedulingRuleId());
                po.setClockRuleId(groupPO.getClockRuleId());
                po.setEntryDate(u.getEntryDate());
                inserList.add(po);
            }

        }
        if (inserList.isEmpty()) {
            return;
        }
        attGroupStaffMapper.update(null, Wrappers.<AttGroupStaffPO>lambdaUpdate()
                                           .eq(AttGroupStaffPO::getGroupId, groupPO.getId())
                                           .in(AttGroupStaffPO::getStaffId, inserList.stream().map(AttGroupStaffPO::getStaffId).collect(Collectors.toSet()))
                                           .set(AttGroupStaffPO::getDeleted, DateUtils.toTimestamp(LocalDateTime.now()))
                                  );
        this.saveBatch(inserList, AttConstants.bachSize);
    }
}
