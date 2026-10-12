package com.guhecloud.cube.srv.gen.loose.works.dto.request.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-08  14:11
 * @Description:
 * @Version: 1.0
 */
@Data
public class ProjectScheduleParam {

    private Long id;
    /**
     * 请示id
     */
    private Long applyId;
    /**
     * 签约id
     */
    private Long contractId;
    /**
     * 发起人
     */
    private Long startUser;
    /**
     * 工单编号
     */
    private String projectNum;
    /**
     * 小散工程名称
     */
    @NotEmpty(message = "项目名称不能为空")
    private String projectName;
    /**
     * 乙方
     */
    private String partyB;
    /**
     * 完成时限
     */
    private LocalDateTime completionTime;
    /**
     * 验收通过时间
     */
    private LocalDateTime overTime;
    /**
     * 工程对接人
     */

    private String superviseUsers;
    /**
     * 工程对接人名称
     */
    private String superviseUserName;
    /**
     * 监管部门
     */
    private String superviseDep;
    /**
     * 工程状态
     */
    private String status;
    /**
     * 进度
     */
    private Integer schedule;
    /**
     * 施工地点
     */
    private String constructionSite;
    /**
     * 工程类型
     */
    @NotNull(message = "工程类型不能为空")
    private Long typeId;
    /**
     * 工程备注
     */
    private String remarks;
    /**
     * 工程详细资料
     */
    private String projectFiles;

    /**
     * 工期
     */
    private String duration;

    /**
     * 施工方联系方式
     */
    @Size(max = 100, message = "施工方联系方式100")
    private String partyBMobile;

    /**
     * 档位号
     */
    @Size(max = 100, message = "档位号100")
    private String stallCode;

    /**
     * 档位联系人
     */
    @Size(max = 100, message = "档位联系人100")
    private String stallLiaison;

    /**
     * 档位联系方式
     */
    @Size(max = 100, message = "档位联系方式100")
    private String stallMobile;

    /**
     * 安全对接人
     */
    private String safetyDockUserId;

    /**
     * 建设方
     */
    private String constructB;
}
