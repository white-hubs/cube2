package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 部门加岗位
 *
 * @author lanchun
 * @date 2022/10/31 16:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDepartmentPostDTO {

    /**
     * 部门主键
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 岗位主键
     */
    private Long postId;

    /**
     * 岗位名称
     */
    private String postName;
}
