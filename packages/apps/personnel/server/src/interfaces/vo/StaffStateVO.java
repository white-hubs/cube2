package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.GenderEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.WorkingStatus;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/11/08
 * @description 员工花名册
 */
@Data
public class StaffStateVO {

    private Long id;

    private Long userId;

    private String gender;

    /**
     * 姓名
     */
    private String name;
    private String firstLetter;
    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 部门ID
     */
    private Long organizationId;

    /**
     * 部门名称
     */
    private String organizationName;

    private String positionIds;

    private List<String> postIds;

    /**
     * 岗位名称
     */
    private String postNames;

    /**
     * 工作状态
     */
    private String state;

    private String stateName;

    private Boolean onDuty;

    public String getStateName() {
        if (StringUtils.isNotEmpty(this.state)) {
            return WorkingStatus.valueOf(this.state).getValue();
        }
        return null;
    }

    public void setGender(String gender) {
        this.gender = StringUtils.isEmpty(gender) ? "" : GenderEnum.valueOf(gender).getValue();
    }

    public List<Long> getPostIds() {
        if (StringUtils.isNotEmpty(this.positionIds)) {
            String[] split = this.positionIds.split(",");
            List<String> list = Arrays.asList(split);
            return list.parallelStream().map(Long::parseLong).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}