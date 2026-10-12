package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po;

import annotation.FormField;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2023/02/28
 * @description 轮岗申请表
 */
@Data
public class RotationPostPO {

    @FormField(label = "拟轮岗部门")
    private List<String> rotationOrganizationIds;

    @FormField(label = "现所在部门")
    private List<String> organizationId;

    @FormField(label = "轮岗员工")
    private List<String> staffIds;

    @FormField(label = "轮岗时间")
    private String time;

    /**
     * 轮岗开始时间
     */
    private LocalDateTime startDate;

    /**
     * 轮岗结束时间
     */
    private LocalDateTime endDate;

    private Long flowFormDataId;

    private Long staffId;

    private Long rotationOrganizationId;

    private Integer tenant;

    // 处理时间
    public void parseStartAndEnd() {
        List<LocalDateTime> startAndEnd = JsonUtils.deserialize(this.time, new TypeReference<>() {});
        this.startDate = DateUtils.parseDate(DateUtils.formatDate(startAndEnd.get(0)));
        this.endDate = DateUtils.parseDate(DateUtils.formatDate(startAndEnd.get(1)));
    }

    public Long getStaffId() {
        if (!CollectionUtils.isEmpty(this.staffIds)) {
            return Long.valueOf(this.staffIds.get(0));
        }
        return staffId;
    }

    public Long getRotationOrganizationId() {
        if (!CollectionUtils.isEmpty(this.rotationOrganizationIds)) {
            return Long.valueOf(this.rotationOrganizationIds.get(0));
        }
        return rotationOrganizationId;
    }

    public Long getOrganizationId() {
        if (!CollectionUtils.isEmpty(this.organizationId)) {
            return Long.valueOf(this.organizationId.get(0));
        }
        return rotationOrganizationId;
    }
}