package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户更新参数
 *
 * @author Woody
 * @date 2021/7/20
 */
@Data
public class UserModifyParam {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户姓名
     */
    @NotEmpty(message = "用户姓名不能为空")
    @Size(max = 100, message = "用户姓名长度不能大于100")
    private String name;

    /**
     * 联系电话
     */
    @Size(max = 100, message = "用户手机号长度不能大于100")
    private String mobile;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 用户性别
     */
    private Gender gender;

    /**
     * 用户备注
     */
    @Size(max = 200, message = "用户备注长度不能大于200")
    private String remark;
    @Size(max = 100, message = "用户账号长度不能大于100")
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled = true;

    /**
     * 入职日期
     */
    private Date entryDate;

    /**
     * 组织机构主键
     */
    private List<Long> organizationIds;

    /**
     * 管理的部门
     */
    private Set<Long> managedOrganizationIds;

    /**
     * 岗位主键列表
     */
    private List<Long> positionIds;

    /**
     * 角色主键列表
     */
    private List<Long> roleIds;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 原来的照片文件id集合（用来判断是否更改照片
     */
    private List<Long> oldPhotographIdList;

    /**
     * 原来的名称（用来判断是否更新人脸对比照片
     */
    private String oldName;

    /**
     * 照片文件id集合
     */
    private List<Long> photographIdList;
    /**
     * 职位ID
     */
    private List<Long> appointmentIds;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * client调用不发送消息
     */
    private Boolean sendMessage = true;

    /**
     * 账号来源
     */
    private String source;

    public List<UserOrganization> getUserOrg(Long userId) {
        List<UserOrganization> userSaveOrganizations = new ArrayList<>();
        if (ObjectUtils.isEmpty(appointmentIds) && ObjectUtils.isEmpty(organizationIds) && ObjectUtils.isEmpty(positionIds)) {
            return null;
        }
        int max = organizationIds.size();
        if (ObjectUtils.isNotEmpty(appointmentIds)) {
            max = appointmentIds.size();
        }
        if (ObjectUtils.isNotEmpty(positionIds)) {
            max = max < positionIds.size() ? positionIds.size() : max;
        }
        for (int i = 0; i < max; i++) {
            UserOrganization organization = new UserOrganization();
            organization.setUserId(userId);
            if (ObjectUtils.isNotEmpty(organizationIds)) {
                organization.setOrganizationId(organizationIds.get(organizationIds.size() - 1));
            }
            if (ObjectUtils.isNotEmpty(positionIds) && positionIds.size() > i) {
                organization.setPositionId(positionIds.get(i));
            }
            if (ObjectUtils.isNotEmpty(appointmentIds) && appointmentIds.size() > i) {
                organization.setAppointmentId(appointmentIds.get(i));
            }
            userSaveOrganizations.add(organization);
        }
        return userSaveOrganizations;
    }
}
