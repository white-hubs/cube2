package com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzy
 * @date 2022/5/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeConfig extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private Long businessKey;

    /**
     * 通知方式
     */
    private String way;

    /**
     * 设备文档
     */
    @TableField("`user`")
    private String user;

    /**
     * 指定部门
     */
    private String department;

    /**
     * 指定岗位
     */
    private String post;
    /**
     * 指定角色
     */
    private String role;

}
