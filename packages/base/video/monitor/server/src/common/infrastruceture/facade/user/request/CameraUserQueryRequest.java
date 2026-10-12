package com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.user.request;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CameraUserQueryRequest extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 用户主键集合
     */
    private List<Long> userIds;

    /**
     * 过滤的用户主键集合
     */
    private List<Long> filterUserIds;

    /**
     * 组织结构主键
     */
    private Long organizationId;

    /**
     * 组织结构主键集合
     */
    private Set<Long> organizationIds;
}
