package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 工程进度详情
 * @date 2023/9/14 19:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectScheduleDetailVO {
    /**
     * 进度id
     */
    private Long id;
    /**
     * 工程编号
     */
    private String projectNum;
    /**
     * 小散工程名称
     */
    private String projectName;
    /**
     * 小散工程状态
     */
    private String status;
    /**
     * 小散工程状态Code
     */
    private String statusCode;
    /**
     * 乙方
     */
    private String partyB;
    /**
     * 完成时限
     */
    private LocalDateTime overTime;
    /**
     * 监管部门
     */
    private List<OrganizationDTO> superviseDepList;
    /**
     * 监管负责人
     */
    private List<UserDTO> superviseUser;
    /**
     * 工程进度
     */
    private Integer schedule;
    /**
     * 验收结果
     */
    private String checkAcceptResult;
    /**
     * 验收备注
     */
    private String checkAcceptRemark;
    /**
     * 进度文件
     */
    private List<FileVO> checkFiles;

    /**
     * 工程类型
     */
    private Long projectType;
    /**
     * 工程类型名称
     */
    private String projectTypeName;
    /**
     * 施工地点
     */
    private String constructionSite;
    private String remarks;
    private List<FileVO> projectFiles;
    private LocalDateTime completionTime;

    /**
     * 工期
     */
    private String duration;

    /**
     * 施工方联系方式
     */
    private String partyBMobile;

    /**
     * 档位号
     */
    private String stallCode;

    /**
     * 档位联系人
     */

    private String stallLiaison;

    /**
     * 档位联系方式
     */
    private String stallMobile;

    /**
     * 安全对接人
     */
    private List<UserDTO> safetyDockUser;

    /**
     * 建设方
     */
    private String constructB;
}
