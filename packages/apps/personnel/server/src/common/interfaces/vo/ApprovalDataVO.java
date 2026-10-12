package com.guhecloud.cube.srv.gen.personnel.management.server.common.interfaces.vo;

import com.guhecloud.cube.srv.gen.personnel.management.server.common.enumeration.AssessResultEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2023/02/17
 * @description
 */
@Data
public class ApprovalDataVO {

    private String id;

    /**
     * 岗位ID
     */
    private String postId;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 部门名称
     */
    private String organizationName;
    private String organizationId;

    /**
     * 员工名称
     */
    private String staffName;

    /**
     * 数量
     */
    private int num;

    /**
     * 优秀人数
     */
    private int outstandingNum;

    /**
     * 合格人数
     */
    private int qualifiedNum;

    /**
     * 不合格人数
     */
    private int unqualifiedNum;

    /**
     * 绩效考核分数
     */
    private String score;

    /**
     * 绩效考核等级
     */
    private AssessResultEnum grade;

    /**
     * 考核时间
     */
    private String assessTime;

    private LocalDateTime time;

    /**
     * 拟轮岗部门
     */
    private String rotationOrganizationName;
    private Long rotationOrganizationId;

    /**
     * 轮岗时间
     */
    private String rotationTime;

    /**
     * 申请人
     */
    private String applicantName;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 处理时间
     */
    private LocalDateTime applyDoneTime;

    public String getGrade() {
        if (grade != null) {
            return grade.getValue();
        }
        return "";
    }
}