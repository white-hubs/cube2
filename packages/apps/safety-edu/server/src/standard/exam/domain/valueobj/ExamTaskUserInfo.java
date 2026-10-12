package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考试用户信息
 *
 * @author lanchun
 * @date 2023/4/19 15:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTaskUserInfo {

    private Long id;

    /**
     * 唯一标识
     */
    private String identify;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户电话
     */
    private String phone;

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
