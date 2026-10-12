package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 用户查询参数
 *
 * @author Woody
 * @date 2021/7/20
 */
@Data
public class UserQueryParam extends Paging {

    /**
     * 用户主键列表
     */
    private List<Long> ids;

    /**
     * 优先排序的组织机构主键
     */
    private Long sortId;

    /**
     * 过滤的用户主键集合
     */
    private List<Long> filterUserIds;

    /**
     * 用户姓名
     */
    @Size(max = 100, message = "用户姓名长度不能大于100")
    private String keyword;

    /**
     * 性别
     */
    private String gender;

    /**
     * 入职日期
     */
    private Period entryDate;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 组织机构主键
     */
    @Min(value = 1, message = "组织机构主键不能小于1")
    private Long organizationId;

    /**
     * 是否是管理员
     */
    private Boolean isAdmin;

    /**
     * 工作状态:WORKING 在岗 REST 休息
     */
    private String workStatus;

    /**
     * 部门id
     * 多选逗号拼接
     */
    private String positionId;

    /**
     * 角色Id
     * 多选逗号拼接
     */
    private String roleId;
    /**
     * 岗位id
     */
    private Long appointmentId;
    /**
     * 创建时间
     */
    private Period createTime;
    /**
     * 来源
     */
    private String source;
}
