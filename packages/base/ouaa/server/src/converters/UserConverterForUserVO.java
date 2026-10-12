package com.guhecloud.cube.biz.ouaa.server.converters;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.server.dto.AppointmentDetailVO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Appointment;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class UserConverterForUserVO {
    private UserConverterForUserVO() {
    }

    public static User toUser(UserVO userVO) {
        return User.builder()
                .id(userVO.getId())
                .name(userVO.getName())
                .email(userVO.getEmail())
                .phone(null)
                .mobile(userVO.getMobile())
                .avatar(userVO.getAvatar())
                .gender(userVO.getGender())
                .workStatus(userVO.getWorkStatus())
                .remark(userVO.getRemark())
                .username(userVO.getUsername())
                .password(userVO.getPassword())
                .admin(userVO.getAdmin())
                .enabled(userVO.getEnabled())
                .entryDate(userVO.getEntryDate())
                .photographKey(userVO.getPhotographKey())
                .lastLoginTime(userVO.getLastLoginTime())
                .lastLoginAddress(null)
                .lastUpdatePasswordTime(null)
                .extension(null)
                .validity(null)
                .source(null)
                .idCard(userVO.getIdCard())
                .build();
    }

    public static List<User> toUsers(List<UserVO> userVOS) {
        return userVOS.stream().map(UserConverterForUserVO::toUser).toList();
    }

    public static UserVO toBaseUserVO(User user) {
        var result = new UserVO();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());
        result.setMobile(user.getMobile());
        result.setGender(user.getGender());
        result.setWorkStatus(user.getWorkStatus());
        result.setRemark(user.getRemark());
        result.setUsername(user.getUsername());
        result.setPassword(user.getPassword());
        result.setEnabled(user.getEnabled());
        result.setEntryDate(user.getEntryDate());
        result.setPhotographKey(user.getPhotographKey());
        result.setAvatar(user.getAvatar());
        result.setOrganizations(null);
        result.setManagedOrganizations(null);
        result.setOrganization(null);
        result.setPositions(null);
        result.setRoles(null);
        result.setFileVO(null);
        result.setAppointmentIds(null);
        result.setAppointmentList(null);
        result.setPhotographList(null);
        result.setTenants(null);
        result.setAdmin(user.getAdmin());
        result.setIdCard(user.getIdCard());
        result.setIsBindTel(null);
        result.setIsBindWechat(null);
        result.setWechatName(null);
        return result;
    }

    public static UserVO toCompletedUserVO(User user, List<UserVO.Organization> allLevelOrganizations,
                                           List<UserVO.Organization> managedOrganizations, List<UserVO.Position> positions,
                                           List<UserVO.Role> roles, FileVO avatarFile, List<Appointment> appointments,
                                           List<AppointmentDetailVO> appointmentDetails, List<FileVO> photographFiles,
                                           List<UserTenant> userTenants, boolean isBindTel, boolean isBindWechat, String wechatName
                                          ) {
        var baseResult = toBaseUserVO(user);
        baseResult.setOrganizations(allLevelOrganizations);
        if (allLevelOrganizations.getLast() != null) {
            baseResult.setOrganization(allLevelOrganizations.getLast());
        }
        baseResult.setManagedOrganizations(managedOrganizations);
        baseResult.setPositions(positions);
        baseResult.setRoles(roles);
        baseResult.setFileVO(avatarFile);
        baseResult.setAppointmentIds(appointments);
        baseResult.setAppointmentList(appointmentDetails);
        baseResult.setPhotographList(photographFiles);
        baseResult.setTenants(userTenants);
        baseResult.setIsBindTel(isBindTel);
        baseResult.setIsBindWechat(isBindWechat);
        baseResult.setWechatName(wechatName);
        return baseResult;
    }

    /**
     *
     * @param userVOs 从数据库查出来不完整的用户信息列表
     *
     * @return 补全字段的用户信息列表
     *
     * @deprecated 这里存在 UserVO 穿层问题，导致需要这样一个方法来扩展属于 DBDTO 的 entity，后续应修复之
     */
    @Deprecated
    public static void extendUserVOs(List<UserVO> userVOs, Map<Long, UserVOExtendInfo> extendInfoMap) {
        for (var userVO : userVOs) {
            var extendInfo = extendInfoMap.get(userVO.getId());
            if (extendInfo == null) {
                return;
            }
            userVO.setFileVO(extendInfo.avatarFile);
            userVO.setPositions(PositionConverterForUserVOPosition.toUserVOPositions(extendInfo.positions));
            userVO.setAppointmentList(extendInfo.appointmentDetails);
            userVO.setRoles(UserRoleDTOConverterForUserVORole.toUserVORoles(extendInfo.roles));
            userVO.setWorkStatus(extendInfo.workStatus);
            if (CollectionUtils.isEmpty(extendInfo.organizationPaths)) {
                userVO.setOrganizations(Collections.emptyList());
                userVO.setOrganization(null);
            } else {
                var userVOOrganizations = OrganizationConverterForUserVOOrg.toUserVOOrganizations(extendInfo.organizationPaths);
                userVO.setOrganizations(userVOOrganizations);
                userVO.setOrganization(userVOOrganizations.getLast());
            }

        }
    }

    public record UserVOExtendInfo(FileVO avatarFile, List<Organization> organizationPaths, List<Position> positions,
                                   List<AppointmentDetailVO> appointmentDetails, List<UserRoleDTO> roles, String workStatus) {}
}
