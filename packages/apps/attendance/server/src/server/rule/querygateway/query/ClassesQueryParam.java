package com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class ClassesQueryParam extends Paging {

    private String keyword;

    /**
     * 如果是false只查询全体休息的数据(空和true这个条件都没有使用)
     */
    private Boolean hasRest = true;

    /**
     * 应用部门
     */
    private Long organizationId;

    /**
     * 获取该考勤组的
     */
    private Long groupId;

    private List<Long> classIdList;

}
